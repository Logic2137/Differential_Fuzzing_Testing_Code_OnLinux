

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class PageDialogMarginValidation {

    private static Thread mainThread;
    private static boolean testPassed;
    private static boolean testGeneratedInterrupt;
    private static PrinterJob printJob;

    public static void pageMarginTest() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        HashPrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
        PageFormat pf;
        pf = pj.pageDialog(aset);
    }

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            doTest(PageDialogMarginValidation::pageMarginTest);
        });
        mainThread = Thread.currentThread();
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            if (!testPassed && testGeneratedInterrupt) {
                throw new RuntimeException(""
                        + "Margin validation is not correct");
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
                = " Initially, a page dialog will be shown.\n "
                + " The left, right, bottom, right margin will have value 1.0.\n"
                + " Modify right margin from 1.0 to 0.0 and press tab.\n"
                + " Please verify the right margin changes back to 1.0. \n"
                + " Please do the same for bottom margin.\n"
                + " If right and bottom margin changes back to 1.0, press PASS else press fail";

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
