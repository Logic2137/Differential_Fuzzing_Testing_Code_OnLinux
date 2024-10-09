


import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ScrollSelectionTest {

    static Frame mainFrame;
    static TextField textField;

    private static void init() throws Exception {
        String[] instructions
                = {
                    "INSTRUCTIONS: There are 2 Tests",
                    "Test1: Text visibility with Scroll",
                    "This is a test for a win32 specific problem",
                    "If you see all the letters from 'a' to 'z' and",
                    "letters from 'a' to 't' are selected then test passes.",
                    "You may have to activate the frame to see the selection",
                    "highlighted (e.g. by clicking on frame's title).",
                    ".",
                    "Test2: Flicker with selection scroll",
                    "Mouse press on the TextField text.",
                    "Move mouse towards left or right with selecting text.",
                    "Move mouse away outside the bounds of TextField.",
                    "No flicker should be observed.",
                };

        Sysout.createDialog();
        Sysout.printInstructions(instructions);
    }

    public static void initTestWindow() {
        mainFrame = new Frame("ScrollSelectionTest frame");
        mainFrame.setBounds(500, 0, 400, 200);

        textField = new TextField(40);
        textField.setText("abcdefghijklmnopqrstuvwxyz");
        mainFrame.add(textField);
        mainFrame.setLayout(new FlowLayout());
        textField.select(0, 20);
        mainFrame.setVisible(true);
    }

    public static void dispose() {
        Sysout.dispose();
        mainFrame.dispose();
    }

    
    private static boolean theTestPassed = false;
    private static boolean testGeneratedInterrupt = false;
    private static String failureMessage = "";
    private static Thread mainThread = null;
    final private static int sleepTime = 300000;

    public static void main(String args[]) throws Exception {
        mainThread = Thread.currentThread();
        try {
            init();
            initTestWindow();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            mainThread.sleep(sleepTime);
        } catch (InterruptedException e) {
            dispose();
            if (testGeneratedInterrupt && !theTestPassed) {
                throw new Exception(failureMessage);
            }
        }
        if (!testGeneratedInterrupt) {
            dispose();
            throw new RuntimeException("Timed out after " + sleepTime / 1000
                    + " seconds");
        }
    }

    public static synchronized void pass() {
        theTestPassed = true;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
    }

    public static synchronized void fail(String whyFailed) {
        theTestPassed = false;
        testGeneratedInterrupt = true;
        failureMessage = whyFailed;
        mainThread.interrupt();
    }
}




class Sysout {
    private static TestDialog dialog;
    private static Frame frame;

    public static void createDialog() {
        frame = new Frame();
        dialog = new TestDialog(frame, "Instructions");
        String[] defInstr = {"Instructions will appear here. ", ""};
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

    public static void dispose() {
        dialog.dispose();
        frame.dispose();
    }
}


class TestDialog extends Dialog implements ActionListener {
    TextArea instructionsText;
    TextArea messageText;
    int maxStringLength = 80;
    Panel buttonP;
    Button failB;
    Button passB;

    
    public TestDialog(Frame frame, String name) {
        super(frame, name);
        int scrollBoth = TextArea.SCROLLBARS_BOTH;
        instructionsText = new TextArea("", 15, maxStringLength, scrollBoth);
        add("North", instructionsText);

        messageText = new TextArea("", 5, maxStringLength, scrollBoth);
        add("Center", messageText);

        buttonP = new Panel();
        passB = new Button("pass");
        passB.setActionCommand("pass");
        passB.addActionListener(this);
        buttonP.add("East", passB);

        failB = new Button("Fail");
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
                    int posOfSpace = remainingStr.
                            lastIndexOf(' ', maxStringLength - 1);

                    if (posOfSpace <= 0) {
                        posOfSpace = maxStringLength - 1;
                    }

                    printStr = remainingStr.substring(0, posOfSpace + 1);
                    remainingStr = remainingStr.substring(posOfSpace + 1);
                }
                else {
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
        if (e.getActionCommand().equals("pass")) {
            ScrollSelectionTest.pass();
        } else {
            ScrollSelectionTest.fail("User Clicked Fail");
        }
    }
}
