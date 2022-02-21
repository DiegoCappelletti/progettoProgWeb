package it.serviziosanitario.controller;

import it.serviziosanitario.other.PasswordHasher;
import it.serviziosanitario.persistence.dao.UtenteDAO;
import it.serviziosanitario.persistence.entities.CookieApp;
import it.serviziosanitario.persistence.entities.Utente;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException;
import it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Diego
 */
public class LoginController extends HttpServlet {

    private UtenteDAO utenteDAO;
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossibile trovare il dao factory");
        }
        try {
            utenteDAO = daoFactory.getDAO(UtenteDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for patient storage system", ex);
        }
    }
    
    public LoginController(){
        super();
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
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        Utente utente = checkCookie(request);
        
        if(utente == null){
            response.sendRedirect(response.encodeRedirectURL(contextPath + "login.html"));
        }else{
            
            try {
                Utente checkUtente = utenteDAO.getByEmail(utente.getEmail());
                //System.out.println("check:  "+checkUtente);
                if (checkUtente == null) {
                    //Email not in the database
                    //System.out.println("Patient == NULL");
                    response.sendRedirect(response.encodeRedirectURL(contextPath + "login.html"));
                } else {

                    

                        request.getSession().setAttribute("contextPath", contextPath);
                        request.getSession().setAttribute("idUtente", checkUtente.getUtenteId());
                        request.getSession().setAttribute("utente", checkUtente);
                        
                        switch(checkUtente.getRuoloUtente()) {
                            case "PAZIENTE": {
                                System.out.println("ruolo paziente");
                                response.sendRedirect(response.encodeRedirectURL(contextPath + "riservato/paziente/profilo.html"));
                                break;
                            }
                            case "MEDICO": {
                                System.out.println("ruolo medico");
                                response.sendRedirect(response.encodeRedirectURL(contextPath + "riservato/medico/profilo.html"));
                                break;
                            }
                            case "FARMACIA": {
                                System.out.println("ruolo farmacia");
                                response.sendRedirect(response.encodeRedirectURL(contextPath + "riservato/farmacia/profilo.html"));
                                break;
                            }
                            case "SSP": {
                                System.out.println("ruolo ssp");
                                response.sendRedirect(response.encodeRedirectURL(contextPath + "riservato/ssp/profilo.html"));
                                break;
                            }
                            default: {
                                //Ruolo non riconosciuto
                                System.out.println("ruolo non riconosciuto");
                                response.sendRedirect(response.encodeRedirectURL(contextPath + "login.html"));
                            }
                        }
                    
                    
                }
            } catch (DAOException ex) {
                request.getServletContext().log("Impossible to retrieve the user", ex);
            }   
        }
    }

    private Utente checkCookie(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        Utente ut = null;
        if(cookies == null)
            return null;
        else{
            String email = "",password = "";
            String token = "";
            for(Cookie ck : cookies){
                /*if(ck.getName().equalsIgnoreCase("email"))
                    email = ck.getValue();
                if(ck.getName().equalsIgnoreCase("password"))
                    password = ck.getValue();
*/
                if(ck.getName().equalsIgnoreCase("token"))
                    token = ck.getValue();
            }
            /*if(!email.isEmpty() && !password.isEmpty()){
                ut = new Utente();
                ut.setEmail(email);
                ut.setHash(password);
            }
*/
            //System.out.println(ut);
            if(!token.isEmpty()){
                //System.out.println("OK");
                CookieApp cookieApp=null;
                try {
                    cookieApp = utenteDAO.checkCookie(token);
                    //System.out.println(cookieApp);
                } catch (DAOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                try {
                    if(cookieApp!=null){
                        ut = utenteDAO.getByPrimaryKey(cookieApp.getUtenteId());
                    }
                } catch (DAOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }

        }
        //System.out.println(ut);
        return ut;
    }
}
