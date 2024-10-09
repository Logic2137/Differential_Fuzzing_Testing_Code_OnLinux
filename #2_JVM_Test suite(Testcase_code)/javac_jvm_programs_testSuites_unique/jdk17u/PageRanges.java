import java.awt.*;
import java.awt.print.*;

public class PageRanges implements Printable {

    static String[] instr = { "This test prints two jobs, and tests that the specified range", "of pages is printed. You must have a printer installed for this test.", "In the first dialog, select a page range of 2 to 3, and press OK", "In the second dialog, select ALL, to print all pages (in total 5 pages).", "Collect the two print outs and confirm the jobs printed correctly" };

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < instr.length; i++) {
            System.out.println(instr[i]);
        }
        PrinterJob job = PrinterJob.getPrinterJob();
        if (job.getPrintService() == null) {
            System.out.println("No printers. Test cannot continue.");
            return;
        }
        job.setPrintable(new PageRanges());
        if (!job.printDialog()) {
            return;
        }
        job.print();
        if (!job.printDialog()) {
            return;
        }
        job.print();
        return;
    }

    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
        if (pi >= 5) {
            return NO_SUCH_PAGE;
        }
        g.drawString("Page : " + (pi + 1), 200, 200);
        return PAGE_EXISTS;
    }
}
