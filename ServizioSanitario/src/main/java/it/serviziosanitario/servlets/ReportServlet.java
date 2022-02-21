/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.serviziosanitario.servlets;

import it.serviziosanitario.persistence.dao.SspDAO;
import it.serviziosanitario.persistence.entities.Ricetta;
import it.serviziosanitario.persistence.entities.Ssp;
import it.serviziosanitario.persistence.entities.Utente;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOException;
import it.unitn.disi.wp.commons.persistence.dao.exceptions.DAOFactoryException;
import it.unitn.disi.wp.commons.persistence.dao.factories.DAOFactory;
import java.io.IOException;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;

/**
 *
 * @author loghi
 */
public class ReportServlet extends HttpServlet {
    private SspDAO sspDAO; 
    
    @Override
    public void init() throws ServletException {
        DAOFactory daoFactory = (DAOFactory) super.getServletContext().getAttribute("daoFactory");
        if (daoFactory == null) {
            throw new ServletException("Impossible to get dao factory");
        }
        try {
            sspDAO = daoFactory.getDAO(SspDAO.class);
        } catch (DAOFactoryException ex) {
            throw new ServletException("Impossible to get dao factory for ssp", ex);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = ((HttpServletRequest) request).getSession(false);
        Utente utente = (Utente) session.getAttribute("utente");
        Integer userId = utente.getUtenteId();
        
        Ssp ssp = null;
        List<Ricetta> lista = null;
        try {
            ssp = sspDAO.getByPrimaryKey(userId);
            lista=sspDAO.getAllRicetteByIdProvincia(ssp.getProvincia().getProvinciaId());
        } catch (DAOException ex) {
            Logger.getLogger(ReportServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        //inizializzo workbook e sheet
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("Ricette");
        
       
        
        //TITOLO
        sheet.addMergedRegion(new CellRangeAddress(0,0,0,6));
        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        headerCell.setCellValue("LISTA RICETTE NELLA PROVINCIA DI "+(ssp.getProvincia().getNome()).toUpperCase());
        
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        
        HSSFFont headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.RED.getIndex());
        
        headerStyle.setFont(headerFont);
        headerCell.setCellStyle(headerStyle);
        
        sheet.setDefaultColumnWidth(18);
        
        if(!lista.isEmpty()){
        
            //CONTENUTO

            CellStyle contentCellStyle = workbook.createCellStyle();
            contentCellStyle.setAlignment(HorizontalAlignment.CENTER);
            
            printHeader(workbook, sheet, 2);
            int rowNumber = 3;
            
            
            Date last_date = new Date(lista.get(0).getDataErogazione().getTime());
            float ticketForNow = 0;
            
            for (Ricetta ricetta : lista) {
                
                Date current_date = new Date(ricetta.getDataErogazione().getTime());
                //se trovo un nuovo giorno stampo il totale del precedente e una nuova riga con i titoli delle colonne
                if(0 != (compareDays(current_date, last_date))){
                    rowNumber++;
                    sheet.addMergedRegion(new CellRangeAddress(rowNumber,rowNumber,0,1));
                    Row tmp = sheet.createRow(rowNumber);
                    Cell totTicket = tmp.createCell(0);
                    totTicket.setCellValue("Totale ticket nel giorno "+ new SimpleDateFormat("dd-MM-yyyy").format(last_date)+
                            ": "+ticketForNow+" €");
                    //stampa totale
                    CellStyle totStyle = workbook.createCellStyle();
                    totStyle.setAlignment(HorizontalAlignment.CENTER);
                    HSSFFont totFont = workbook.createFont();
                    totFont.setColor(IndexedColors.ORANGE.getIndex());
                    totStyle.setFont(headerFont);
                    totTicket.setCellStyle(totStyle);
                    
                    
                    rowNumber = rowNumber + 3;
                    ticketForNow  = 0;
                    printHeader(workbook, sheet, rowNumber);
                    rowNumber++;
                }
                
                
                Row contentRow = sheet.createRow(rowNumber);
                last_date = current_date;
                
                String giorno;
                String ora;
                giorno = new SimpleDateFormat("dd-MM-yyyy").format(ricetta.getDataErogazione());
                ora = new SimpleDateFormat("HH:mm").format(ricetta.getDataErogazione());
                Cell contentCells[] = new Cell[9];
                for(int i=0; i<9;i++){
                    contentCells[i] = contentRow.createCell(i);
                    contentCells[i].setCellStyle(contentCellStyle);
                }
                contentCells[0].setCellValue(giorno);
                contentCells[1].setCellValue(ora);
                contentCells[2].setCellValue(ricetta.getFarmaco().getNome());
                contentCells[3].setCellValue(ricetta.getMedico().getNome()+" "+ricetta.getMedico().getCognome());
                contentCells[4].setCellValue(ricetta.getPaziente().getNome()+" "+ricetta.getPaziente().getCognome());
                contentCells[5].setCellValue(ricetta.getFarmaco().getCosto()+" €");
                
                ticketForNow = ticketForNow + ricetta.getFarmaco().getCosto();          
                rowNumber++;
            }
            //finito le ricette, stampo il totale dell'ultimo giorno
            rowNumber++;
            sheet.addMergedRegion(new CellRangeAddress(rowNumber,rowNumber,0,1));
            Row tmp = sheet.createRow(rowNumber);
            Cell totTicket = tmp.createCell(0);
            totTicket.setCellValue("Totale ticket nel giorno "+ new SimpleDateFormat("dd-MM-yyyy").format(last_date)+
                    ": "+ticketForNow+" €");
            CellStyle totStyle = workbook.createCellStyle();
            totStyle.setAlignment(HorizontalAlignment.CENTER);
            HSSFFont totFont = workbook.createFont();
            totFont.setColor(IndexedColors.ORANGE.getIndex());
            totStyle.setFont(headerFont);
            totTicket.setCellStyle(totStyle);
            
        }else{
            //caso in cui non ci sono ricette erogate
            Row emptyCase = sheet.createRow(2);
            sheet.addMergedRegion(new CellRangeAddress(2,2,0,3));
            Cell emptyCellCase = emptyCase.createCell(0);
            emptyCellCase.setCellStyle(headerStyle);
            emptyCellCase.setCellValue("AL MOMENTO NON CI SONO RICETTE EROGATE IN QUESTA PROVINCIA");
        }

//        System.out.println("Done");

        String filename = "attachment; filename=report_"+ Calendar.getInstance().getTimeInMillis() + ".xls";
        response.setHeader("Content-disposition", filename);
        try{
            workbook.write(response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        //chiudo il workbook
        workbook.close();
 
    }

    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

   
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    /**
     * Funziona ad hoc per comparare due giorni ignorado l'ora
     * @param d1
     * @param d2
     * @return 0 se sono uguali, minore di 0 se d2 è maggiore, maggiore di 0 se d1 è maggiore
     */
    public int compareDays(Date d1, Date d2) {
        if (d1.getYear() != d2.getYear()) 
            return d1.getYear() - d2.getYear();
        if (d1.getMonth() != d2.getMonth()) 
            return d1.getMonth() - d2.getMonth();
        return d1.getDate() - d2.getDate();
    }
    
    /**
     * funzione per stampare un header prima di ogni giorno
     * @param workbook
     * @param sheet
     * @param row 
     */
    public void printHeader(HSSFWorkbook workbook, HSSFSheet sheet, int row){
       Row nameRow = sheet.createRow(row);
            Cell nameCells[] = new Cell[9];
            

            CellStyle nameCellStyle = workbook.createCellStyle();
            nameCellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            nameCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            nameCellStyle.setAlignment(HorizontalAlignment.CENTER);

            HSSFFont nameCellFont = workbook.createFont();
            nameCellFont.setColor(IndexedColors.WHITE.getIndex());
            nameCellStyle.setFont(nameCellFont);

            for(int i=0; i<6;i++){
                nameCells[i] = nameRow.createCell(i);
                nameCells[i].setCellStyle(nameCellStyle);
            }
            nameCells[0].setCellValue("GIORNO");
            nameCells[1].setCellValue("ORA");
            nameCells[2].setCellValue("FARMACO");
            nameCells[3].setCellValue("MEDICO");
            nameCells[4].setCellValue("PAZIENTE");
            nameCells[5].setCellValue("TICKET");
    }

}
