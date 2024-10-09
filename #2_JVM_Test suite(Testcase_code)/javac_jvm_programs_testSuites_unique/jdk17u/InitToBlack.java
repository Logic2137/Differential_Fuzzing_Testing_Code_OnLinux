import java.awt.*;
import java.awt.print.*;
import java.applet.Applet;

public class InitToBlack extends Applet implements Printable {

    public void init() {
        PrinterJob pjob = PrinterJob.getPrinterJob();
        Book book = new Book();
        book.append(this, pjob.defaultPage());
        pjob.setPageable(book);
        try {
            pjob.print();
        } catch (PrinterException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        g.drawString("Test Passes", 200, 200);
        return PAGE_EXISTS;
    }

    public static void main(String[] args) {
        new InitToBlack().init();
        System.exit(0);
    }
}
