

import javax.print.PrintService;
import javax.print.attribute.PrintServiceAttributeSet;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.print.*;



public class HeadlessPrinterJob {

    class testPrintable implements Printable {

        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
            Graphics2D g2 = (Graphics2D) graphics;

            if (pageIndex >= 10) {
                return Printable.NO_SUCH_PAGE;
            }

            int gridWidth = 400 / 6;
            int gridHeight = 300 / 2;

            int rowspacing = 5;
            int columnspacing = 7;
            int rectWidth = gridWidth - columnspacing;
            int rectHeight = gridHeight - rowspacing;

            Color fg3D = Color.lightGray;

            g2.setPaint(fg3D);
            g2.drawRect(80, 80, 400 - 1, 310);
            g2.setPaint(Color.black);

            int x = 85;
            int y = 87;


            
            g2.draw(new Line2D.Double(x, y + rectHeight - 1, x + rectWidth, y));
            x += gridWidth;

            
            
            g2.draw(new Rectangle2D.Double(x, y, rectWidth, rectHeight));
            x += gridWidth;

            
            
            g2.draw(new RoundRectangle2D.Double(x, y, rectWidth,
                    rectHeight, 10, 10));
            return Printable.PAGE_EXISTS;
        }
    }

    class testPageable implements Pageable {

        public int getNumberOfPages() {
            return 10;
        }

        public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
            PageFormat pf = null;
            if (pageIndex >= 10) {
                throw new IndexOutOfBoundsException("Wrong page#");
            }
            switch (pageIndex) {
                case 0:
                case 2:
                case 4:
                case 6:
                case 8:
                    pf = new PageFormat();
                    pf.setOrientation(PageFormat.REVERSE_LANDSCAPE);
                    break;
                case 1:
                case 3:
                case 5:
                case 7:
                case 9:
                    pf = new PageFormat();
                    pf.setOrientation(PageFormat.LANDSCAPE);
                    break;
            }
            return pf;
        }

        public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
            if (pageIndex >= 10) {
                throw new IndexOutOfBoundsException("Wrong page#");
            }
            return new testPrintable();
        }
    }

    public static void main(String args[]) throws Exception {
        new HeadlessPrinterJob().doTest();
    }

    void doTest() throws Exception {
        PrinterJob pj = PrinterJob.getPrinterJob();
        for (PrintService psl : pj.lookupPrintServices()) {
            PrintServiceAttributeSet psas = psl.getAttributes();
            pj.setPrintService(psl);
        }
        PrintService ps = pj.getPrintService();
        pj.setPrintable(new testPrintable());

        pj = PrinterJob.getPrinterJob();
        PageFormat pf = new PageFormat();
        pf.setOrientation(PageFormat.REVERSE_LANDSCAPE);
        pj.setPrintable(new testPrintable(), pf);
        pj.setPageable(new testPageable());

        boolean exceptions = false;
        try {
            pj.printDialog();
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");

        exceptions = false;
        try {
            pj = PrinterJob.getPrinterJob();
            pf = new PageFormat();
            pf.setOrientation(PageFormat.REVERSE_LANDSCAPE);
            pf = pj.pageDialog(pf);
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");

        pf = new PageFormat();
        pf.setOrientation(PageFormat.REVERSE_LANDSCAPE);
        pf = pj.defaultPage(pf);
        pf = pj.defaultPage();

        pf = new PageFormat();
        pf.setOrientation(PageFormat.REVERSE_LANDSCAPE);
        pf = pj.validatePage(pf);
        pj.setCopies(10);
        pj.getCopies();
        pj.getUserName();
        pj.setJobName("no-job-name");
        pj.getJobName();
    }
}
