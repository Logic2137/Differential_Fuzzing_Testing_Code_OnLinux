



import java.awt.*;
import java.awt.print.*;


public class PrintBadImage implements Printable {

    public static void main(String args[]) {

      PrintBadImage pbi = new PrintBadImage();
      PrinterJob pj = PrinterJob.getPrinterJob();
      if (pj != null) {
          pj.setPrintable(pbi);
          try {
               pj.print();
         } catch (PrinterException pe) {
         } finally {
            System.err.println("PRINT RETURNED");
         }
      }
    }

    public int print(Graphics g, PageFormat pgFmt, int pgIndex) {
      if (pgIndex > 0)
         return Printable.NO_SUCH_PAGE;

      Graphics2D g2d = (Graphics2D)g;
      g2d.translate(pgFmt.getImageableX(), pgFmt.getImageableY());
      Image imgJava = Toolkit.getDefaultToolkit().getImage("img.bad");
      g2d.drawImage(imgJava, 0, 0, null);

      return Printable.PAGE_EXISTS;
    }

}
