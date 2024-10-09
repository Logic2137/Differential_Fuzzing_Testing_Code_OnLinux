


import java.awt.print.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;

public class PageFormatFromAttributes {

    public static void main(String args[]) {
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintService service = job.getPrintService();
        if (service == null) {
            return; 
        }
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        test(job, MediaSizeName.ISO_A4, OrientationRequested.PORTRAIT);
        test(job, MediaSizeName.ISO_A4, OrientationRequested.LANDSCAPE);
        test(job, MediaSizeName.ISO_A4,
             OrientationRequested.REVERSE_LANDSCAPE);
        test(job, MediaSizeName.ISO_A3, OrientationRequested.PORTRAIT);
        test(job, MediaSizeName.NA_LETTER, OrientationRequested.PORTRAIT);
        test(job, MediaSizeName.NA_LETTER, OrientationRequested.LANDSCAPE);
        test(job, MediaSizeName.NA_LEGAL, OrientationRequested.PORTRAIT);
    }

    static void test(PrinterJob job,
                     MediaSizeName media, OrientationRequested orient) {

        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(media);
        aset.add(orient);

        PrintService service = job.getPrintService();
        if (!service.isAttributeValueSupported(media, null, aset) ||
            !service.isAttributeValueSupported(orient, null, aset)) {
            return; 
        }
        PageFormat pf = job.getPageFormat(aset);
        boolean ok = true;
        switch (pf.getOrientation()) {
        case PageFormat.PORTRAIT :
            ok = orient == OrientationRequested.PORTRAIT;
            break;
        case PageFormat.LANDSCAPE :
            ok = orient == OrientationRequested.LANDSCAPE;
            break;
        case PageFormat.REVERSE_LANDSCAPE :
            ok = orient == OrientationRequested.REVERSE_LANDSCAPE;
            break;
        }
        if (!ok) {
            throw new RuntimeException("orientation not as specified");
        }
        MediaSize mediaSize = MediaSize.getMediaSizeForName(media);
        if (mediaSize == null) {
            throw new RuntimeException("expected a media size");
        }
        double units = Size2DSyntax.INCH/72.0;
        double w = mediaSize.getX(1) / units;
        double h = mediaSize.getY(1) / units;
        Paper paper = pf.getPaper();
        double pw = paper.getWidth();
        double ph = paper.getHeight();
        if (Math.round(pw) != Math.round(w) ||
            Math.round(ph) != Math.round(h)) {
            throw new RuntimeException("size not as specified");
        }
    }
}
