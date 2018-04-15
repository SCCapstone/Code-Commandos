package dutyroster;



import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class MemoHeader extends PdfPageEventHelper {
 
   
    protected PdfPTable table;
    protected float tableHeight;
    
    public MemoHeader(String oSym, String oSubject) {
        
        
        oSym = (oSym.isEmpty()) ? "missing" : oSym;
        oSubject = (oSubject.isEmpty()) ? "missing subject" : oSubject;
        
        table = new PdfPTable(1);
        table.setTotalWidth(500);
        table.setLockedWidth(true);
        PdfPCell cell;
        cell = new PdfPCell(new Phrase(oSym));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT); 
        cell.setBorder(0);
        table.addCell(cell);           

        cell = new PdfPCell(new Phrase(oSubject));
        cell.setBorder(0);
        table.addCell(cell); 


        tableHeight = table.getTotalHeight();
        
    }
    

 
    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        
        if(document.getPageNumber()==1)
            return;

        table.writeSelectedRows(0, -1,
                    document.left(),
                    document.top() + ((document.topMargin() + tableHeight) / 2) - 30,
                    writer.getDirectContent());
    }

}