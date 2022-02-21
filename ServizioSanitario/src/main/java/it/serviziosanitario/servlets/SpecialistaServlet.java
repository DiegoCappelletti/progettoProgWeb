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
import it.serviziosanitario.persistence.entities.Medico;
import it.serviziosanitario.persistence.entities.Paziente;
import it.serviziosanitario.persistence.entities.Utente;
import it.serviziosanitario.persistence.entities.Visita;
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
public class SpecialistaServlet extends HttpServlet {

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
            throw new ServletException("Impossible to get dao factory for patient and medic", ex);
        }
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String risultati = null;
        Integer visitaId = null;
        Integer medicoId = null;
        
        Visita visita = null;
        Medico medicoS = null;
        Paziente paziente = null;
        Utente utentePaziente = null;
        
        risultati = request.getParameter("risultati");
        visitaId = Integer.parseInt(request.getParameter("visitaId"));
        
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        Utente utente = (Utente) session.getAttribute("utente");
        medicoId = utente.getUtenteId();
//        System.out.println(risultati+"\n"+visitaId);
        try {
            visita = medicoDao.getVisitaByPrimaryKey(visitaId);
            medicoS = medicoDao.getByPrimaryKey(medicoId);
            medicoDao.EseguiVisita(medicoS, visita, risultati);
            
        } catch (DAOException ex) {
            Logger.getLogger(SpecialistaServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //manda una mail al paziente
        
        try {
            paziente = pazienteDao.getByPrimaryKey(visita.getPaziente().getUtenteId());
            utentePaziente = utenteDao.getByPrimaryKey(visita.getPaziente().getUtenteId());
        } catch (DAOException ex) {
            Logger.getLogger(MBaseServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        String messaggio;
        messaggio = paziente.getNome()+" "+paziente.getCognome()+",\nhai un nuovo referto da leggere. Accedi subito al "+
                "sito del Servizio Sanitario e consulta le nuove informazioni disponibili.\nCordiali saluti.";
        
        try {
            SendEmail(utentePaziente.getEmail(), "Nuovo referto", messaggio);
        } catch (MessagingException ex) {
            Logger.getLogger(SspServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String ContextPath = getServletContext().getContextPath();
        if (!ContextPath.endsWith("/")) {
            ContextPath += "/";
        }
        
        response.sendRedirect(response.encodeRedirectURL(ContextPath + "riservato/medico/scheda-paziente-s.html?id="+utentePaziente.getUtenteId()));
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
