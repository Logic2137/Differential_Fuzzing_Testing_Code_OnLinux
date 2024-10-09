import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;

public class ExceptionFromPrintableIsIgnoredTest {

    private enum TestThreadType {

        MAIN, EDT
    }

    private enum TestExceptionType {

        PE, RE
    }

    private volatile Throwable printError;

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new RuntimeException("Two arguments are expected:" + " test thread type and test exception type.");
        }
        new ExceptionFromPrintableIsIgnoredTest(TestThreadType.valueOf(args[0]), TestExceptionType.valueOf(args[1]));
    }

    public ExceptionFromPrintableIsIgnoredTest(final TestThreadType threadType, final TestExceptionType exceptionType) {
        System.out.println(String.format("Test started. threadType='%s', exceptionType='%s'", threadType, exceptionType));
        String osName = System.getProperty("os.name");
        boolean isOSX = osName.toLowerCase().startsWith("mac");
        if ((exceptionType == TestExceptionType.RE) && !isOSX) {
            System.out.println("Currently this test scenario can be verified only on macOS.");
            return;
        }
        printError = null;
        if (threadType == TestThreadType.MAIN) {
            runTest(exceptionType);
        } else if (threadType == TestThreadType.EDT) {
            try {
                SwingUtilities.invokeAndWait(new Runnable() {

                    @Override
                    public void run() {
                        runTest(exceptionType);
                    }
                });
            } catch (InterruptedException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        if (printError == null) {
            throw new RuntimeException("No exception was thrown.");
        } else if (!(printError instanceof PrinterException)) {
            throw new RuntimeException("Unexpected exception was thrown.");
        }
        System.out.println("Test passed.");
    }

    private void runTest(final TestExceptionType exceptionType) {
        PrinterJob job = PrinterJob.getPrinterJob();
        if (job.getPrintService() == null) {
            System.out.println("No printers are available.");
            return;
        }
        job.setPrintable(new Printable() {

            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 1) {
                    return NO_SUCH_PAGE;
                }
                if (exceptionType == TestExceptionType.PE) {
                    throw new PrinterException("Exception from 'Printable.print'.");
                } else if (exceptionType == TestExceptionType.RE) {
                    throw new RuntimeException("Exception from 'Printable.print'.");
                }
                return PAGE_EXISTS;
            }
        });
        try {
            job.print();
        } catch (Throwable t) {
            printError = t;
            System.out.println("'PrinterJob.print' threw the exception:");
            t.printStackTrace(System.out);
        }
    }
}
