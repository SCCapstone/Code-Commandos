/**
 * This creates a Roster Memo PDF file for a specific roster and a specific month
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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RosterMemo {
    
    
    private final ObservableList<ObservableList<StringProperty>> rowData = FXCollections.observableArrayList();
    private static final String DATE_FORMAT = "d MMM yy";
    private final String rosterTitle;
    SimpleDateFormat milDate = new SimpleDateFormat(DATE_FORMAT);
    
    private final int cYear;
    private final int cMonth;
    private final String gSymbol,gHead,gFirst,gConc,gSig;

    
    public RosterMemo(ObservableList<ObservableList<StringProperty>> tempRows, String roster, int year, int month){

        rowData.addAll(tempRows);
        cYear = year;
        cMonth = month;
        rosterTitle = roster;
        
        SettingsController settings = new SettingsController();
        //"fSymbol", "fHead","fFirst","fConc","fSig"
        gSymbol = settings.getSetting("fSymbol"); 
        gHead = settings.getSetting("fHead");
        gFirst = settings.getSetting("fFirst");
        gConc = settings.getSetting("fConc");
        gSig = settings.getSetting("fSig");
    }
    
    
    public void makePDF() throws DocumentException, IOException{

        String rTitle = "Memo_" + rosterTitle + "_" + cYear + "_" + cMonth; 
        ExportFile ex = new ExportFile(rTitle);
        File file = ex.getFilePath();
        Font font = new Font(FontFamily.HELVETICA,11);
        Font boldFont = new Font(FontFamily.HELVETICA,11,Font.BOLD);
        
        if (file==null)
            return;
              
        ///Today
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String now = milDate.format(c.getTime()); 
       
        //Roster Date
        Calendar rd = Calendar.getInstance();
        rd.set(cYear, (cMonth-1), 1);
        String rosterDate = new SimpleDateFormat("MMMM YYYY").format(rd.getTime());
        
        Document doc = new Document(PageSize.A4); //Portrait

        PdfWriter.getInstance(doc, new FileOutputStream(file.getPath()));
        
        doc.setMargins(50f, 50f, 50f, 50f);
        doc.open();        

        //Header
        String[] hLines = gHead.split("[\\r\\n]+");
        Paragraph header = new Paragraph();
        header.setAlignment(Element.ALIGN_CENTER);  
        Paragraph p;
        for(int i=0; i < hLines.length; i++){
            if(i==0)
                p = new Paragraph(hLines[i],boldFont);
            else
                p = new Paragraph(hLines[i],font);
            p.setAlignment(Element.ALIGN_CENTER);
            header.add(p);
        }
        doc.add(header);
        
        //Spacing 2
        for(int i = 0; i < 2; i++)
               doc.add( Chunk.NEWLINE );
        
        //Office Symbol and Date      
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.addCell(getCell(gSymbol, PdfPCell.ALIGN_LEFT));
        table.addCell(getCell(now, PdfPCell.ALIGN_RIGHT));
        doc.add(table);
       
        //Spacing 1
        doc.add( Chunk.NEWLINE );       
        
        //Subject
        p = new Paragraph("SUBJECT: " + rosterTitle + " Roster for " + rosterDate);
        doc.add(p);
        
        //Spacing 2
        for(int i = 0; i < 2; i++)
            doc.add( Chunk.NEWLINE );        
        
        //First Paragraph
        p = new Paragraph(gFirst);
        doc.add(p);       

        if(gFirst.length()>0)
               doc.add( Chunk.NEWLINE ); //Spacing 1   

        //Duty Roster Data goes here
            p = new Paragraph("Duty Roster Goes Here!");
            doc.add(p); 
        
        //Add space only if there is a first column
        if(gFirst.length()>0)
            doc.add( Chunk.NEWLINE ); //Spacing 1    
        
        //Conclusion
        p = new Paragraph(gConc);
        doc.add(p);       

        //Spacing 4
        for(int i = 0; i < 4; i++)
            doc.add( Chunk.NEWLINE );   
        
        //Signature Block  
        table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.addCell(getCell("", PdfPCell.ALIGN_LEFT));
        table.addCell(getCell(gSig, PdfPCell.ALIGN_LEFT));
        doc.add(table);
        
        //Close document
        doc.close();       
           
    }
    
    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
    
}


