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
import javax.print.attribute.standard.Destination;

public class PrintToFileTest implements Printable {

    public PrintToFileTest() {
    }

    public static void main(String[] args) throws Exception {
        PrinterJob pj = PrinterJob.getPrinterJob();
        if (pj.getPrintService() == null) {
            System.out.println("No printers installed. Skipping test.");
            return;
        }
        pj.setPrintable(new PrintToFileTest(), new PageFormat());
        PrintRequestAttributeSet pSet = new HashPrintRequestAttributeSet();
        File file = new File("./out.prn");
        pSet.add(new Destination(file.toURI()));
        pj.print(pSet);
        if (!file.exists()) {
            throw new RuntimeException("No file created");
        }
    }

    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
        if (pi > 0) {
            return Printable.NO_SUCH_PAGE;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        g2.translate(pf.getImageableX(), pf.getImageableY());
        g2.drawRect(1, 1, 200, 300);
        g2.drawRect(1, 1, 25, 25);
        return Printable.PAGE_EXISTS;
    }
}
