
 
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.print.Printable;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class PrintDlgSelectionAttribTest {

    private static Thread mainThread;
    private static boolean testPassed;
    private static boolean testGeneratedInterrupt;
    private static PrinterJob printJob;

    public static void print() {

        
        printJob.setPrintable(new Printable() {
            public int print(Graphics graphics, PageFormat pageFormat,
                    int pageIndex) throws PrinterException {
                return NO_SUCH_PAGE;
            }
        });

        
        if (!printJob.printDialog()) {
            System.out.println("\tPrinting canceled by user");
            return;
        }

        try {
            printJob.print();
        } catch (PrinterException e) {
        }
    }

    public static void printTest() {
        printJob = PrinterJob.getPrinterJob();
        System.out.println(" -=- Starting printing #1 -=-");
        print();
        System.out.println(" -=- Starting printing #2 -=-");
        print();
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            doTest(PrintDlgSelectionAttribTest::printTest);
        });
        mainThread = Thread.currentThread();
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            if (!testPassed && testGeneratedInterrupt) {
                throw new RuntimeException(""
                        + "Selection radio button is enabled in print dialog");
            }
        }
        if (!testGeneratedInterrupt) {
            throw new RuntimeException("user has not executed the test");
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
        String description
                = " Visual inspection of print dialog is required.\n"
                + " Initially, a print dialog will be shown.\n "
                + " Please verify Selection radio button is disabled.\n"
                + " Press OK. Then 2nd print dialog will be shown.\n"
                + " Please verify the Selection radio button is disabled\n"
                + " in 2nd print dialog. If disabled, press PASS else press fail";

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
    }
}
