

import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;

public class TestPgfmtSetMPA {

    public static void main(String args[]) {
        PrinterJob job;

        job = PrinterJob.getPrinterJob();
        if (job.getPrintService() == null) {
            System.out.println("No printers. Test cannot continue");
            return;
        }

        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();

        
        PageFormat pf2 = job.getPageFormat(pras);
        System.out.println((pf2.getImageableX() / 72f) + " "
                + (pf2.getImageableY() / 72f) + " "
                + (pf2.getImageableWidth() / 72f) + " "
                + (pf2.getImageableHeight() / 72f)
        );

        
        pras.add(new MediaPrintableArea(2.0f,
                (float)(pf2.getImageableY() / 72f),
                (float) ((pf2.getImageableWidth() / 72f) - 1.0f),
                (float) (pf2.getImageableHeight() / 72f),
                MediaPrintableArea.INCH));

        pf2 = job.getPageFormat(pras);
        System.out.println((pf2.getImageableX() / 72f) + " "
                + (pf2.getImageableY() / 72f) + " "
                + (pf2.getImageableWidth() / 72f) + " "
                + (pf2.getImageableHeight() / 72f)
        );

        
        if (pf2.getImageableX() / 72f != 2.0f) {
            throw new RuntimeException("getPageFormat doesn't apply specified "
                    + "MediaPrintableArea attribute");
        }
    }
}
