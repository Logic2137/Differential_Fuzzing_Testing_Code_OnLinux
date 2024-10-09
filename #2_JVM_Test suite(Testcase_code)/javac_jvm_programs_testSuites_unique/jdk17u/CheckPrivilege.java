import java.awt.print.*;

public class CheckPrivilege implements Printable {

    static boolean verbose;

    private static void println(String mess) {
        if (verbose) {
            System.err.println(mess);
        }
    }

    static class PrintLover extends SecurityManager {

        public void checkPrintJobAccess() {
        }

        public void checkPackageAccess(String pkg) {
        }

        public void checkPropertyAccess(String key) {
        }
    }

    class Printing extends RuntimeException {
    }

    public static void main(String[] argv) {
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("INSTRUCTIONS: You should have a printer configured in your system to do this test. Test fails if you get this error message:");
        System.out.println("   \"Regression: printing causes a NullPointerException\"");
        System.out.println("-----------------------------------------------------------------------");
        if (argv.length > 0 && argv[0].equals("-v")) {
            verbose = true;
        }
        java.awt.Toolkit.getDefaultToolkit();
        try {
            SecurityManager sm = new PrintLover();
            println("Installing PrintLover security manager");
            System.setSecurityManager(sm);
            println("Installed security manager OK");
        } catch (Throwable th) {
            System.err.println("Failed to install SecurityManager");
            th.printStackTrace();
            throw new RuntimeException("Failed to install SecurityManager");
        }
        try {
            println("calling getPrinterJob");
            PrinterJob pj = PrinterJob.getPrinterJob();
            if ((pj == null) || (pj.getPrintService() == null)) {
                return;
            }
            println("PrinterJob class is " + pj.getClass());
            println("calling pj.setPrintable");
            pj.setPrintable(new CheckPrivilege());
            println("calling pj.print");
            pj.print();
            println("done pj.print");
        } catch (Printing ex) {
            println("Caught \"Printing\" exception OK");
        } catch (PrinterException ex) {
            System.err.println("Caught " + ex);
            throw new RuntimeException("" + ex);
        } catch (NullPointerException ex) {
            System.err.println("Caught " + ex);
            System.err.println("Regression: printing causes a NullPointerException");
            throw ex;
        }
    }

    public int print(java.awt.Graphics g, PageFormat pf, int index) {
        println("Started printing " + index);
        return Printable.NO_SUCH_PAGE;
    }
}
