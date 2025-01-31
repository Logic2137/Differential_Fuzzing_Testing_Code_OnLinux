import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PageRanges;
import javax.print.attribute.standard.DialogTypeSelection;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class DlgAttrsBug implements Printable {

    private static Thread mainThread;

    private static boolean testPassed;

    private static boolean testGeneratedInterrupt;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            doTest(DlgAttrsBug::printTest);
        });
        mainThread = Thread.currentThread();
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            if (!testPassed && testGeneratedInterrupt) {
                throw new RuntimeException("Print Dialog does not " + "reflect Copies or Page Ranges");
            }
        }
        if (!testGeneratedInterrupt) {
            throw new RuntimeException("user has not executed the test");
        }
    }

    private static void printTest() {
        PrinterJob job = PrinterJob.getPrinterJob();
        if (job.getPrintService() == null) {
            System.out.println("No printers. Test cannot continue");
            return;
        }
        job.setPrintable(new DlgAttrsBug());
        PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        aset.add(new Copies(5));
        aset.add(new PageRanges(3, 4));
        aset.add(DialogTypeSelection.NATIVE);
        job.printDialog(aset);
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
        String description = " Visual inspection of print dialog is required.\n" + " A print dialog will be shown.\n " + " Please verify Copies 5 is selected.\n" + " Also verify, Page Range is selected with " + " from page 3 and to Page 4.\n" + " If ok, press PASS else press FAIL";
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

    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
        System.out.println("pi = " + pi);
        if (pi >= 5) {
            return NO_SUCH_PAGE;
        }
        g.drawString("Page : " + (pi + 1), 200, 200);
        return PAGE_EXISTS;
    }
}
