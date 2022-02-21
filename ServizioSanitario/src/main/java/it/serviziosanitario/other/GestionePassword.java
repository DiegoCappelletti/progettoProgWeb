package it.serviziosanitario.other;

import static it.serviziosanitario.other.PasswordHasher.generateHashAndSalt;
import it.serviziosanitario.persistence.dao.UtenteDAO;
import it.serviziosanitario.persistence.entities.LinkChangePass;
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
import org.javatuples.Pair;

/**
 *
 * @author diego
 */
public class GestionePassword extends HttpServlet {

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
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }

        String password = request.getParameter("password1"); 
        String link = request.getParameter("link");

        LinkChangePass lPass;
        Utente newUtente;

        try {

            lPass = utenteDAO.checkLink(link);
            //cambio la password solo se il link esiste nel database e non è ancora scaduto
            if(lPass != null){
                newUtente = utenteDAO.getByPrimaryKey(lPass.utenteId());

                Pair<String,String> pass = generateHashAndSalt(password);

                newUtente.setHash((String) pass.getValue(0));
                newUtente.setSalt((String) pass.getValue(1));

                utenteDAO.updateUtente(newUtente);
                utenteDAO.deleteLink(newUtente.getUtenteId());

                response.sendRedirect(response.encodeRedirectURL(contextPath + "passwordCambiata.html"));
            }else{
                response.sendRedirect(response.encodeRedirectURL(contextPath + "passwordNonCambiata.html"));
            }

        } catch (DAOException ex) {
            Logger.getLogger(GestionePassword.class.getName()).log(Level.SEVERE, null, ex);
        }  
    }
}
