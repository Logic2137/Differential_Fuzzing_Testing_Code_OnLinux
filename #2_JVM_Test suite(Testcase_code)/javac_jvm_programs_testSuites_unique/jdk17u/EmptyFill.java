import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.print.*;
import javax.print.*;
import javax.print.attribute.*;

public class EmptyFill implements Printable {

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }
        g.setColor(Color.black);
        int[] xq = { 75, 125, 75 };
        int[] yq = { 140, 140, 140 };
        g.fillPolygon(xq, yq, 3);
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
        pj.setPrintable(new EmptyFill());
        pj.print();
        String outStr = baos.toString("ISO-8859-1");
        if (outStr.indexOf("\nfill\n") > 0) {
            throw new Exception("Expected no fills");
        }
    }
}
