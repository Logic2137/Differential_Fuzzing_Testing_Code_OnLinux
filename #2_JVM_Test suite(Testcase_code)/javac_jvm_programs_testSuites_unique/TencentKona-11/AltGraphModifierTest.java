



import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AltGraphModifierTest {
    private static void init() throws Exception {
        String[] instructions
                = {
                    "This test is for verifying Alt-Gr modifier of an event.",
                    "Linux :-",
                    "1. Please check if Alt-Gr key is present on keyboard.",
                    "2. If present, press the Alt-Gr key and perform",
                    "   mouse click on the TestWindow.",
                    "3. Navigate to System Settings-> Keyboard-> Shortcuts->",
                    "   Typing.",
                    "4. Select an option for the Alternative Characters Key",
                    "   For example. Right Alt",
                    "5. Close the settings and navigate to test",
                    "6. Press Right Alt Key & perform mouse click on the",
                    "   TestWindow",
                    "7. Test will exit by itself with appropriate result.",
                    " ",
                };

        Sysout.createDialog();
        Sysout.printInstructions(instructions);
    }

    static Frame mainFrame;
    public static void initTestWindow() {
        mainFrame = new Frame();
        mainFrame.setTitle("TestWindow");
        mainFrame.setBounds(700, 10, 300, 300);
        mainFrame.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int ex = e.getModifiersEx();
                if ((ex & InputEvent.ALT_GRAPH_DOWN_MASK) == 0) {
                    AltGraphModifierTest.fail("Alt-Gr Modifier bit is not set.");
                } else {
                    AltGraphModifierTest.pass();
                }
            }
        });
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
        dialog.show();
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

    
    public TestDialog(Frame frame, String name) {
        super(frame, name);
        int scrollBoth = TextArea.SCROLLBARS_BOTH;
        instructionsText = new TextArea("", 15, maxStringLength, scrollBoth);
        add("North", instructionsText);

        messageText = new TextArea("", 5, maxStringLength, scrollBoth);
        add("Center", messageText);

        buttonP = new Panel();
        failB = new Button("Fail");
        failB.setActionCommand("fail");
        failB.addActionListener(this);
        buttonP.add("Center", failB);

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

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "fail") {
            AltGraphModifierTest.fail("User Clicked Fail");
        }
    }
}
