




import java.awt.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.awt.print.*;
import java.io.*;

public class HeadlessPrintingTest {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "true");
        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(new Printable() {
            public int print(Graphics g, PageFormat pg, int pageIndex) {
                Graphics2D g2d = (Graphics2D)g;
                if (pageIndex > 2) {
                    return Printable.NO_SUCH_PAGE;
                } else {
                    g2d.translate(pg.getImageableX(), pg.getImageableY());
                    g2d.setColor(Color.RED);
                    g2d.drawString("page " + pageIndex, 100, 100);
                    return Printable.PAGE_EXISTS;
                }
            }
        });

        try {
            HashPrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
            File f = File.createTempFile("out", "ps");
            f.deleteOnExit();
            Destination dest = new Destination(f.toURI());
            attr.add(dest);
            pj.print(attr);
        } catch (Exception e) {
        }
    }
}
