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
import javax.print.attribute.standard.SheetCollate;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class BannerTest implements Printable {

    private static Thread mainThread;

    private static boolean testPassed;

    private static boolean testGeneratedInterrupt;

    private static volatile PrinterJob job;

    public static void main(String[] args) throws Exception {
        job = PrinterJob.getPrinterJob();
        PrintService prtSrv = job.getPrintService();
        if (job.getPrintService() == null) {
            System.out.println("No printers. Test cannot continue");
            return;
        }
        if (!prtSrv.isAttributeCategorySupported(JobSheets.class)) {
            return;
        }
        SwingUtilities.invokeAndWait(() -> {
            doTest(BannerTest::printTest);
        });
        mainThread = Thread.currentThread();
        try {
            Thread.sleep(180000);
        } catch (InterruptedException e) {
            if (!testPassed && testGeneratedInterrupt) {
                throw new RuntimeException("Banner page did not print");
            }
        }
        if (!testGeneratedInterrupt) {
            throw new RuntimeException("user has not executed the test");
        }
    }

    private static void printTest() {
        job.setPrintable(new BannerTest());
        if (job.printDialog()) {
            try {
                job.print();
            } catch (PrinterException e) {
                e.printStackTrace();
                fail();
            }
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
        String description = " A print dialog will be shown.\n" + " Click on the \"Appearance\" tab.\n " + " Select the \"Banner page\" checkbox.\n" + " Click on Print, and check if Banner page is printed.\n " + " If Banner page is printed, press PASS else press FAIL";
        final JDialog dialog = new JDialog();
        dialog.setTitle("printBannerTest");
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
    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
        System.out.println("pi = " + pi);
        g.drawString("Testing", 100, 100);
        if (pi == 1)
            return NO_SUCH_PAGE;
        return PAGE_EXISTS;
    }
}
