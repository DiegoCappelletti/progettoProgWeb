/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.servlets;

import it.serviziosanitario.persistence.dao.PazienteDAO;
import it.serviziosanitario.persistence.entities.Paziente;
import it.serviziosanitario.persistence.entities.Utente;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException;
import it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Part;


/**
 *
 * @author loghi
 */
@MultipartConfig
public class FotoServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private PazienteDAO pazienteDao;

    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory for patient");
        }
        try {
            pazienteDao = daoFactory.getDAO(PazienteDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for patient", ex);
        }
    }
    
    
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String avatarsFolder = getServletContext().getInitParameter("avatarsFolder");
        if (avatarsFolder == null) {
            throw new ServletException("Avatars folder not configured");
        }
        
        //questa è la prima cartella: si salvano in target quindi ad ogni build vengono cancellati
        avatarsFolder = getServletContext().getRealPath(avatarsFolder);
        
        String rootPath = getServletContext().getInitParameter("rootPath");
        //seconda cartella: da qui le foto vengono prese quando si fa build
        String avatarsFolder2 = rootPath + "\\src\\main\\webapp\\images\\avatars";
        
        // Se le cartelle non esisistono si creano
        //prima cartella
        File avatarFolder = new File(avatarsFolder);
        if (!avatarFolder.exists()) {
            avatarFolder.mkdir();
        }
        //seconda cartella
        File avatarFolder2 = new File(avatarsFolder2);
        if (!avatarFolder2.exists()) {
            avatarFolder2.mkdir();
        }
        
        
        String ContextPath = getServletContext().getContextPath();
        if (!ContextPath.endsWith("/")) {
            ContextPath += "/";
        }

        
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        Utente utente = (Utente) session.getAttribute("utente");
        Integer userId = utente.getUtenteId();
        
        
        Paziente paziente = null;
        if (userId != null) {
            try {
                paziente = pazienteDao.getByPrimaryKey(userId);
            } catch (DAOException ex) {
                response.sendError(500, ex.getMessage());
                return;
            }
        }

        if (paziente == null) {
            response.sendRedirect(ContextPath + "login.html");
            return;
        }
        
        File patientFolder = new File(avatarsFolder+"/"+userId);
        if (!patientFolder.exists()) {
            patientFolder.mkdir();
         }
        //elimina il file nella prima directory
        for(File file: patientFolder.listFiles()) 
            if (!file.isDirectory()) 
            file.delete();
        
        File patientFolder2 = new File(avatarsFolder2+"/"+userId);
        if (!patientFolder2.exists()) {
            patientFolder2.mkdir();
         }
        //elimina il file nella seconda directory
        for(File file: patientFolder2.listFiles()) 
            if (!file.isDirectory()) 
            file.delete();

        Part filePart = request.getPart("avatar");

        Date current_date = new Date();
        String date_string = current_date.toString();
        //remove spaces
        date_string = date_string.replaceAll("\\s","");
        //remove punctuation
        date_string = date_string.replaceAll("\\W","");
        if ((filePart != null) && (filePart.getSize() > 0)) {
            //String filename = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();//MSIE  fix.
            String filename = userId+date_string+".jpg";
            try (InputStream fileContent = filePart.getInputStream()) {
                //prima in target
                File file = new File(avatarsFolder+"/"+userId, filename);
                Files.copy(fileContent, file.toPath());
                //una seconda copia in src
                InputStream fileContent2 = filePart.getInputStream();
                File file2 = new File(avatarsFolder2+"/"+userId, filename);
                Files.copy(fileContent2, file2.toPath());
                //cancello la vecchia foto dal db
                pazienteDao.deleteFoto(userId);
                //inserisco la nuova foto nel db
                pazienteDao.insertFoto(paziente, "images/avatars/"+userId+"/"+filename);
            }catch (FileAlreadyExistsException ex) {
                getServletContext().log("File \"" + filename + "\" already exists on the server");
            } catch (RuntimeException ex) {
                getServletContext().log("impossible to upload the file", ex);
            } catch (DAOException ex) {
                Logger.getLogger(FotoServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
        response.sendRedirect(response.encodeRedirectURL(ContextPath + "riservato/paziente/profilo.html?id=" + paziente.getUtenteId()));
        
    }
}
