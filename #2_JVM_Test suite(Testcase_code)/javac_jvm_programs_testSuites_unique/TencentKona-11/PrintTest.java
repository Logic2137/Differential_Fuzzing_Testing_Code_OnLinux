

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.JobAttributes;
import java.awt.PageAttributes;
import java.awt.PrintJob;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class PrintTest {

    private static Thread mainThread;
    private static boolean testPassed;
    private static boolean testGeneratedInterrupt;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                doTest(PrintTest::printTest);
            }
        });
        mainThread = Thread.currentThread();
        try {
            mainThread.sleep(30000);
        } catch (InterruptedException e) {
            if (!testPassed && testGeneratedInterrupt) {
                throw new RuntimeException("All radio button is not selected");
            }
        }
        if (!testGeneratedInterrupt) {
            throw new RuntimeException("user has not executed the test");
        }
    }

    private static void printTest() {
        JobAttributes job = new JobAttributes();
        PageAttributes page = new PageAttributes();
        job.setDialog(JobAttributes.DialogType.NATIVE);
        job.setDefaultSelection(JobAttributes.DefaultSelectionType.ALL);
        job.setFromPage(2);
        job.setToPage(5);
        Toolkit tk = Toolkit.getDefaultToolkit();
        
        if (tk != null) {
            PrintJob pj = tk.getPrintJob(new JFrame(),
                          "testing the attribute setting ", job, page);
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
                + " A print dialog will be shown.\n "
                + " Please verify ALL radio button is selected.\n"
                + " If ALL radio button is selected,press PASS else press FAIL";

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
