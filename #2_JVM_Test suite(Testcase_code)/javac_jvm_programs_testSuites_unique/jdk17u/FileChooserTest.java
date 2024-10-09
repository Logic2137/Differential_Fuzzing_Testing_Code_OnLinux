import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class FileChooserTest {

    private static boolean theTestPassed;

    private static boolean testGeneratedInterrupt;

    private static Thread mainThread;

    private static int sleepTime = 30000;

    public static JFileChooser fileChooser;

    private static void init() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                String[] instructions = { "1) Create a folder with read only permissions by " + "changing security permission through Security tab" + "under Folder->Properties menu to deny write permission" + " to the newly created folder", "2) Click on run test button.It will open a open dialog" + " Navigate to the newly created read only folder", "3) Click on the create new folder button in open dialog", "4) If an error message does not pops up" + "test failed otherwise passed.", "5) Pressing Pass/Fail button will mark test as " + "pass/fail and will shutdown JVM", "6) Newly created folder permissions can now be restored" + " back to default" };
                Sysout.createDialogWithInstructions(instructions);
                Sysout.printInstructions(instructions);
            }
        });
    }

    public static void main(String[] args) throws Exception {
        mainThread = Thread.currentThread();
        try {
            init();
        } catch (Exception ex) {
            return;
        }
        try {
            mainThread.sleep(sleepTime);
        } catch (InterruptedException ex) {
            Sysout.dispose();
            if (!theTestPassed && testGeneratedInterrupt) {
                throw new RuntimeException("Test Failed");
            }
        }
        if (!testGeneratedInterrupt) {
            Sysout.dispose();
            throw new RuntimeException("Test Failed");
        }
    }

    public static synchronized void pass() {
        theTestPassed = true;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
    }

    public static synchronized void fail() {
        theTestPassed = false;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
    }
}

class Sysout {

    private static TestDialog dialog;

    private static JFrame frame;

    public static void createDialogWithInstructions(String[] instructions) {
        frame = new JFrame();
        dialog = new TestDialog(frame, "Instructions");
        dialog.printInstructions(instructions);
        dialog.setVisible(true);
        println("Any messages for the tester will display here.");
    }

    public static void printInstructions(String[] instructions) {
        dialog.printInstructions(instructions);
    }

    public static void println(String messageIn) {
        dialog.displayMessage(messageIn);
    }

    public static void dispose() {
        Sysout.println("Shutting down the Java process..");
        if (FileChooserTest.fileChooser != null) {
            FileChooserTest.fileChooser.cancelSelection();
        }
        frame.dispose();
        dialog.dispose();
    }
}

class TestDialog extends JDialog {

    private TextArea instructionsText;

    private TextArea messageText;

    private int maxStringLength = 80;

    private Panel buttonP = new Panel();

    private JButton run = new JButton("Run");

    private JButton passB = new JButton("Pass");

    private JButton failB = new JButton("Fail");

    public TestDialog(JFrame frame, String name) {
        super(frame, name);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        int scrollBoth = TextArea.SCROLLBARS_BOTH;
        instructionsText = new TextArea("", 15, maxStringLength, scrollBoth);
        add("North", instructionsText);
        messageText = new TextArea("", 5, maxStringLength, scrollBoth);
        add("Center", messageText);
        buttonP.add("East", run);
        buttonP.add("East", passB);
        buttonP.add("West", failB);
        passB.setEnabled(false);
        failB.setEnabled(false);
        add("South", buttonP);
        run.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                FileChooserTest.fileChooser = new JFileChooser();
                FileChooserTest.fileChooser.showOpenDialog(null);
                passB.setEnabled(true);
                failB.setEnabled(true);
            }
        });
        passB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                FileChooserTest.pass();
            }
        });
        failB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                FileChooserTest.fail();
            }
        });
        pack();
        setVisible(true);
    }

    public void printInstructions(String[] instructions) {
        instructionsText.setText("");
        String printStr, remainingStr;
        for (String instruction : instructions) {
            remainingStr = instruction;
            while (remainingStr.length() > 0) {
                if (remainingStr.length() >= maxStringLength) {
                    int posOfSpace = remainingStr.lastIndexOf(' ', maxStringLength - 1);
                    if (posOfSpace <= 0) {
                        posOfSpace = maxStringLength - 1;
                    }
                    printStr = remainingStr.substring(0, posOfSpace + 1);
                    remainingStr = remainingStr.substring(posOfSpace + 1);
                } else {
                    printStr = remainingStr;
                    remainingStr = "";
                }
                instructionsText.append(printStr + "\n");
            }
        }
    }

    public void displayMessage(String messageIn) {
        messageText.append(messageIn + "\n");
    }
}
