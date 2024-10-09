import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.*;
import java.awt.*;
import java.awt.print.*;

public class PageRangesDlgTest implements Printable {

    static String[] instr = { "This test is to check that the print dialog displays the specified", "page ranges. You must have a printer installed for this test.", "It is valid only on dialogs which support page ranges", "In each dialog, check that a page range of 2 to 3 is requested", "Optionally press Print instead of Cancel, and verify that the", "correct number/set of pages is printed" };

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < instr.length; i++) {
            System.out.println(instr[i]);
        }
        PrinterJob job = PrinterJob.getPrinterJob();
        if (job.getPrintService() == null) {
            System.out.println("No printers. Test cannot continue.");
            return;
        }
        job.setPrintable(new PageRangesDlgTest());
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(new PageRanges(2, 3));
        if (job.printDialog(aset)) {
            job.print(aset);
        }
        job = PrinterJob.getPrinterJob();
        job.setPrintable(new PageRangesDlgTest());
        aset.add(DialogTypeSelection.NATIVE);
        if (job.printDialog()) {
            job.print();
        }
    }

    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
        System.out.println("pi=" + pi);
        if (pi >= 5) {
            return NO_SUCH_PAGE;
        }
        g.drawString("Page : " + (pi + 1), 200, 200);
        return PAGE_EXISTS;
    }
}
