import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.JobAttributes;
import java.awt.PrintJob;
import java.awt.Toolkit;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class JobAttrUpdateTest {

    private static Thread mainThread;

    private static boolean testPassed;

    private static boolean testGeneratedInterrupt;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            doTest(JobAttrUpdateTest::printTest);
        });
        mainThread = Thread.currentThread();
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            if (!testPassed && testGeneratedInterrupt) {
                throw new RuntimeException("" + "JobAttributes.getFromPage(),getToPage() not updated correctly");
            }
        }
        if (!testGeneratedInterrupt) {
            throw new RuntimeException("user has not executed the test");
        }
    }

    private static void printTest() {
        JobAttributes ja = new JobAttributes();
        Toolkit tk = Toolkit.getDefaultToolkit();
        PrintJob pjob = tk.getPrintJob(new JFrame(), "test", ja, null);
        if (pjob == null) {
            return;
        }
        if (ja.getDefaultSelection() == JobAttributes.DefaultSelectionType.RANGE) {
            int fromPage = ja.getFromPage();
            int toPage = ja.getToPage();
            if (fromPage != 2 || toPage != 3) {
                fail();
            } else {
                pass();
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
        String description = " A print dialog will be shown.\n " + " Please select Pages within Page-range.\n" + " and enter From 2 and To 3. Then Select OK.";
        final JDialog dialog = new JDialog();
        dialog.setTitle("JobAttribute Updation Test");
        JTextArea textArea = new JTextArea(description);
        textArea.setEditable(false);
        final JButton testButton = new JButton("Start Test");
        testButton.addActionListener((e) -> {
            testButton.setEnabled(false);
            action.run();
        });
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(textArea, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(testButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        dialog.add(mainPanel);
        dialog.pack();
        dialog.setVisible(true);
    }
}
