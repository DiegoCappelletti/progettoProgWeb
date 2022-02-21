/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.servlets;

import be.quodlibet.boxable.utils.PDStreamUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import it.serviziosanitario.persistence.dao.PazienteDAO;
import it.serviziosanitario.persistence.entities.Ricetta;
import it.serviziosanitario.persistence.entities.Utente;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException;
import it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

/**
 *
 * @author loghi
 */
public class QRServlet extends HttpServlet {

    private PazienteDAO pazienteDAO; 
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory");
        }
        try {
            pazienteDAO = daoFactory.getDAO(PazienteDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for paziente", ex);
        }
    }

    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String ContextPath = getServletContext().getContextPath();
        if (!ContextPath.endsWith("/")) {
            ContextPath += "/";
        }
        
        String pdfFolder = getServletContext().getInitParameter("pdfFolder");
        if (pdfFolder == null) {
            throw new ServletException("PDFs folder not configured");
        }
        
        pdfFolder = getServletContext().getRealPath(pdfFolder);
        
        boolean controllo = false;
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        Utente utente = (Utente) session.getAttribute("utente");
        Integer userId = utente.getUtenteId();
        
        
        Integer ricettaId = null;
        try {
            ricettaId = Integer.valueOf(request.getParameter("id"));
        } catch (NumberFormatException | NullPointerException ex) {
            throw new ServletException("ricettaId non valida");
        }
        
        //controllo se l'utente autenticato ha quella ricetta
        try {
            controllo = pazienteDAO.checkRicetta(userId, ricettaId);
        } catch (DAOException ex) {
            Logger.getLogger(QRServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(controllo){
            Ricetta ricetta = null;
            try {
                ricetta = pazienteDAO.getRicettaByPrimaryKey(ricettaId);
            } catch (DAOException ex) {
                Logger.getLogger(QRServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            //le informazioni da inserire nel qr
            String output="";
            output="Id medico: "+ricetta.getMedico().getUtenteId()+
                    "\nCodice fiscale: "+ricetta.getPaziente().getCodiceFiscale()+
                    "\nData prescrizione: "+new SimpleDateFormat("dd-MM-yyyy  HH:mm ").format(ricetta.getDataErogazione())+
                    "\nId ricetta farmaceutica: "+ricetta.getRicettaId()+
                    "\nNome Farmaco: "+ricetta.getFarmaco().getNome()+
                    "\nId Farmaco: "+ricetta.getFarmaco().getFarmacoId()+
                    "\nCosto: "+ricetta.getFarmaco().getCosto()+" euro";
            byte[] image = null;
            try {
                image = getQRCodeImage(output, 125, 125);
            } catch (WriterException ex) {
                System.out.println("Could not generate QR Code, WriterException : " + ex.getMessage());
            }

            ByteArrayInputStream bais = new ByteArrayInputStream(image);
            BufferedImage bim = ImageIO.read(bais);



            try (PDDocument doc = new PDDocument()) {
                //l'immagine del qr code da usare
                PDImageXObject pdImage = LosslessFactory.createFromImage(doc, bim);

                PDPage page = new PDPage();
                doc.addPage(page);
                PDPageContentStream contents = new PDPageContentStream(doc, page);
                {
                    PDStreamUtils.write(
                            contents,
                            "Ricetta farmaceutica",
                            PDType1Font.HELVETICA_BOLD,
                            26,
                            200,
                            725,
                            Color.BLACK);
                    PDStreamUtils.write(
                            contents,
                            new SimpleDateFormat("dd-MM-yyyy  HH:mm ").format(new Date()),
                            PDType1Font.HELVETICA,
                            10,
                            500,
                            750,
                            Color.BLACK);

                    contents.drawImage(pdImage, 250, 570);

                    float y = 550;

                    PDStreamUtils.write(
                            contents,
                            "Nome: "+ricetta.getPaziente().getNome(),
                            PDType1Font.HELVETICA,
                            14,
                            80,
                            y,
                            Color.BLACK);
                    y=y-15;
                    PDStreamUtils.write(
                            contents,
                            "Cognome: "+ricetta.getPaziente().getCognome(),
                            PDType1Font.HELVETICA,
                            14,
                            80,
                            y,
                            Color.BLACK);
                    y=y-15;
                    PDStreamUtils.write(
                            contents,
                            "Data di nascita: "+ricetta.getPaziente().getDataNascita(),
                            PDType1Font.HELVETICA,
                            14,
                            80,
                            y,
                            Color.BLACK);
                    y=y-15;
                    PDStreamUtils.write(
                            contents,
                            "Codice fiscale: "+ricetta.getPaziente().getCodiceFiscale(),
                            PDType1Font.HELVETICA,
                            14,
                            80,
                            y,
                            Color.BLACK);

                    y=y-15;
                    PDStreamUtils.write(
                            contents,
                            "Medico di base: "+ricetta.getMedico().getNome()+" "+ricetta.getMedico().getCognome(),
                            PDType1Font.HELVETICA,
                            14,
                            80,
                            y,
                            Color.BLACK);
                    y=y-15;
                    PDStreamUtils.write(
                            contents,
                            "Farmaco: "+ricetta.getFarmaco().getNome(),
                            PDType1Font.HELVETICA,
                            14,
                            80,
                            y,
                            Color.BLACK);
                    y=y-15;
                    PDStreamUtils.write(
                            contents,
                            "Costo: "+ricetta.getFarmaco().getCosto()+" €",
                            PDType1Font.HELVETICA,
                            14,
                            80,
                            y,
                            Color.BLACK);
                    y=y-15;
                    String data_prescrizione = new SimpleDateFormat("dd-MM-yyyy  HH:mm ").format(ricetta.getDataErogazione());
                    PDStreamUtils.write(
                            contents,
                            "Data prescrizione: "+data_prescrizione,
                            PDType1Font.HELVETICA,
                            14,
                            80,
                            y,
                            Color.BLACK);

                    contents.close();
                    doc.save(new File(pdfFolder, "user-" + ricettaId + "-" + Calendar.getInstance().getTimeInMillis() + ".pdf"));

                    response.setContentType("application/pdf");
                    String download = "attachment; filename=ricetta_"+ricetta.getRicettaId()+".pdf";
                    response.setHeader("Content-disposition", download);
                    doc.save(response.getOutputStream());
                }
            }
        }else{
            response.sendRedirect(response.encodeRedirectURL(ContextPath + "/riservato/paziente/profilo.html"));
        }
        
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private byte[] getQRCodeImage(String text, int width, int height) throws WriterException, IOException {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        byte[] pngData = pngOutputStream.toByteArray(); 
        return pngData;
}
}
