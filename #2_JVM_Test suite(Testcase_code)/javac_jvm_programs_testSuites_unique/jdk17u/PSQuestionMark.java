import java.io.*;
import java.awt.*;
import java.awt.print.*;
import javax.print.*;
import javax.print.attribute.*;

public class PSQuestionMark implements Printable {

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }
        g.setColor(Color.black);
        g.setFont(new Font("Serif", Font.PLAIN, 12));
        g.drawString("?", 100, 150);
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
        pj.setPrintable(new PSQuestionMark());
        pj.print();
        String outStr = baos.toString("ISO-8859-1");
        String ls = System.getProperty("line.separator");
        if (outStr.indexOf("12.0 32 F" + ls + "<3f>") < 0) {
            throw new Exception("PS font not used");
        }
    }
}
