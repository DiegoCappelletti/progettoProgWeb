
package it.serviziosanitario.servlets;

import it.serviziosanitario.other.PasswordHasher;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException;
import it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory;
import it.serviziosanitario.persistence.dao.UtenteDAO;
import it.serviziosanitario.persistence.entities.Utente;
import java.io.IOException;
import java.util.UUID;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class LoginServlet extends HttpServlet {

    final int secondi = 60*60*24*30; //durata dei cookie   30 giorni
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
            throw new ServletException("Impossibile recuperare il dao factory per il paziente", ex);
        }
    }
    
    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     *
     * @author Alessio Gottardi
     * @since 1.0.0.190504
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("username");
        String password = request.getParameter("password");
        boolean remember = request.getParameter("rememberMe") != null;

        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        
        try {
            Utente utente = utenteDAO.getByEmail(email);
            if(utente == null){
                //L'email non si trova nel database
                request.setAttribute("error", "L'utente indicato non esiste.");
                request.getRequestDispatcher("login.html").forward(request, response);
            } else {
                if(PasswordHasher.authenticate(password, utente.getHash(), utente.getSalt())) {
                    request.getSession().setAttribute("contextPath", contextPath);
                    request.getSession().setAttribute("idUtente", utente.getUtenteId());
                    request.getSession().setAttribute("utente", utente);
                    switch(utente.getRuoloUtente()) {
                        case "PAZIENTE": {
                            System.out.println("ruolo paziente");
                                                        
                            if(remember){
                                /*Cookie ckPazienteEmail = new Cookie("email",email);
                                ckPazienteEmail.setMaxAge(60); // 60 secondi
                                response.addCookie(ckPazienteEmail);

                                Cookie ckPazientePassword = new Cookie("password",password);
                                ckPazientePassword.setMaxAge(60);
                                response.addCookie(ckPazientePassword);
*/
                                
                                
                                String token = UUID.randomUUID().toString().replace("-", "");
                                utenteDAO.deleteCookie(utente.getUtenteId());
                                utenteDAO.creaCookie(utente, secondi, token);
                                Cookie ckToken = new Cookie("token", token);
                                ckToken.setMaxAge(secondi);
                                //System.out.println(secondi);
                                response.addCookie(ckToken);
                            }
                            
                            response.sendRedirect(response.encodeRedirectURL(contextPath + "riservato/paziente/profilo.html"));
                            break;
                        }
                        case "MEDICO": {
                            System.out.println("ruolo medico");
                                                        
                            if(remember){
                                
//                                Cookie ckMedicoEmail = new Cookie("email",email);
//                                ckMedicoEmail.setMaxAge(60);
//                                response.addCookie(ckMedicoEmail);
//                                
//                                Cookie ckMecicoPassword = new Cookie("password",password);
//                                ckMecicoPassword.setMaxAge(60);
//                                response.addCookie(ckMecicoPassword);
                                
                                String token = UUID.randomUUID().toString().replace("-", "");
                                utenteDAO.deleteCookie(utente.getUtenteId());
                                utenteDAO.creaCookie(utente, secondi, token);
                                Cookie ckToken = new Cookie("token", token);
                                ckToken.setMaxAge(secondi);
                                response.addCookie(ckToken);
                            }
                            
                            response.sendRedirect(response.encodeRedirectURL(contextPath + "riservato/medico/profilo.html"));
                            break;
                        }
                        case "FARMACIA": {
                            System.out.println("ruolo farmacia");
                                                        
                            if(remember){
                                
//                                Cookie ckFarmaciaEmail = new Cookie("email",email);
//                                ckFarmaciaEmail.setMaxAge(60);
//                                response.addCookie(ckFarmaciaEmail);
//                                
//                                Cookie ckFarmaciaPassword = new Cookie("password",password);
//                                ckFarmaciaPassword.setMaxAge(60);
//                                response.addCookie(ckFarmaciaPassword);
                                
                                String token = UUID.randomUUID().toString().replace("-", "");
                                utenteDAO.deleteCookie(utente.getUtenteId());
                                utenteDAO.creaCookie(utente, secondi, token);
                                Cookie ckToken = new Cookie("token", token);
                                ckToken.setMaxAge(secondi);
                                response.addCookie(ckToken);

                            }
                            
                            response.sendRedirect(response.encodeRedirectURL(contextPath + "riservato/farmacia/profilo.html"));
                            break;
                        }
                        case "SSP": {
                            System.out.println("ruolo ssp");
                                                        
                            if(remember){
//                                Cookie ckSSPEmail = new Cookie("email",email);
//                                ckSSPEmail.setMaxAge(60);
//                                response.addCookie(ckSSPEmail);
//                                
//                                Cookie ckSSPPassword = new Cookie("password",password);
//                                ckSSPPassword.setMaxAge(60);
//                                response.addCookie(ckSSPPassword);
                                
                                String token = UUID.randomUUID().toString().replace("-", "");
                                utenteDAO.deleteCookie(utente.getUtenteId());
                                utenteDAO.creaCookie(utente, secondi, token);
                                Cookie ckToken = new Cookie("token", token);
                                ckToken.setMaxAge(secondi);
                                response.addCookie(ckToken);
                            }
                            
                            response.sendRedirect(response.encodeRedirectURL(contextPath + "riservato/ssp/profilo.html"));
                            break;
                        }
                        default: {
                            //Ruolo non riconosciuto
                            System.out.println("ruolo non riconosciuto");
                            request.setAttribute("error", "Ruolo utente non riconosciuto.");
                            request.getRequestDispatcher("login.html").forward(request, response);
                        }
                    }
                }else {
                    //La password non è corretta
                    System.out.println("invalid password");
                    request.setAttribute("error", "Password non corretta.");
                    request.getRequestDispatcher("login.html").forward(request, response);
                }
            }
        } catch (DAOException ex) {
            //TODO: log exception
            System.out.println("exeption");
            request.getServletContext().log("Impossibile recuperare l'utente", ex);
        }
    }

}
