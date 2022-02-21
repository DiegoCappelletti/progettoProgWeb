/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.other;

import it.serviziosanitario.persistence.dao.UtenteDAO;
import it.serviziosanitario.persistence.entities.Utente;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException;
import it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.InetAddress;

/**
 *
 * @author diego
 */
public class SendEmail extends HttpServlet {
    
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String contextPath = getServletContext().getContextPath();
        if (!contextPath.endsWith("/")) {
            contextPath += "/";
        }
        
        String email = request.getParameter("mail"); 
        Utente utente = null;
        
        System.out.println("Invio...");
        
        try {
            utente = utenteDAO.getByEmail(email);
            if(utente == null) {
                request.setAttribute("success", false);
                request.setAttribute("message", "Email non valida.");
                request.getRequestDispatcher("changePassword.html").forward(request, response);
                return;
            }
            
            String cod = UUID.randomUUID().toString().replace("-", "");
            String link = "richiestaCambioPassword.html?link=" + cod;
            
            System.out.println(link); 
            
            //elimino link precedenti dell'utente se esiste
            utenteDAO.deleteLink(utente.getUtenteId());
            utenteDAO.creaLink(utente, cod);
            //recupero l'ip di questo server
            InetAddress inetAddress = InetAddress.getLocalHost();
            //System.out.println("IP Address:- " + inetAddress.getHostAddress()); 
            SendEmail(email, "Richiesta cambio password", "Per effetttuare il cambio della password accedere alla seguente pagina e compilare il modulo.\n\nhttp://"+inetAddress.getHostAddress()+":8080" + contextPath + link);
            System.out.println("Messaggio inviato.");
            
        } catch (MessagingException ex) {
            System.err.println("Errore nell'invio della email: " + ex + "\n\n" +SendEmail.class.getName());
        } catch (DAOException ex) {
            Logger.getLogger(SendEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        request.setAttribute("success", true);
        request.setAttribute("message", "E' stata inviata un'email all'indirizzo indicato.");
        request.getRequestDispatcher("changePassword.html").forward(request, response);
    }

    /**
     * Send email.
     * @param destEmail TO recipient (destinatario)
     * @param title title of the message
     * @param message message to be sent
     * @throws AddressException if the email address parse failed
     * @throws MessagingException if the connection is dead or not in the connected state or if the message is not a MimeMessage
     */
    public static void SendEmail(String destEmail, String title, String message) throws AddressException, MessagingException {
        
//        Properties props = new Properties();
//        props.put("mail.smtp.auth", "false");
//        props.put("mail.smtps.host", "smtp.gmail.com"); // Indirizzo ip del server smtp, cambiare con "smtp.gmail.com" se si vuole usare gmail
//        props.put("mail.smtp.port", "465");
//        
//        Session session = Session.getInstance(props,null);
        
        final String host = "smtp.gmail.com";
        final String port = "465";
        final String username = "loghinaz@gmail.com";
        final String password = "baenrhqxzksbhdqi";
        Properties props = System.getProperties();
        
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.port", port);
        props.setProperty("mail.smtp.socketFactory.port", port);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.starttls.enable", "true");
        props.setProperty("mail.debug", "false");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        
        try{
            MimeMessage msg = new MimeMessage(session);
//            msg.setFrom(mittEmail);
            msg.setFrom("ServizioSanitario@ServizioSanitario.it");
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destEmail,false));
            msg.setSubject(title);
            msg.setSentDate(new Date());
            msg.setText(message);
            
            Transport.send(msg);
            
        } catch(MessagingException ex){
            System.err.println("Send failed, exception: " + ex);
        }
    }
    
}
