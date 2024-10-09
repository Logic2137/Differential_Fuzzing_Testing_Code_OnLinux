import java.io.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.print.*;
import javax.print.*;
import javax.print.attribute.*;

public class GlyphPositions implements Printable {

    static String testString = "0123456789";

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }
        g.setColor(Color.black);
        float x = (float) pf.getImageableX() + 20f, y = (float) pf.getImageableY() + 30f;
        Graphics2D g2 = (Graphics2D) g;
        Font font = new Font("SansSerif", Font.PLAIN, 20);
        FontRenderContext frc = g2.getFontRenderContext();
        GlyphVector v = font.createGlyphVector(frc, testString);
        for (int i = 0; i <= v.getNumGlyphs(); i++) {
            Point2D.Float p = new Point2D.Float();
            p.x = i * 40f;
            p.y = 0;
            v.setGlyphPosition(i, p);
        }
        g2.drawGlyphVector(v, x, y);
        return Printable.PAGE_EXISTS;
    }

    public static void main(String[] arg) throws Exception {
        DocFlavor psFlavor = new DocFlavor("application/postscript", "java.io.OutputStream");
        StreamPrintServiceFactory[] spfs = PrinterJob.lookupStreamPrintServices("application/postscript");
        if (spfs.length == 0) {
            return;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);
        StreamPrintService svc = spfs[0].getPrintService(baos);
        PrinterJob pj = PrinterJob.getPrinterJob();
        if (svc == null) {
            return;
        }
        pj.setPrintService(svc);
        pj.setPrintable(new GlyphPositions());
        pj.print();
        String outStr = baos.toString("ISO-8859-1");
        String ls = System.getProperty("line.separator");
        int indexCount = 0;
        int index = 0;
        while (index >= 0) {
            index = outStr.indexOf("20.0 12 F" + ls, index + 1);
            if (index > 0)
                indexCount++;
        }
        if (indexCount < testString.length()) {
            throw new Exception("Positions not used");
        }
    }
}
