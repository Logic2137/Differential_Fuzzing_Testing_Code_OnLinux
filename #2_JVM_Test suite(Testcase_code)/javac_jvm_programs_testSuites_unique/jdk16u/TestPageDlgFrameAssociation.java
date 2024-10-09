

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class TestPageDlgFrameAssociation {
    private static Thread mainThread;
    private static boolean testPassed;
    private static boolean testGeneratedInterrupt;
    private static Button print;
    private static Frame frame;
    private static boolean start;
    private static Thread t;

    public static void main(String args[]) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            doTest(TestPageDlgFrameAssociation::frameTest);
        });
        mainThread = Thread.currentThread();
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            if (!testPassed && testGeneratedInterrupt) {
                throw new RuntimeException("Page dialog not disposed."
                        + " Page dialog is not associated with owner Frame`");
            }
        }
        if (!testGeneratedInterrupt) {
            throw new RuntimeException("user has not executed the test");
        }
    }

    private static void frameTest() {
        Panel panel =new Panel();

        print = new Button("PageDialog");
        print.setActionCommand("PageDialog");
        print.addActionListener((e) -> {
            PrinterJob job = PrinterJob.getPrinterJob();
                PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                t.start();
                start = true;
                PageFormat pf = job.pageDialog(aset);
        });

        panel.add(print);

        frame = new Frame("Test Frame");
        frame.setLayout (new BorderLayout ());
        frame.add(panel,"South");
        frame.pack();
        frame.setVisible(true);

        t = new Thread (() -> {
            if (start) {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException ex) {}
                frame.dispose();
            }
        });
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
                = " A Frame with PageDialog Button is shown. Press PageDialog.\n"
                + " A page dialog will be shown. Do not press any button.\n"
                + " After 5 secs the frame along with this page dialog will be disposed.\n"
                + " If the page dialog is not disposed, press FAIL else press PASS";

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
}
