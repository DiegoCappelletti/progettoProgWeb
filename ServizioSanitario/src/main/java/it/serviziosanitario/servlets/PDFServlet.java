
package it.serviziosanitario.servlets;

import be.quodlibet.boxable.BaseTable;
import be.quodlibet.boxable.Cell;
import be.quodlibet.boxable.Row;
import be.quodlibet.boxable.utils.PDStreamUtils;
import it.serviziosanitario.persistence.dao.PazienteDAO;
import it.serviziosanitario.persistence.entities.Esame;
import it.serviziosanitario.persistence.entities.Paziente;
import it.serviziosanitario.persistence.entities.Ricetta;
import it.serviziosanitario.persistence.entities.Utente;
import it.serviziosanitario.persistence.entities.Visita;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException;
import it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import static org.apache.pdfbox.pdmodel.font.PDType1Font.HELVETICA_BOLD;


public class PDFServlet extends HttpServlet {

    final int text_size = 14;
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
        
        String pdfFolder = getServletContext().getInitParameter("pdfFolder");
        if (pdfFolder == null) {
            throw new ServletException("PDFs folder not configured");
        }
        
        pdfFolder = getServletContext().getRealPath(pdfFolder);

        //prendo l'utente dalla sessione
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        Utente utente = (Utente) session.getAttribute("utente");
        Integer userId = utente.getUtenteId();
        

        Paziente paziente = null;
        try {
            paziente = pazienteDAO.getByPrimaryKey(userId);
        } catch (DAOException ex) {
            throw new ServletException("Impossibile recuperare il paziente con id: " + userId, ex);
        }

        //recuper visite, esami, e ricette
        List<Visita> visite = null;
        List<Esame> esami = null;
        List<Ricetta> ricette = null;
        try {
            visite = pazienteDAO.getAllVisiteByPaziente(paziente);
            esami = pazienteDAO.getAllEsamiByPaziente(paziente);
            ricette = pazienteDAO.getAllRicetteByPaziente(paziente);
        } catch (DAOException ex) {
            throw new ServletException("Impossible recuperare visite/esami/ricette del paziente con id: " + userId, ex);
        }

        
        try (PDDocument doc = new PDDocument()) {
            
            PDPage page = new PDPage();
            doc.addPage(page);
            PDPageContentStream contents = new PDPageContentStream(doc, page);
            {
                //Titolo
                PDStreamUtils.write(
                        contents,
                        "Lista tickets",
                        PDType1Font.HELVETICA_BOLD,
                        26,
                        250,
                        700,
                        Color.BLACK);
                //Data corrente
                PDStreamUtils.write(
                        contents,
                        new SimpleDateFormat("dd-MM-yyyy  HH:mm ").format(new Date()),
                        PDType1Font.HELVETICA,
                        10,
                        500,
                        750,
                        Color.BLACK);
                //Dati del paziente
                float y = 675;
                y=y-30;
                PDStreamUtils.write(
                        contents,
                        "Nome: "+paziente.getNome(),
                        PDType1Font.HELVETICA,
                        14,
                        80,
                        y,
                        Color.BLACK);
                y=y-15;
                PDStreamUtils.write(
                        contents,
                        "Cognome: "+paziente.getCognome(),
                        PDType1Font.HELVETICA,
                        14,
                        80,
                        y,
                        Color.BLACK);
                y=y-15;
                PDStreamUtils.write(
                        contents,
                        "Sesso: "+paziente.getSesso(),
                        PDType1Font.HELVETICA,
                        14,
                        80,
                        y,
                        Color.BLACK);
                y=y-15;
                PDStreamUtils.write(
                        contents,
                        "Data di nascita: "+paziente.getDataNascita(),
                        PDType1Font.HELVETICA,
                        14,
                        80,
                        y,
                        Color.BLACK);
                y=y-15;
                PDStreamUtils.write(
                        contents,
                        "Codice fiscale: "+paziente.getCodiceFiscale(),
                        PDType1Font.HELVETICA,
                        14,
                        80,
                        y,
                        Color.BLACK);
                
                
                
                float somma = 0;
                float sommaTot = 0;
                
                
                y=y-50;
                PDStreamUtils.write(
                        contents,
                        "Tickets esami",
                        PDType1Font.HELVETICA,
                        14,
                        250,
                        y,
                        Color.BLACK);
                
                y=y-30;
                float margin = 110;
                float yStartNewPage = page.getMediaBox().getHeight() - (2 * margin);
                float tableWidth = page.getMediaBox().getWidth() - (2 * margin);
                
                boolean drawContent = true;
                float yStart = yStartNewPage;
                float bottomMargin = 60;
                
                //Lista degli esami (solo eseguiti, cioè solo pagati)
                if(!esami.isEmpty()){
                    BaseTable table = new BaseTable(y, 750, bottomMargin, tableWidth, margin, doc, page, true, drawContent);
                    Row<PDPage> header = table.createRow(20);
                    Cell cell;
                    cell = header.createCell(30, "Esame");
                    cell.setFont(HELVETICA_BOLD);
                    cell.setFontSize(text_size);
                    cell = header.createCell(20, "Data");
                    cell.setFont(HELVETICA_BOLD);
                    cell.setFontSize(text_size);
                    cell = header.createCell(25, "Ssp");
                    cell.setFont(HELVETICA_BOLD);
                    cell.setFontSize(text_size);
                    cell = header.createCell(20, "Ticket");
                    cell.setFont(HELVETICA_BOLD);
                    cell.setFontSize(text_size);
                    table.addHeaderRow(header);



                    for (Esame esame : esami) {
                        if(esame.isEseguito()){
                            Row<PDPage> row = table.createRow(12);
                            row.createCell(esame.getTipoEsame().getNome());
                            row.createCell(new SimpleDateFormat("dd-MM-yyyy  HH:mm").format(esame.getDataEsame()));
                            row.createCell(esame.getSsp().getNome());
                            row.createCell(Float.toString(esame.getTipoEsame().getCosto())+" €");
                            somma = somma+esame.getTipoEsame().getCosto();
                        }
                    }
                    sommaTot= sommaTot+somma;
                    y = table.draw();

                    if (table.getCurrentPage() != page) {
                        contents.close();
                        page = table.getCurrentPage();
                        contents = new PDPageContentStream(doc, page, true, true);
                    }
                }
                y=y-20;
                PDStreamUtils.write(
                    contents,
                    "Costo tickets esami: " + somma + " €",
                    PDType1Font.HELVETICA_BOLD,
                    14,
                    80,
                    y,
                    Color.BLACK);

                if (y<100) {
                        contents.close();
                        page = new PDPage();
                        doc.addPage(page);
                        contents = new PDPageContentStream(doc, page, true, true);
                        y=800;
                    }

                y=y-50;
                PDStreamUtils.write(
                    contents,
                    "Tickets visite",
                    PDType1Font.HELVETICA,
                    14,
                    250,
                    y,
                    Color.BLACK);

                y=y-30;  
                somma=0;


                //tabella visite (solo eseguite, cioè pagate)
                if(!visite.isEmpty()){
                    BaseTable table = new BaseTable(y, 750, bottomMargin, tableWidth, margin, doc, page, true, drawContent);
                    Row<PDPage> header = table.createRow(20);
                    Cell cell;
                    cell = header.createCell(30, "Visita");
                    cell.setFont(HELVETICA_BOLD);
                    cell.setFontSize(text_size);
                    cell = header.createCell(20, "Data");
                    cell.setFont(HELVETICA_BOLD);
                    cell.setFontSize(text_size);
                    cell = header.createCell(25, "Specialista");
                    cell.setFont(HELVETICA_BOLD);
                    cell.setFontSize(text_size);
                    cell = header.createCell(20, "Ticket");
                    cell.setFont(HELVETICA_BOLD);
                    cell.setFontSize(text_size);
                    table.addHeaderRow(header);



                    for (Visita visita : visite) {
                        if(visita.isEseguita()){
                            Row<PDPage> row = table.createRow(12);
                            row.createCell(visita.getTipoVisita().getNome());
                            row.createCell(new SimpleDateFormat("dd-MM-yyyy  HH:mm").format(visita.getDataVisita()));
                            row.createCell(visita.getMedicoSpecialista().getNome()+" "+visita.getMedicoSpecialista().getCognome() );
                            row.createCell(Float.toString(visita.getTipoVisita().getCosto())+" €");
                            somma = somma+visita.getTipoVisita().getCosto();
                        }
                    }
                    sommaTot= sommaTot+somma;
                    y = table.draw();

                    if (table.getCurrentPage() != page) {
                        contents.close();
                        page = table.getCurrentPage();
                        contents = new PDPageContentStream(doc, page, true, true);
                    }
                }
                y=y-20;
                PDStreamUtils.write(
                        contents,
                        "Costo tickets visite: " + somma + " €",
                        PDType1Font.HELVETICA_BOLD,
                        14,
                        80,
                        y,
                        Color.BLACK);  

                if (y<100) {
                        contents.close();
                        page = new PDPage();
                        doc.addPage(page);
                        contents = new PDPageContentStream(doc, page, true, true);
                        y=800;
                    }

                y=y-50;
                PDStreamUtils.write(
                    contents,
                    "Tickets ricette",
                    PDType1Font.HELVETICA,
                    14,
                    250,
                    y,
                    Color.BLACK);



                y=y-30;  
                somma=0;
                //tabella ricette
                if(!ricette.isEmpty()){
                    BaseTable table = new BaseTable(y, 750, bottomMargin, tableWidth, margin, doc, page, true, drawContent);

                    Cell cell;
                    Row<PDPage> header = table.createRow(20);
                    cell = header.createCell(30, "Farmaco");
                    cell.setFont(HELVETICA_BOLD);
                    cell.setFontSize(text_size);
                    cell = header.createCell(20, "Data");
                    cell.setFont(HELVETICA_BOLD);
                    cell.setFontSize(text_size);
                    cell = header.createCell(25, "Medico");
                    cell.setFont(HELVETICA_BOLD);
                    cell.setFontSize(text_size);
                    cell = header.createCell(20, "Ticket");
                    cell.setFont(HELVETICA_BOLD);
                    cell.setFontSize(text_size);

                    table.addHeaderRow(header);



                    for (Ricetta ricetta : ricette) {
                            Row<PDPage> row = table.createRow(12);
                            row.createCell(ricetta.getFarmaco().getNome());
                            row.createCell(new SimpleDateFormat("dd-MM-yyyy  HH:mm").format(ricetta.getDataErogazione()));
                            row.createCell(ricetta.getMedico().getNome()+" "+ricetta.getMedico().getCognome());
                            row.createCell(Float.toString(ricetta.getFarmaco().getCosto())+" €");
                            somma = somma+ricetta.getFarmaco().getCosto();
                        
                    }
                    sommaTot= sommaTot+somma;
                    y = table.draw();

                    if (table.getCurrentPage() != page) {
                        contents.close();
                        page = table.getCurrentPage();
                        contents = new PDPageContentStream(doc, page, true, true);
                    }
                }
                y=y-20;
                PDStreamUtils.write(
                        contents,
                        "Costo tickets ricette: " + somma + " €",
                        PDType1Font.HELVETICA_BOLD,
                        14,
                        80,
                        y,
                        Color.BLACK); 



                if (y<50) {
                        contents.close();
                        page = new PDPage();
                        doc.addPage(page);
                        contents = new PDPageContentStream(doc, page, true, true);
                        y=800;
                    }

                y=y-50;
                PDStreamUtils.write(
                    contents,
                    "Costo tickets totale: "+sommaTot+" €",
                    PDType1Font.HELVETICA,
                    20,
                    300,
                    y,
                    Color.BLACK);
                }
            

            contents.close();
            doc.save(new File(pdfFolder, "user-" + userId + "-" + Calendar.getInstance().getTimeInMillis() + ".pdf"));

            response.setContentType("application/pdf");
            response.setHeader("Content-disposition", "attachment; filename=lista-ticket.pdf");
            doc.save(response.getOutputStream());
        }

        
//        String contextPath = getServletContext().getContextPath();
//        if (!contextPath.endsWith("/")) {
//            contextPath += "/";
//        }

        //response.sendRedirect(response.encodeRedirectURL(contextPath + "restricted/shopping.lists.html?id=" + userId));
    }    
}
