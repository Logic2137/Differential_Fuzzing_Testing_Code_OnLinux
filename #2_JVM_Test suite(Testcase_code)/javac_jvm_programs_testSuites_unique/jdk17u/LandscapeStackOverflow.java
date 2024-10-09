import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.OrientationRequested;

public class LandscapeStackOverflow {

    public static final void main(String[] parameters) {
        PrinterJob printjob = PrinterJob.getPrinterJob();
        printjob.setJobName("Test Print Job");
        PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
        attributes.add(OrientationRequested.LANDSCAPE);
        try {
            printjob.setPrintable(new Painter());
            printjob.print(attributes);
        } catch (PrinterException exception) {
            exception.printStackTrace();
        }
    }
}

class Painter implements Printable {

    public int print(Graphics graphics, PageFormat format, int index) {
        Graphics2D g2d = (Graphics2D) graphics;
        double scalex = g2d.getTransform().getScaleX();
        double scaley = g2d.getTransform().getScaleY();
        double centerx = (format.getImageableX() + (format.getImageableWidth() / 2)) * scalex;
        double centery = (format.getImageableY() + (format.getImageableHeight() / 2)) * scaley;
        g2d.scale(1 / scalex, 1 / scaley);
        g2d.translate(centerx, centery);
        Path2D.Double path = new Path2D.Double();
        path.moveTo(-scalex * 72, -scaley * 72);
        path.lineTo(-scalex * 72, scaley * 72);
        path.lineTo(scalex * 72, scaley * 72);
        path.lineTo(scalex * 72, -scaley * 72);
        path.closePath();
        g2d.draw(path);
        return index == 0 ? PAGE_EXISTS : NO_SUCH_PAGE;
    }
}
