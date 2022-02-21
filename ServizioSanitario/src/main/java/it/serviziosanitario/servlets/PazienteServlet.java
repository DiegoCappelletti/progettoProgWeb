/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.servlets;

import it.serviziosanitario.persistence.dao.PazienteDAO;
import it.serviziosanitario.persistence.dao.UtenteDAO;
import it.serviziosanitario.persistence.entities.Utente;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException;
import it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author loghi
 */
public class PazienteServlet extends HttpServlet {

    private PazienteDAO pazienteDao;
    private UtenteDAO utenteDao;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for patient");
        }
        try {
            pazienteDao = daoFactory.getDAO(PazienteDAO.class);
            utenteDao = daoFactory.getDAO(UtenteDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for patient", ex);
        }
    }
    

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        Utente utente = (Utente) session.getAttribute("utente");
        Integer pazienteId = utente.getUtenteId();
        
        
        String richiesta = request.getParameter("richiesta");
        //System.out.println("Richiesta ricevuta nella servlet: "+richiesta);
        
//        System.out.println("MEDICO SCELTO:    "+medicoId);
//        System.out.println("PAZIENTE LOGGATO:    "+pazienteId);

        String ContextPath = getServletContext().getContextPath();
        if (!ContextPath.endsWith("/")) {
            ContextPath += "/";
        }
            
        if(richiesta.equalsIgnoreCase("CambiaMedico")){
            //cambia medico di base
            Integer medicoId = null;
            medicoId = Integer.parseInt(request.getParameter("medicoId"));
            try {
                pazienteDao.cambiaMedico(pazienteId, medicoId);
            } catch (DAOException ex) {
                Logger.getLogger(PazienteServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        response.sendRedirect(response.encodeRedirectURL(ContextPath + "riservato/paziente/profilo.html"));
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
