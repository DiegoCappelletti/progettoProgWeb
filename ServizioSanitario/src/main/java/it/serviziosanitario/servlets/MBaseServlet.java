/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.servlets;

import static it.serviziosanitario.other.SendEmail.SendEmail;
import it.serviziosanitario.persistence.dao.MedicoDAO;
import it.serviziosanitario.persistence.dao.PazienteDAO;
import it.serviziosanitario.persistence.dao.UtenteDAO;
import it.serviziosanitario.persistence.entities.Paziente;
import it.serviziosanitario.persistence.entities.Utente;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException;
import it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author loghi
 */
public class MBaseServlet extends HttpServlet {

    
    private MedicoDAO medicoDao;
    private PazienteDAO pazienteDao;
    private UtenteDAO utenteDao;
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for patient");
        }
        try {
            medicoDao = daoFactory.getDAO(MedicoDAO.class);
            pazienteDao = daoFactory.getDAO(PazienteDAO.class);
            utenteDao = daoFactory.getDAO(UtenteDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for patient and/or medic", ex);
        }
    }


    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ContextPath = getServletContext().getContextPath();
        if (!ContextPath.endsWith("/")) {
            ContextPath += "/";
        }
        String redirect="";
        Integer medicoId = null;
        Integer pazienteId = null;
        
        
        Integer esameId = null;
        Integer visitaId = null;
        Integer farmacoId = null;
        
        //recupero il medico loggato
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        Utente utente = (Utente) session.getAttribute("utente");
        medicoId = utente.getUtenteId();
        
        
        String tipoRichiesta = request.getParameter("tipoRichiesta");
        String text="";//testo per inviare nella mail
        //nuovo esame da erogare
        if(tipoRichiesta.equalsIgnoreCase("nuovoEsame")){
            text="un nuovo esame prescritto";
            if(!request.getParameterMap().containsKey("esameId")){
                redirect=ContextPath + "riservato/medico/scheda-paziente-b.html?id="+pazienteId;
            }else{
                esameId = Integer.parseInt(request.getParameter("esameId"));
                pazienteId = Integer.parseInt(request.getParameter("nuovoEsamePazienteId"));
    //            System.out.println("It's an exam : "+esameId);
    //            System.out.println("Medic : "+medicoId);
    //            System.out.println("Patient : "+pazienteId);
                try {
                    medicoDao.InserisciEsame(pazienteId, medicoId, esameId); 
                } catch (DAOException ex) {
                    Logger.getLogger(MBaseServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        //nuova visita da erogare    
        }else if(tipoRichiesta.equalsIgnoreCase("nuovaVisita")){
            text="una nuova visita prescritta";
            visitaId = Integer.parseInt(request.getParameter("visitaId"));
            pazienteId = Integer.parseInt(request.getParameter("nuovaVisitaPazienteId"));
//            System.out.println("It's a visit : "+visitaId);
//            System.out.println("Medic : "+medicoId);
//            System.out.println("Patient : "+pazienteId);
            try {
                medicoDao.InserisciVisita(pazienteId, medicoId, visitaId);
            } catch (DAOException ex) {
                Logger.getLogger(MBaseServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        //nuova ricetta da erogare
        }else if(tipoRichiesta.equalsIgnoreCase("nuovaRicetta")){
            text="un nuovo farmaco prescritto";
            farmacoId = Integer.parseInt(request.getParameter("farmacoId"));
            pazienteId = Integer.parseInt(request.getParameter("nuovaRicettaPazienteId"));
            
            try {
                medicoDao.ErogaRicetta(pazienteId, medicoId, farmacoId);
            } catch (DAOException ex) {
                Logger.getLogger(MBaseServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        
        //manda una mail al paziente
        Paziente paziente = null;
        Utente utentePaziente = null;
        try {
            paziente = pazienteDao.getByPrimaryKey(pazienteId);
            utentePaziente = utenteDao.getByPrimaryKey(pazienteId);
        } catch (DAOException ex) {
            Logger.getLogger(MBaseServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        String messaggio;
        messaggio = paziente.getNome()+" "+paziente.getCognome()+",\nhai "+text+". Accedi subito al "+
                "sito del Servizio Sanitario e consulta le nuove informazioni disponibili.\nCordiali saluti.";
//        System.out.println("Email inviata a: "+utentePaziente.getEmail());
//        System.out.println(messaggio);

//todo:   manda mail
        try {
            SendEmail(utentePaziente.getEmail(), "Nuova prescrizione", messaggio);
        } catch (MessagingException ex) {
            Logger.getLogger(SspServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        response.sendRedirect(response.encodeRedirectURL(ContextPath + "riservato/medico/scheda-paziente-b.html?id="+pazienteId));
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
