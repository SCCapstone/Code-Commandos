/**
 * This creates the DA 6 Form PDF file for a specific roster and a specific month
 * @author othen
 */
package dutyroster;


import java.io.FileOutputStream;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class PDF {
    
    public static final int LANDSCAPE = 90;

    private final ObservableList<ObservableList<StringProperty>> rowData = FXCollections.observableArrayList();
    private static final String DATE_FORMAT = "d MMM yy";
    private final String rosterTitle;
    SimpleDateFormat milDate = new SimpleDateFormat(DATE_FORMAT);
    
    private final int cYear;
    private final int cMonth;
 
    
    public PDF(ObservableList<ObservableList<StringProperty>> tempRows, String roster, int year, int month){

        rowData.addAll(tempRows);
        cYear = year;
        cMonth = month;
        rosterTitle = roster;
    }
    
    
    public void makePDF() throws DocumentException, IOException{

        String rTitle = "DA6_" + cYear + "_" + cMonth; 
       
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, new FileOutputStream(rTitle + ".pdf"));

        int rowSize = rowData.size();        
        int pages = (int) Math.ceil( (double) rowSize / (double) 40 );
           
        document.setPageCount(pages);
        document.setMargins(50f, 50f, 30f, 20f);
        document.open();        
        
        for (int i = 0; i < pages; i++){
            
            if(i>0)
                document.newPage();
    
            PdfPTable table = newTable(i+1, pages);
            document.add( table); 
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
        Font mdFont = new Font(FontFamily.HELVETICA,8);
        Font lgFont = new Font(FontFamily.HELVETICA,10,Font.BOLD);
        PdfPCell cell;
        
        //Row 1
        cell = new PdfPCell(new Phrase("DUTY ROSTER",lgFont));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(25f);
        cell.setColspan(2);
        cell.setRowspan(2);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("NATURE OF DUTY",mdFont));
        cell.setFixedHeight(10f);
        cell.setColspan(12);
        cell.setPaddingTop(1);
        cell.setBorder(Rectangle.TOP | Rectangle.RIGHT);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("ORGANIZATION",mdFont));
        cell.setColspan(14);
        cell.setBorder(Rectangle.TOP | Rectangle.RIGHT);
        cell.setPaddingTop(1);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("FROM (Date)",mdFont));
        cell.setColspan(8);
        cell.setPaddingTop(1);
        cell.setBorder(Rectangle.TOP | Rectangle.RIGHT);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("TO (Date)",mdFont));
        cell.setColspan(7);
        cell.setPaddingTop(1);
        cell.setBorder(Rectangle.TOP | Rectangle.RIGHT);
        table.addCell(cell);
        
        //Row 2
        cell = new PdfPCell(new Phrase(rosterTitle,mdFont)); //Title
        cell.setFixedHeight(15f);
        cell.setColspan(12);
        cell.setBorder(Rectangle.RIGHT);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(" ",mdFont));
        cell.setColspan(14);
        cell.setBorder(Rectangle.RIGHT);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(startDate,mdFont));
        cell.setColspan(8);
        cell.setBorder(Rectangle.RIGHT);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase(endDate,mdFont));
        cell.setColspan(7);
        cell.setBorder(Rectangle.RIGHT);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
             
        //Row 3
        cell = new PdfPCell(new Phrase("GRADE",mdFont));
        cell.setRowspan(2);
        
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("NAME",mdFont));
        cell.setRowspan(2);
        cell.setFixedHeight(20f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
        table.addCell(cell);
        
        cell = new PdfPCell(new Phrase("Month",mdFont));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setFixedHeight(10f);
        table.addCell(cell);
       
        cell = new PdfPCell(new Phrase(strMonth, mdFont));
        cell.setColspan(40);
        table.addCell(cell);
        
        //Row 4
        cell = new PdfPCell(new Phrase("Day",mdFont));
        cell.setFixedHeight(15f);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cell);
            
        for(int i = 1; i < 41; i++){
            String day = (i <= lastDay)? Integer.toString(i) : "";
            cell = new PdfPCell(new Phrase(day,smFont));
            cell.setPadding(0);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
            table.addCell(cell);
            
        }
        
        int startIndex = (40 * page) - 40;
        int endIndex = startIndex + 39;
        int leftOver = 40 - ((pages * 40) - rowData.size());       
                
         System.out.println();
         
        for(int i = startIndex; i < endIndex; i++){
       
            System.out.println(page + " < " + pages + " or  " + i + " < " + leftOver );
            
            if((page < pages) || i < leftOver ){
                
                
                //Grade
                cell = new PdfPCell(new Phrase(rowData.get(i).get(0).get(),smFont));
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
                cell.setFixedHeight(12f);
                table.addCell(cell);          

                //Name
                cell = new PdfPCell(new Phrase(rowData.get(i).get(1).get(),smFont));
                cell.setColspan(2);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT); 
                table.addCell(cell); 
            
                for(int j = 8; j < 48; j++){
           
                    if(j < rowData.get(i).size())
                        cell = new PdfPCell(new Phrase(rowData.get(i).get(j).get(),smFont));
                    else
                        cell = new PdfPCell(new Phrase("",smFont));
                    
                    cell.setPadding(0);
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
                cell.setFixedHeight(12f);
                table.addCell(cell);          

                //Name
                cell = new PdfPCell(new Phrase(" ",smFont));
                cell.setColspan(2);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setHorizontalAlignment(Element.ALIGN_LEFT); 
                table.addCell(cell); 
                
               for(int j = 8; j < 48; j++){
           
                    cell = new PdfPCell(new Phrase(" ",smFont));
                    cell.setPadding(0);
                    cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER); 
                    table.addCell(cell);
                } 
                  
            }
        
        }

        return table; 

    }

}   

