import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Test6991580 {

    private static void init() throws Exception {
        String[] instructions = { "This test should only be run on non-Windows systems.", "If your system doesn't meet this condition, press PASS.", "To run the test follow these instructions:", "1. Open a terminal window.", "2. Make sure you have, for example, the following snippet " + "into your platform's /etc/resolv.conf:", "nameserver 127.0.0.1", "nameserver 2001:4860:4860::8888", "nameserver [::1]:5353", "nameserver 127.0.0.1:5353", "Modify the /etc/resolv.conf file if needed. " + "Don't forget to save the original content of the file.", "3. Type \"cd " + System.getProperty("test.classes") + "\".", "4. Type \"" + System.getProperty("java.home") + "/bin/java IPv6NameserverPlatformParsingTest\".", "5. If you see", "\"PASS: Found IPv6 address and DnsClient parsed it correctly.\"", ", press PASS else press FAIL.", "6. If you modified /etc/resolv.conf on the step #2, " + "please, restore the original content of the file." };
        Sysout.createDialog();
        Sysout.printInstructions(instructions);
    }

    private static boolean theTestPassed = false;

    private static boolean testGeneratedInterrupt = false;

    private static String failureMessage = "";

    private static Thread mainThread = null;

    private static int sleepTime = 300000;

    public static void main(String[] args) throws Exception {
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
        dialog.show();
        println("Any messages for the tester will display here.");
    }

    public static void createDialog() {
        dialog = new TestDialog(new Frame(), "Instructions");
        String[] defInstr = { "Instructions will appear here. ", "" };
        dialog.printInstructions(defInstr);
        dialog.show();
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

    int maxStringLength = 120;

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
        show();
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
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("pass".equals(e.getActionCommand())) {
            Test6991580.pass();
        } else {
            Test6991580.fail();
        }
    }
}
