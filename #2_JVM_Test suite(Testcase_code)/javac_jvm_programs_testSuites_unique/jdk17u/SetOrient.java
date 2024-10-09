import java.awt.*;
import java.awt.geom.*;
import java.awt.print.*;
import java.applet.Applet;

public class SetOrient extends Applet implements Printable {

    PrinterJob pjob;

    public void init() {
        pjob = PrinterJob.getPrinterJob();
        Book book = new Book();
        PageFormat pf = pjob.defaultPage();
        pf.setOrientation(PageFormat.PORTRAIT);
        book.append(this, pf);
        pf = pjob.defaultPage();
        pf.setOrientation(PageFormat.LANDSCAPE);
        book.append(this, pf);
        pjob.setPageable(book);
        try {
            pjob.print();
        } catch (PrinterException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        Graphics2D g2d = (Graphics2D) g;
        drawGraphics(g2d, pf);
        return Printable.PAGE_EXISTS;
    }

    void drawGraphics(Graphics2D g, PageFormat pf) {
        double ix = pf.getImageableX();
        double iy = pf.getImageableY();
        double iw = pf.getImageableWidth();
        double ih = pf.getImageableHeight();
        g.setColor(Color.black);
        g.drawString(((pf.getOrientation() == PageFormat.PORTRAIT) ? "Portrait" : "Landscape"), (int) (ix + iw / 2), (int) (iy + ih / 2));
        g.draw(new Ellipse2D.Double(ix, iy, iw, ih));
    }
}
