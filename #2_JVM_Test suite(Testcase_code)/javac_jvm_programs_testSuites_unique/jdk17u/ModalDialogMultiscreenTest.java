import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ModalDialogMultiscreenTest {

    private static class ButtonActionListener implements ActionListener {

        JFrame frame;

        JDialog dialog;

        public ButtonActionListener(JFrame frame, JDialog dialog) {
            this.frame = frame;
            this.dialog = dialog;
        }

        public void actionPerformed(ActionEvent e) {
            dialog.setLocationRelativeTo(frame);
            dialog.setVisible(true);
        }
    }

    public static class TestDialog extends JDialog {

        public TestDialog(Frame owner, String title, boolean modal, GraphicsConfiguration gc) {
            super(owner, title, modal, gc);
            setSize(200, 100);
            JButton button = new JButton("Close");
            button.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            getContentPane().add(button);
        }
    }

    private static void init() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        Sysout.createDialog();
        if (gs.length < 2) {
            System.out.println("Not multi-head environment, test not valid!");
            ModalDialogMultiscreenTest.pass();
        }
        String[] instructions = { "The test should be run on a multi-head X (non-xinerama) systems.", "Otherwise click the Pass button right now.", "You will see an open Frame on each screen your system has.", "The frame has an 'Open dialog' button.", "Clicking the button opens a modal dialog with a Close button.", "The test procedure:", "1. Open a dialog and close it with appropriate buttons.", "2. Switch to another screen ($ DISPLAY=X.Y xprop)", "3. Repeat steps 1-2 several times (about 3*<number-of-screens>)", "If the test doesn't cause the window manager to hang, it's passed." };
        Sysout.printInstructions(instructions);
        for (int i = 0; i < gs.length; i++) {
            JFrame frame = new JFrame("Frame " + i, gs[i].getDefaultConfiguration());
            JButton button = new JButton("Open Dialog");
            button.setMinimumSize(new Dimension(200, 100));
            button.setPreferredSize(new Dimension(200, 100));
            button.setSize(new Dimension(200, 100));
            button.addActionListener(new ButtonActionListener(frame, new TestDialog(frame, "Dialog #" + i, true, gs[i].getDefaultConfiguration())));
            frame.getContentPane().add(button);
            frame.pack();
            frame.setVisible(true);
        }
    }

    private static boolean theTestPassed = false;

    private static boolean testGeneratedInterrupt = false;

    private static String failureMessage = "";

    private static Thread mainThread = null;

    private static int sleepTime = 300000;

    public static void main(String[] args) throws InterruptedException {
        mainThread = Thread.currentThread();
        try {
            init();
        } catch (TestPassedException e) {
            return;
        }
        try {
            Thread.sleep(sleepTime);
            throw new RuntimeException("Timed out after " + sleepTime / 1000 + " seconds");
        } catch (InterruptedException e) {
            if (!testGeneratedInterrupt)
                throw e;
            testGeneratedInterrupt = false;
            if (theTestPassed == false) {
                throw new RuntimeException(failureMessage);
            }
        }
    }

    public static synchronized void setTimeoutTo(int seconds) {
        sleepTime = seconds * 1000;
    }

    public static synchronized void pass() {
        Sysout.println("The test passed.");
        Sysout.println("The test is over, hit  Ctl-C to stop Java VM");
        if (mainThread == Thread.currentThread()) {
            theTestPassed = true;
            throw new TestPassedException();
        }
        theTestPassed = true;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
    }

    public static synchronized void fail() {
        fail("it just plain failed! :-)");
    }

    public static synchronized void fail(String whyFailed) {
        Sysout.println("The test failed: " + whyFailed);
        Sysout.println("The test is over, hit  Ctl-C to stop Java VM");
        if (mainThread == Thread.currentThread()) {
            throw new RuntimeException(whyFailed);
        }
        theTestPassed = false;
        testGeneratedInterrupt = true;
        failureMessage = whyFailed;
        mainThread.interrupt();
    }
}

class TestPassedException extends RuntimeException {
}

class Sysout {

    private static TestDialog dialog;

    public static void createDialogWithInstructions(String[] instructions) {
        dialog = new TestDialog(new Frame(), "Instructions");
        dialog.printInstructions(instructions);
        dialog.setVisible(true);
        println("Any messages for the tester will display here.");
    }

    public static void createDialog() {
        dialog = new TestDialog(new Frame(), "Instructions");
        String[] defInstr = { "Instructions will appear here. ", "" };
        dialog.printInstructions(defInstr);
        dialog.setVisible(true);
        println("Any messages for the tester will display here.");
    }

    public static void printInstructions(String[] instructions) {
        dialog.printInstructions(instructions);
    }

    public static void println(String messageIn) {
        dialog.displayMessage(messageIn);
    }
}

class TestDialog extends Dialog implements ActionListener {

    TextArea instructionsText;

    TextArea messageText;

    int maxStringLength = 80;

    Panel buttonP = new Panel();

    Button passB = new Button("pass");

    Button failB = new Button("fail");

    public TestDialog(Frame frame, String name) {
        super(frame, name);
        int scrollBoth = TextArea.SCROLLBARS_BOTH;
        instructionsText = new TextArea("", 15, maxStringLength, scrollBoth);
        add("North", instructionsText);
        messageText = new TextArea("", 5, maxStringLength, scrollBoth);
        add("Center", messageText);
        passB = new Button("pass");
        passB.setActionCommand("pass");
        passB.addActionListener(this);
        buttonP.add("East", passB);
        failB = new Button("fail");
        failB.setActionCommand("fail");
        failB.addActionListener(this);
        buttonP.add("West", failB);
        add("South", buttonP);
        pack();
        setVisible(true);
    }

    public void printInstructions(String[] instructions) {
        instructionsText.setText("");
        String printStr, remainingStr;
        for (int i = 0; i < instructions.length; i++) {
            remainingStr = instructions[i];
            while (remainingStr.length() > 0) {
                if (remainingStr.length() >= maxStringLength) {
                    int posOfSpace = remainingStr.lastIndexOf(' ', maxStringLength - 1);
                    if (posOfSpace <= 0)
                        posOfSpace = maxStringLength - 1;
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
        System.out.println(messageIn);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "pass") {
            ModalDialogMultiscreenTest.pass();
        } else {
            ModalDialogMultiscreenTest.fail();
        }
    }
}
