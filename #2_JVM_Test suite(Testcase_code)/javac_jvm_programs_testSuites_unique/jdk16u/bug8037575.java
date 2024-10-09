


import java.awt.Frame;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.Timer;

public class bug8037575 {

    private static boolean theTestPassed;
    private static boolean testGeneratedInterrupt;
    private static Thread mainThread;
    private static int sleepTime = 30000;
    private static int waitTime = 2000;
    private final static JFrame frame = new JFrame("bug8037575");

    private static void init() throws Exception {

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                String[] instructions
                        = {
                            "1) You see a dialog with buttons and text area.",
                            "2) Pressing Run button will start test. A new Frame will open automatically"
                        + " and minimize after 2 seconds.",
                            "3) Frame should minimize gradually with animation effect.",
                            "4) If frame disappers without animation then test "
                            + "failed otherwise passed.",
                            "5) Pressing Pass/Fail button will mark test as "
                            + "pass/fail and will shutdown JVM as well"};

                Sysout.createDialogWithInstructions(instructions);
                Sysout.printInstructions(instructions);
            }
        });
    }

    
    public static void main(String args[]) throws InterruptedException {

        mainThread = Thread.currentThread();
        try {
            init();
        } catch (Exception ex) {
            Logger.getLogger(bug8037575.class.getName()).log(Level.SEVERE, null, ex);
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

    public static synchronized void runTest() {
        frame.setSize(800, 600);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Timer timer = new Timer(waitTime, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                frame.setExtendedState(Frame.ICONIFIED);
                frame.dispose();
                Sysout.println("Test completed please press/fail button");
            }
        });
        timer.setRepeats(false);
        timer.start();
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
                bug8037575.runTest();
                passB.setEnabled(true);
                failB.setEnabled(true);
            }
        });

        passB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                bug8037575.pass();
            }
        });

        failB.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                bug8037575.fail();
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
                    int posOfSpace = remainingStr.
                            lastIndexOf(' ', maxStringLength - 1);

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
