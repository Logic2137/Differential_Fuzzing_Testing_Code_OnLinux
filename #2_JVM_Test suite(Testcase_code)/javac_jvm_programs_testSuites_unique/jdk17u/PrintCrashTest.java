import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.Destination;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.io.File;

public class PrintCrashTest {

    public static void main(String[] args) throws Exception {
        PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable((graphics, pageFormat, pageIndex) -> {
            if (pageIndex != 0) {
                return Printable.NO_SUCH_PAGE;
            } else {
                Shape shape = new Rectangle(110, 110, 10, 10);
                Rectangle rect = shape.getBounds();
                BufferedImage image = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().createCompatibleImage(rect.width, rect.height, Transparency.BITMASK);
                graphics.drawImage(image, rect.x, rect.y, rect.width, rect.height, null);
                return Printable.PAGE_EXISTS;
            }
        });
        File file = null;
        try {
            HashPrintRequestAttributeSet hashPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
            file = File.createTempFile("out", "ps");
            file.deleteOnExit();
            Destination destination = new Destination(file.toURI());
            hashPrintRequestAttributeSet.add(destination);
            printerJob.print(hashPrintRequestAttributeSet);
        } finally {
            if (file != null) {
                file.delete();
            }
        }
    }
}
