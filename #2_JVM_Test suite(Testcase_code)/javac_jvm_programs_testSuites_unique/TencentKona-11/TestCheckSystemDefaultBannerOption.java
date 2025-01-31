

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import static java.awt.print.Printable.NO_SUCH_PAGE;
import static java.awt.print.Printable.PAGE_EXISTS;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.PrintService;
import javax.print.attribute.standard.JobSheets;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;


public class TestCheckSystemDefaultBannerOption implements Printable {
    private static Thread mainThread;
    private static boolean testPassed;
    private static boolean testGeneratedInterrupt;
    private static boolean noJobSheet = false;
    private static PrinterJob job = null;

    public static void main (String[] args) throws Exception {

        job = PrinterJob.getPrinterJob();
        PrintService prtSrv = job.getPrintService();
        if (prtSrv == null) {
            System.out.println("No printers. Test cannot continue");
            return;
        }
        
        if (!prtSrv.isAttributeCategorySupported(JobSheets.class)) {
            return;
        }
        
        JobSheets js = (JobSheets)job.getPrintService().
                getDefaultAttributeValue(JobSheets.class);
        if (js != null && js.equals(JobSheets.NONE)) {
            noJobSheet = true;
        }
        SwingUtilities.invokeAndWait(() -> {
            doTest(TestCheckSystemDefaultBannerOption::printTest);
        });
        mainThread = Thread.currentThread();
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            if (!testPassed && testGeneratedInterrupt) {
                String banner = noJobSheet ? "Banner page" : " No Banner page";
                throw new RuntimeException(banner + " is printed");
            }
        }
        if (!testGeneratedInterrupt) {
            throw new RuntimeException("user has not executed the test");
        }
    }

    private static void printTest() {
        job.setPrintable(new TestCheckSystemDefaultBannerOption());
        try {
            job.print();
        } catch (PrinterException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void pass() {
        testPassed = true;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
    }

    public static synchronized void fail() {
        testPassed = false;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
    }

    private static void doTest(Runnable action) {
        String banner = null;
        if (noJobSheet) {
            banner = "No Banner page";
        } else {
            banner = "Banner page";
        }
        String description
                = " A testpage will be sent to printer. \n"
                + " Please check if " + banner + " is printed \n"
                + " along with testpage.\n "
                + " If " + banner + " is printed, press PASS else press FAIL";

        final JDialog dialog = new JDialog();
        dialog.setTitle("printSelectionTest");
        JTextArea textArea = new JTextArea(description);
        textArea.setEditable(false);
        final JButton testButton = new JButton("Start Test");
        final JButton passButton = new JButton("PASS");
        passButton.setEnabled(false);
        passButton.addActionListener((e) -> {
            dialog.dispose();
            pass();
        });
        final JButton failButton = new JButton("FAIL");
        failButton.setEnabled(false);
        failButton.addActionListener((e) -> {
            dialog.dispose();
            fail();
        });
        testButton.addActionListener((e) -> {
            testButton.setEnabled(false);
            action.run();
            passButton.setEnabled(true);
            failButton.setEnabled(true);
        });
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(textArea, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(testButton);
        buttonPanel.add(passButton);
        buttonPanel.add(failButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setVisible(true);
        dialog.addWindowListener(new WindowAdapter() {
           @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("main dialog closing");
                testGeneratedInterrupt = false;
                mainThread.interrupt();
            }
        });
    }

    @Override
    public int print(Graphics g, PageFormat pf, int pi)
            throws PrinterException {
        System.out.println("pi = " + pi);
        g.drawString("Testing", 100, 100);
        if (pi == 1)
            return NO_SUCH_PAGE;
        return PAGE_EXISTS;
    }
}
