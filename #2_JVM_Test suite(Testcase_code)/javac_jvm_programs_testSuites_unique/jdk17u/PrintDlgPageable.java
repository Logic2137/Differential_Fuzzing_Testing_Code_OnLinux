import java.awt.*;
import java.awt.print.*;
import java.util.Locale;
import javax.print.*;

class PrintDlgPageable implements Printable {

    public static int arg;

    public PrintDlgPageable() {
        super();
    }

    public static void main(java.lang.String[] args) {
        if (args.length < 1) {
            System.out.println("usage: java PrintDlgPageable { 0 | 2}");
            return;
        }
        arg = Integer.parseInt(args[0]);
        PrintDlgPageable pd = new PrintDlgPageable();
        PrinterJob pj = PrinterJob.getPrinterJob();
        PageableHandler handler = new PageableHandler();
        pj.setPageable(handler);
        System.out.println("open PrintDialog..");
        if (pj.printDialog()) {
            try {
                System.out.println("About to print the data ...");
                pj.print();
                System.out.println("Printed");
            } catch (PrinterException pe) {
                pe.printStackTrace();
            }
        }
    }

    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.black);
        g2.translate(pf.getImageableX(), pf.getImageableY());
        g2.drawRect(1, 1, 200, 300);
        g2.drawRect(1, 1, 25, 25);
        System.out.println("print method called " + pi + " Orientation " + pf.getOrientation());
        return Printable.PAGE_EXISTS;
    }
}

class PageableHandler implements Pageable {

    PageFormat pf = new PageFormat();

    public int getNumberOfPages() {
        return PrintDlgPageable.arg;
    }

    public Printable getPrintable(int pageIndex) {
        return new PrintDlgPageable();
    }

    public PageFormat getPageFormat(int pageIndex) {
        System.out.println("getPageFormat called " + pageIndex);
        if (pageIndex == 0) {
            pf.setOrientation(PageFormat.PORTRAIT);
            System.out.println("Orientation returned from Pageable " + findOrientation(pf.getOrientation()));
            return pf;
        } else {
            pf.setOrientation(PageFormat.LANDSCAPE);
            System.out.println("Orientation returned from Pageable " + findOrientation(pf.getOrientation()));
            return pf;
        }
    }

    public String findOrientation(int orient) {
        if (orient == PageFormat.LANDSCAPE) {
            return "LANDSCAPE";
        } else if (orient == PageFormat.PORTRAIT) {
            return "PORTRAIT";
        } else if (orient == PageFormat.REVERSE_LANDSCAPE) {
            return "REVERSE LANDSCAPE";
        } else {
            return null;
        }
    }
}
