/**
 * This creates a Roster Memo PDF file for a specific roster and a specific month
 * @author othen, Austin
 * @version 4, 14 April, 2018
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
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RosterMemo {
    
    private final ObservableList<ObservableList<StringProperty>> rowData = FXCollections.observableArrayList();
    private final Roster roster;
     
    private final int cYear;
    private final int cMonth;
    private final String gSymbol,gHead,gFirst,gConc,gSig;

    
    public RosterMemo(ObservableList<ObservableList<StringProperty>> tempRows, Roster roster, int year, int month){

        rowData.addAll(tempRows);
        cYear = year;
        cMonth = month;
        this.roster = roster;
        
        SettingsController settings = new SettingsController();
        //"fSymbol", "fHead","fFirst","fConc","fSig"
        gSymbol = settings.getSetting("fSymbol"); 
        gHead = settings.getSetting("fHead");
        gFirst = settings.getSetting("fFirst");
        gConc = settings.getSetting("fConc");
        gSig = settings.getSetting("fSig");
    }
    
    
    public void makePDF() throws DocumentException, IOException{

        String rTitle = "Memo_" + roster.getTitle() + "_" + cYear + "_" + cMonth; 
        ExportFile ex = new ExportFile(rTitle);
        File file = ex.getFilePath();
        
        if (file==null)
            return;
              
        Calendar c = Calendar.getInstance();     
        Font font = new Font(FontFamily.HELVETICA,11);
        Font boldFont = new Font(FontFamily.HELVETICA,11,Font.BOLD);
        SimpleDateFormat mTime = new SimpleDateFormat("kkmm");
        SimpleDateFormat mDate = new SimpleDateFormat("EEE, MMM d");
    
        //Today
        c.setTime(new Date());
        String now = new SimpleDateFormat("d MMM YY").format(c.getTime()).toUpperCase(); 
        //Roster Date
        c.set(cYear, (cMonth-1), 1);
        int lastDay = c.getActualMaximum(Calendar.DAY_OF_MONTH);
        
        //Generate a proper  list for memo output
        ArrayList<ArrayList<String>> dutyList =  makeDutyList(lastDay);

        String rosterDate = new SimpleDateFormat("MMMM, YYYY").format(c.getTime());
        String subject = "SUBJECT: " + roster.getTitle() + " Roster for " + rosterDate;
             
        Document doc = new Document(PageSize.A4); //Portrait
         doc.setMargins(80f, 80f, 80f, 80f);
        PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(file.getPath()));
        MemoHeader event = new MemoHeader(gSymbol,subject);
        writer.setPageEvent(event);
        
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
        doc.setMargins(80f, 80f, 120f, 80f); 
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
        p = new Paragraph(subject);
        doc.add(p);
        
        //Spacing 2
        for(int i = 0; i < 2; i++)
            doc.add( Chunk.NEWLINE );        
        
        //First Paragraph
        p = new Paragraph(gFirst);
        doc.add(p);       

        if(gFirst.length()>0)
            doc.add( Chunk.NEWLINE ); //Spacing 1   

        String date;
        c.set(cYear, (cMonth-1), 1);
        BaseFont bf = BaseFont.createFont();
        float indent = bf.getWidthPoint("   ", 12);
        int rInterval = roster.getDInterval();
        int rAmount = roster.getAmount();
        String sHour, eHour;

        //Duty Roster Data goes here
        for (int day = 1; day <= lastDay; day++){
        
            date = mDate.format(c.getTime()).toUpperCase();
            Paragraph pDate; 
            pDate = new Paragraph(date);
            pDate.setIndentationLeft(indent);
            doc.add(pDate); 
            
            if(rInterval < 24){
                
                int shift = 0;
                for(int hour = 0; hour < 24; hour=hour+rInterval){
                    
                    Calendar h = Calendar.getInstance();
                    h.set(Calendar.HOUR_OF_DAY, hour);
                    h.set(Calendar.MINUTE, 0);
                    sHour = mTime.format(h.getTime());
                    sHour = (hour == 0) ? "0000" : sHour;
                    h.set(Calendar.HOUR_OF_DAY, hour+rInterval);
                    eHour = mTime.format(h.getTime());
                    Paragraph pHour;
                    pHour = new Paragraph(sHour + " - " + eHour);
                    pHour.setIndentationLeft(indent*8);
                    doc.add(pHour);
                    
                    for (int d = 0; d < rAmount; d++){

                        String person = dutyList.get(day-1).get(d+shift);
                        p = new Paragraph(person);
                        p.setIndentationLeft(indent*16);
                        doc.add(p);
                    }
                    shift += rAmount;            
                }
            }
            else{
                for (int d = 0; d < rAmount; d++){
                    String person = dutyList.get(day-1).get(d);
                    p = new Paragraph(person);
                    p.setIndentationLeft(indent*8);
                    doc.add(p);
                }   
            }
            c.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        p.setIndentationLeft(0);
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
    
    private ArrayList<ArrayList<String>> makeDutyList(int lastDay){
        
        ArrayList<ArrayList<String>> dayList = new ArrayList<>();
        
        //rowData.get(i).get(1)
        for (int day = 0; day < lastDay; day++ ){
            ArrayList<String> dList = new ArrayList<>();
            
            for(ObservableList<StringProperty> sp :rowData)
                if (sp.get(day+8).get().equals("///"))
                    dList.add(sp.get(1).get());
                
            dayList.add(dList);
            
        }
 
        return dayList;
    }
    
    
 
}


