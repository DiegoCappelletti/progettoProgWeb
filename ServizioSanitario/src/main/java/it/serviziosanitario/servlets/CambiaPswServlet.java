/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.servlets;

import it.serviziosanitario.other.PasswordHasher;
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
import org.javatuples.Pair;

/**
 *
 * @author loghi
 */
public class CambiaPswServlet extends HttpServlet {

    private UtenteDAO utenteDao;
    
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for user");
        }
        try {
            utenteDao = daoFactory.getDAO(UtenteDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for user", ex);
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
        
        //cambia password
        String nuovaPassword =request.getParameter("password1");
        String confermaPassword=request.getParameter("password2");
        if(nuovaPassword.equalsIgnoreCase(confermaPassword)){
            Pair<String,String> pair = PasswordHasher.generateHashAndSalt(nuovaPassword);
            String genHash = pair.getValue0();
            String genSalt = pair.getValue1();
            utente.setHash(genHash);
            utente.setSalt(genSalt);

            try {
                utenteDao.updateUtente(utente);
            } catch (DAOException ex) {
                Logger.getLogger(PazienteServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
                    System.out.println(utente);
                    System.out.println("newHash: "+genHash);
                    System.out.println("newSalt: "+genSalt);
        }
        
        String ContextPath = getServletContext().getContextPath();
        if (!ContextPath.endsWith("/")) {
            ContextPath += "/";
        }
        
        System.out.println(ContextPath + "riservato/paziente/profilo.html");
        if(utente.getRuoloUtente().equalsIgnoreCase("PAZIENTE")){
            response.sendRedirect(response.encodeRedirectURL(ContextPath + "riservato/paziente/profilo.html"));
        }else if(utente.getRuoloUtente().equalsIgnoreCase("MEDICO")){
            response.sendRedirect(response.encodeRedirectURL(ContextPath + "riservato/medico/profilo.html"));
        }else if(utente.getRuoloUtente().equalsIgnoreCase("SSP")){
            response.sendRedirect(response.encodeRedirectURL(ContextPath + "riservato/ssp/profilo.html"));
        }
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
