
/**
 * This creates the DA 6 Form PDF file for a specific roster and a specific month
 * @author othen, michael
 */
package dutyroster;

import com.itextpdf.text.Chunk;
import java.io.File;
import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RosterForm {
    

    private final ArrayList<Blockout> blockoutArray = new ArrayList();
    private final ArrayList<Status> statusArray = new ArrayList();
    
    private final ObservableList<ObservableList<StringProperty>> rowData = FXCollections.observableArrayList();
    private static final String DATE_FORMAT = "d MMM yy";
    private static final float WIDTH_1 = 1.5f;
    private static final float WIDTH_2 = 1.7f;
    private static final float WIDTH_3 = 1f;
    private static final float WIDTH_4 = 0.5f;
    private static final float WIDTH_5 = 0f;
    private final String rosterTitle;
    SimpleDateFormat milDate = new SimpleDateFormat(DATE_FORMAT);
    
    private final int cYear;
    private final int cMonth;
    private final String gTitle;
    private final String gRef;
    private final String gVer;
    private final String gNote;
    private final String gOrg;
    
    public RosterForm(ObservableList<ObservableList<StringProperty>> tempRows, ArrayList<Blockout> blocks, String roster, int year, int month){
        
        rowData.addAll(tempRows);
        blockoutArray.addAll(blocks);
        loadStatusData();
        cYear = year;
        cMonth = month;
        rosterTitle = roster;
        
        SettingsController settings = new SettingsController();
        //"fTitle","fRef","fVer","fNote"
        gTitle = settings.getSetting("fTitle");
        gRef = settings.getSetting("fRef");
        gVer = settings.getSetting("fVer");
        gNote = settings.getSetting("fNote");
        gOrg = settings.getSetting("fOrg");

    }
    
    
    public void makePDF() throws DocumentException, IOException{

         ArrayList<Blockout> remarks = new ArrayList<>();
        remarks = getRemarks();
        
        Collections.sort(remarks); 

        String rTitle = "Form_" + rosterTitle + "_" + cYear + "_" + cMonth; 
        ExportFile ex = new ExportFile(rTitle);
        File file = ex.getFilePath();
        
        if (file==null)
            return;
        
        Document document = new Document(PageSize.A4.rotate());
        
        System.out.println(file.getPath());
        
        PdfWriter.getInstance(document, new FileOutputStream(file.getPath()));
        
        int rowSize = rowData.size();        
        int pages = (int) Math.ceil( (double) rowSize / (double) 40 );
           
        document.setPageCount(pages);
        document.setMargins(50f, 50f, 20f, 10f);
        document.open();        
        
        for (int i = 0; i < pages; i++){
            
            if(i>0)
                document.newPage();
    
            PdfPTable table = newTable(i+1, pages);
            document.add( table); 
        }
        
        
        if(!remarks.isEmpty()){
           document.newPage(); 
         
           Paragraph remark;   
            Font font = new Font(FontFamily.HELVETICA,11);
            Font boldFont = new Font(FontFamily.HELVETICA,11,Font.UNDERLINE);
            remark = new Paragraph("Remarks:",boldFont);
            document.add(remark);
           for (Blockout b : remarks){
               String code = lookupStatusCode(b.getStatus());
               
               remark = new Paragraph(
                       "(" + b.getFromDate() + " - " + b.getToDate() + ") " 
                            + b.getName() + ": " + code + "-" + b.getStatus() + "; "
                            + b.getReason()
               ,font);     
               document.add(remark);
           }

        }
        document.close();            
    }
    
    public PdfPTable newTable(int page, int pages) throws DocumentException, IOException {

        Calendar selCal = Calendar.getInstance();
        selCal.set(cYear, (cMonth-1), 1);
        int lastDay = selCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        String strMonth = new SimpleDateFormat("MMMM").format(selCal.getTime());
        String startDate = milDate.format(selCal.getTime()); 
        selCal.set(cYear, (cMonth-1), lastDay);
        String endDate = milDate.format(selCal.getTime());
        
        
        
        PdfPTable table = new PdfPTable(43);
        table.setWidthPercentage(100);
        
        float[] columnWidths = new float[43];
       
        columnWidths[0] = 2.5f;
        columnWidths[1] = 5f;
        columnWidths[2] = 2f;
        
        for(int i = 3; i < 43; i++)
            columnWidths[i] = .8f;
        
        table.setWidths(columnWidths);
                
        Font smFont = new Font(FontFamily.HELVETICA,6);
        Font smBold = new Font(FontFamily.HELVETICA,6,Font.BOLD);
        Font mdFont = new Font(FontFamily.HELVETICA,8);
        Font mdBold = new Font(FontFamily.HELVETICA,8,Font.BOLD);
        Font lgBold = new Font(FontFamily.HELVETICA,10,Font.BOLD);
        PdfPCell cell;
        
        //Row 1
        cell = new PdfPCell(new Phrase("DUTY ROSTER",lgBold));
        cell.setBorderWidth(WIDTH_4);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(25f);
        cell.setBorderWidthTop(WIDTH_1);
        cell.setBorderWidthLeft(WIDTH_2);
        cell.setBorderWidthRight(WIDTH_4);
        cell.setBorderWidthBottom(WIDTH_5);
        cell.setColspan(2);
        cell.setRowspan(2);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("NATURE OF DUTY",mdFont));
        cell.setBorderWidth(WIDTH_4);
        cell.setFixedHeight(10f);
        cell.setColspan(12);
        cell.setBorderWidthTop(WIDTH_1);
        cell.setPaddingTop(1);
        cell.setBorder(Rectangle.TOP |Rectangle.RIGHT);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("ORGANIZATION",mdFont));
        cell.setBorderWidth(WIDTH_4);
        cell.setColspan(14);
        cell.setBorderWidthTop(WIDTH_1);
        cell.setBorder(Rectangle.TOP | Rectangle.RIGHT);
        cell.setPaddingTop(1);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("FROM (Date)",mdFont));
        cell.setBorderWidth(WIDTH_4);
        cell.setColspan(8);
        cell.setPaddingTop(1);
        cell.setBorderWidthTop(WIDTH_1);
        cell.setBorder(Rectangle.TOP | Rectangle.RIGHT);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("TO (Date)",mdFont));
        cell.setBorderWidth(WIDTH_4);
        cell.setColspan(7);
        cell.setPaddingTop(1);
        cell.setBorderWidthTop(WIDTH_1);
        cell.setBorderWidthRight(WIDTH_2);
        cell.setBorder(Rectangle.TOP | Rectangle.RIGHT);
        table.addCell(cell);
        
        //Row 2
        cell = new PdfPCell(new Phrase(rosterTitle,mdFont)); //Title
        cell.setBorderWidth(WIDTH_4);
        cell.setFixedHeight(15f);
        cell.setColspan(12);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidthRight(WIDTH_4);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(gOrg,mdFont));
        cell.setBorderWidth(WIDTH_4);
        cell.setColspan(14);
        cell.setBorder(Rectangle.RIGHT);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBorderWidthRight(WIDTH_4);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(startDate,mdFont));
        cell.setBorderWidth(WIDTH_4);
        cell.setColspan(8);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidthRight(WIDTH_4);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(endDate,mdFont));
        cell.setBorderWidth(WIDTH_4);
        cell.setColspan(7);
        cell.setBorder(Rectangle.RIGHT);
        cell.setBorderWidthRight(WIDTH_2);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
             
        //Row 3
        cell = new PdfPCell(new Phrase("GRADE",mdFont));
        cell.setBorderWidth(WIDTH_4);
        cell.setRowspan(2);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorderWidthLeft(WIDTH_2);
        cell.setBorderWidthTop(WIDTH_4);
        cell.setBorderWidthRight(0);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("NAME",mdFont));
        cell.setBorderWidth(WIDTH_4);
        cell.setColspan(1);
        cell.setRowspan(2);
        cell.setBorderWidthTop(WIDTH_4);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Month",mdFont));
        cell.setBorderWidth(WIDTH_4);
        cell.setColspan(1);
        cell.setRowspan(1);
        cell.setBorderWidthLeft(WIDTH_5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
       
        cell = new PdfPCell(new Phrase(strMonth, mdFont));
        cell.setBorderWidth(WIDTH_4);
        cell.setColspan(40);
        cell.setBorderWidthRight(WIDTH_2);
        table.addCell(cell);
        
        //Row 4
        cell = new PdfPCell(new Phrase("Day",mdFont));
        cell.setBorderWidth(WIDTH_4);
        cell.setColspan(1);
        cell.setRowspan(1);
        cell.setBorderWidthTop(WIDTH_5);
        cell.setBorderWidthLeft(WIDTH_5);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
            
        for(int i = 1; i < 41; i++){
            String day = (i <= lastDay)? Integer.toString(i) : "";
            cell = new PdfPCell(new Phrase(day,smFont));
            cell.setBorderWidth(WIDTH_4);
            cell.setBorderWidthTop(0);
            cell.setPadding(0);
            if(i<40)
                cell.setBorderWidthRight(0);
            else
            cell.setBorderWidthRight(WIDTH_2);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
            table.addCell(cell);
            
        }
        
        int startIndex = (40 * page) - 40;
        int endIndex = startIndex + 39;
        int leftOver = 40 - ((pages * 40) - rowData.size());       
                
        int hRow=0, pIndex=0;
        for(int i = startIndex; i <= endIndex; i++){
            
            if((page < pages) || pIndex < leftOver ){
                
                //Grade
                cell = new PdfPCell(new Phrase(rowData.get(i).get(0).get(),smFont));
                cell.setBorderWidth(WIDTH_4);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorderWidthLeft(WIDTH_2);
                cell.setBorderWidthTop(0);
                cell.setBorderWidthRight(0);
                cell.setFixedHeight(12f);
                
                 if(i==endIndex)
                    cell.setBorderWidthBottom(WIDTH_1);    
                 else if(hRow==3)
                    cell.setBorderWidthBottom(WIDTH_3);   
                
                table.addCell(cell);          

                //Name
                cell = new PdfPCell(new Phrase(rowData.get(i).get(1).get(),smFont));
                cell.setBorderWidth(WIDTH_4);
                cell.setColspan(2);
                cell.setBorderWidthTop(0);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT); 
                
                 if(i==endIndex)
                    cell.setBorderWidthBottom(WIDTH_1);    
                 else if(hRow==3)
                    cell.setBorderWidthBottom(WIDTH_3); 
                 
                 table.addCell(cell); 
            
                for(int j = 8; j < 48; j++){
           
                    if(j < rowData.get(i).size())
                        cell = new PdfPCell(new Phrase(rowData.get(i).get(j).get(),smFont));
                    else
                        cell = new PdfPCell(new Phrase("",smFont));
                    
                    cell.setPadding(0);
                    cell.setBorderWidthTop(0);
                    
                    if(j<47)
                        cell.setBorderWidthRight(0);
                    else
                        cell.setBorderWidthRight(WIDTH_2);
                    
                    if(i==endIndex)
                       cell.setBorderWidthBottom(WIDTH_1);    
                    else if(hRow==3)
                       cell.setBorderWidthBottom(WIDTH_3);   
                     
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
                    table.addCell(cell);
                }
            }
            else{
                
                 //Grade
                cell = new PdfPCell(new Phrase(" ",smFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
                cell.setBorderWidthLeft(WIDTH_2);
                cell.setBorderWidthTop(0);
                cell.setBorderWidthRight(0);
                cell.setFixedHeight(12f);
                
                 if(i==endIndex)
                    cell.setBorderWidthBottom(WIDTH_1);    
                 else if(hRow==3)
                    cell.setBorderWidthBottom(WIDTH_3); 
                
                table.addCell(cell);          

                //Name
                cell = new PdfPCell(new Phrase(" ",smFont));
                cell.setColspan(2);
                cell.setBorderWidthTop(0);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT); 
                
                 if(i==endIndex)
                    cell.setBorderWidthBottom(WIDTH_1);    
                 else if(hRow==3)
                    cell.setBorderWidthBottom(WIDTH_3);   
                 
                table.addCell(cell); 
                
               for(int j = 8; j < 48; j++){
           
                    cell = new PdfPCell(new Phrase(" ",smFont));
                    cell.setPadding(0);
                    cell.setBorderWidthTop(0);
                    if(j<47)
                        cell.setBorderWidthRight(0);
                    else
                        cell.setBorderWidthRight(WIDTH_2);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
                    if(i==endIndex)
                       cell.setBorderWidthBottom(WIDTH_1);    
                    else if(hRow==3)
                    cell.setBorderWidthBottom(WIDTH_3);    
                    table.addCell(cell);

                } 
                  
            }
              
            if(hRow==3)
                hRow=0;
            else
               hRow++;
           
            
            if(page==pages)
                pIndex++;
        }
        
        //Form ID
        cell = new PdfPCell(new Phrase(gTitle,mdBold));
        cell.setColspan(6);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT); 
        cell.setBorder(0);
        table.addCell(cell); 
        
        //Notice
        cell = new PdfPCell(new Phrase(gNote,mdFont));
        cell.setColspan(17);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT); 
        cell.setBorder(0);
        table.addCell(cell); 
        
        //Information
        cell = new PdfPCell(new Phrase(gRef,smBold));
        cell.setColspan(20);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT); 
        cell.setBorder(0);
        table.addCell(cell);      
        
         //Form Version
        cell = new PdfPCell(new Phrase(gVer,mdFont));
        cell.setColspan(43);
        cell.setVerticalAlignment(Element.ALIGN_TOP);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT); 
        cell.setBorder(0);
        table.addCell(cell);           
        return table; 

    }
    
    private ArrayList<Blockout> getRemarks(){
           
        ArrayList<Blockout> tmpBlocks = new ArrayList<>();
        
        Calendar monthStart = Calendar.getInstance();
        monthStart.set(cYear, (cMonth-1), 1);
        int lastDay = monthStart.getActualMaximum(Calendar.DAY_OF_MONTH);
        Calendar monthEnd = Calendar.getInstance();
        monthEnd.set(cYear, (cMonth-1), lastDay);
        
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.ENGLISH);
        Calendar startDate = Calendar.getInstance();
        Calendar endDate = Calendar.getInstance();    
        
        for (Blockout b : blockoutArray){  
            
            try {
                startDate.setTime( sdf.parse( b.getFromDate() ) );
                endDate.setTime( sdf.parse( b.getToDate() ) );
                endDate.add(Calendar.DATE, 1); 
            } catch (ParseException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
                
            if ( 
                    ( (startDate.before(monthStart) || startDate.equals(monthStart))
                    && (endDate.after(monthStart) || endDate.equals(monthStart)) )
                    ||
                    ( (startDate.before(monthEnd) || startDate.equals(monthEnd))
                    && (endDate.after(monthEnd) || endDate.equals(monthEnd)) )
                    ||
                    ( (startDate.after(monthStart) || startDate.equals(monthStart))
                    && (endDate.before(monthEnd) || endDate.equals(monthEnd)) )               
                    ) {
                tmpBlocks.add(b); 
            }
                
        }
        
        return tmpBlocks;   
    }
    
    private String lookupStatusCode(String status){
        
        for (Status s : statusArray)
            if (s.getTitle().equals(status))
                return s.getCode();
        
        return "";
    } 
    
    public void loadStatusData(){
        
          if(!statusArray.isEmpty()) 
        statusArray.clear();
        
        SecureFile sc = new SecureFile("Status");
        String a = sc.retrieve();
      
        String aArry[] = a.split("\\|", -1);
        for (String b : aArry){
            
            if (b.length() > 2){
                
                String bArry[] = b.split("\\@", -1);
                
                if(bArry[0].length() > 0 && bArry[1].length() > 0){
                    
                    boolean incs = ( bArry[2].equals("1") );
                    String sCode = Tools.replaceSpecialChars(bArry[0]);
                    String sTitle = Tools.replaceSpecialChars(bArry[1]);
                    
                    statusArray.add(  new Status(sCode, sTitle, incs)  );  
                }
            }
        }
    }

}   
