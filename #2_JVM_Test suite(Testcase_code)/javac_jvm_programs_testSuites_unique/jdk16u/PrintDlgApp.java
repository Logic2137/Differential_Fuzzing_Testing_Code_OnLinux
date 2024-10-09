



import java.io.File;
import java.net.URI;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.awt.print.PrinterException;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.Destination;

public class PrintDlgApp implements Printable {

    public PrintDlgApp() {}

    public static void main(String[] args) {
         PrinterJob pj = PrinterJob.getPrinterJob();
         if (pj.getPrintService() == null) {
             System.out.println("No printers installed. Skipping test");
             return;
         }

         PrintDlgApp pd = new PrintDlgApp();
         PageFormat pf = new PageFormat();
         pj.setPrintable(pd, pf);

         PrintRequestAttributeSet pSet = new HashPrintRequestAttributeSet();
         pSet.add(new Copies(1));

         Destination dest = null;
         for (int i=0; i<2; i++) {
             File file = new File("./out"+i+".prn");
             dest = new Destination(file.toURI());
             pSet.add(dest);
             System.out.println("open PrintDialog.");
             if (pj.printDialog(pSet)) {
                 
                 dest = (Destination)pSet.get(Destination.class);
                 System.out.println("DEST="+dest);
                 if (dest != null) {
                     URI uri = dest.getURI();
                     file = new File(uri.getSchemeSpecificPart());
                     System.out.println("will be checking for file " + file);
                 }
                 try {
                     System.out.println("About to print the data ...");
                     pj.print(pSet);
                     System.out.println("Printed.");
                 }
                 catch (PrinterException pe) {
                     pe.printStackTrace();
                 }
             }
             if (dest != null && !file.exists()) {
                 throw new RuntimeException("No file created");
             }
         }
    }

    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {

        if (pi > 0) {
            return Printable.NO_SUCH_PAGE;
        }
        
        Graphics2D g2 = (Graphics2D)g;
        g2.setColor(Color.black);
        g2.translate(pf.getImageableX(), pf.getImageableY());
        g2.drawRect(1,1,200,300);
        g2.drawRect(1,1,25,25);
        System.out.println("print method called "+pi);
        return Printable.PAGE_EXISTS;
    }
}
