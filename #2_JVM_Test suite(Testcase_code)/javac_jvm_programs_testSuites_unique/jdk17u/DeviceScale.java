import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

public class DeviceScale implements Printable {

    boolean firstTime = true;

    double sx, sy;

    Shape clip, firstClip;

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        Graphics2D g2 = (Graphics2D) g;
        if (pageIndex >= 1) {
            return Printable.NO_SUCH_PAGE;
        }
        AffineTransform at = g2.getTransform();
        System.out.println(at);
        clip = g2.getClip();
        System.out.println(clip);
        if (firstTime) {
            firstTime = false;
            sx = Math.abs(at.getScaleX());
            sy = Math.abs(at.getScaleY());
            firstClip = clip;
        } else {
            double newSx = Math.abs(at.getScaleX());
            double newSy = Math.abs(at.getScaleY());
            if (Math.abs(sx - newSx) > 0.1 || Math.abs(sy - newSy) > 0.1) {
                throw new RuntimeException("different scale, was " + sx + "," + sy + " now " + newSx + "," + newSy);
            }
            if (!clip.equals(firstClip)) {
                throw new RuntimeException("different clip, was " + firstClip + " now " + clip);
            }
        }
        return Printable.PAGE_EXISTS;
    }

    public static void doit(OrientationRequested o) throws Exception {
        PrinterJob pj = PrinterJob.getPrinterJob();
        if (pj.getPrintService() == null) {
            System.out.println("No print service found.");
            return;
        }
        pj.setPrintable(new DeviceScale());
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(o);
        String fileName = "out.prn";
        File f = new File(fileName);
        f.deleteOnExit();
        URI dest = f.toURI();
        aset.add(new Destination(dest));
        pj.print(aset);
    }

    public static void main(String[] arg) throws Exception {
        doit(OrientationRequested.PORTRAIT);
        doit(OrientationRequested.LANDSCAPE);
        doit(OrientationRequested.REVERSE_LANDSCAPE);
    }
}
