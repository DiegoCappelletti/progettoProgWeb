package it.serviziosanitario.servlets;

import it.serviziosanitario.persistence.dao.UtenteDAO;
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
import javax.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {

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
            throw new ServletException("Impossibile recupera il dao factory per l'utente", ex);
        }
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        int UtenteId = (Integer) session.getAttribute("idUtente");
        session.removeAttribute("utente");
        session.invalidate();
        
        Cookie []cookies = request.getCookies();
        for(Cookie ck : cookies){
            if(ck.getName().equalsIgnoreCase("token")){
                ck.setValue("");
                ck.setMaxAge(0);
                response.addCookie(ck);
                try {
                    utenteDAO.deleteCookie(UtenteId);
                } catch (DAOException ex) {
                    Logger.getLogger(LogoutServlet.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        
        if (!response.isCommitted()) {
            response.sendRedirect(response.encodeRedirectURL(contextPath + "login.html"));
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
