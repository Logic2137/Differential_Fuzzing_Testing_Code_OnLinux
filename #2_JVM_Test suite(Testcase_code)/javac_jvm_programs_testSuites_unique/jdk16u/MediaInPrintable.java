


import java.awt.*;
import java.awt.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;

public class MediaInPrintable implements Printable {
        private static Font fnt = new Font("Helvetica",Font.PLAIN,24);
        public static void main(String[] args) {

            System.out.println("arguments : native1 | native2\nExpected output :\n\tnative1 -  Landscape orientation.\n\tnative2 - Legal paper is selected.");
            if (args.length == 0) {
                return;
            }


            
            PrinterJob job = PrinterJob.getPrinterJob();
            PageFormat pf = new PageFormat();

            if (args[0].equals("native1")) {
                pf.setOrientation(PageFormat.LANDSCAPE);
                job.setPrintable(new MediaInPrintable(), pf);
                if (job.printDialog()) {
                        
                        try {
                                job.print();
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }
            } else if (args[0].equals("native2")) {
                Paper p = new Paper();
                p.setSize(612.0, 1008.0);
                p.setImageableArea(72.0, 72.0, 468.0, 864.0);
                pf.setPaper(p);

                job.setPrintable(new MediaInPrintable(), pf);
                if (job.printDialog()) {
                        
                        try {
                                job.print();
                        } catch (Exception e) {
                                e.printStackTrace();
                        }
                }

            }

                
        }

        public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
                return Printable.NO_SUCH_PAGE;
        }
        g.setFont(fnt);
        g.setColor(Color.green);
        g.drawString("Page " + (pageIndex+1), 100, 100);
        return Printable.PAGE_EXISTS;
        }
}
