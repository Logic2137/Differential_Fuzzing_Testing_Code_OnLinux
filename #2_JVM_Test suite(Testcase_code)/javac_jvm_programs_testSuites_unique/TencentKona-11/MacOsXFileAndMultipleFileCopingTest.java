



import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class MacOsXFileAndMultipleFileCopingTest {
    private static void init() {
        String[] instructions =
                {"Test for MacOS X only:",
                        "1. The aim is to test that java works fine with \"application/" +
                                "x-java-url;class=java.net.URL\"falvor and support coping of multiple files",
                        "2. Open finder and select any file.",
                        "3. Press CMD+C or press \"Copy\" in context menu",
                        "4. Focus window with \"Test URL\" Button.",
                        "5. If you see URL for selected file, then test PASSED,",
                        "otherwise test FAILED.",

                        "6. Open finder again and select several files.",
                        "7. Press CMD+C or press \"Copy\" in context menu",
                        "8. Focus window with \"Test multiple files coping\" Button.",
                        "9. If you see list of selected files, then test PASSED,",
                        "otherwise test FAILED.",

                };

        Sysout.createDialog();
        Sysout.printInstructions(instructions);

        final Frame frame = new Frame();
        Panel panel = new Panel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));

        frame.add(panel);
        Button testUrlBtn = new Button("Test URL");
        final TextArea textArea = new TextArea(5, 80);
        testUrlBtn.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
                    URL url = (URL) board.getData(new DataFlavor("application/x-java-url;class=java.net.URL"));
                    textArea.setText(url.toString());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        panel.add(testUrlBtn);
        Button testUriList = new Button("Test multiple files coping");
        testUriList.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                try {
                    Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
                        String files = (String) board.getData(new DataFlavor("text/uri-list;class=java.lang.String"));
                    textArea.setText(files);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        panel.add(testUriList);
        panel.add(textArea);
        frame.setBounds(200, 200, 400, 400);
        frame.setVisible(true);

    }


    
    private static boolean theTestPassed = false;
    private static boolean testGeneratedInterrupt = false;
    private static String failureMessage = "";

    private static Thread mainThread = null;

    private static int sleepTime = 300000;

    public static void main(String args[]) throws InterruptedException {
        if (!System.getProperty("os.name").startsWith("Mac")) {
            return;
        }
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
            if (!testGeneratedInterrupt) throw e;

            
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
        if (mainThread != null) {
            mainThread.interrupt();
        }
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
    private static boolean numbering = false;
    private static int messageNumber = 0;

    public static void createDialogWithInstructions(String[] instructions) {
        dialog = new TestDialog(new Frame(), "Instructions");
        dialog.printInstructions(instructions);
        dialog.setVisible(true);
        println("Any messages for the tester will display here.");
    }

    public static void createDialog() {
        dialog = new TestDialog(new Frame(), "Instructions");
        String[] defInstr = {"Instructions will appear here. ", ""};
        dialog.printInstructions(defInstr);
        dialog.setVisible(true);
        println("Any messages for the tester will display here.");
    }


    
    public static void enableNumbering(boolean enable) {
        numbering = enable;
    }

    public static void printInstructions(String[] instructions) {
        dialog.printInstructions(instructions);
    }


    public static void println(String messageIn) {
        if (numbering) {
            messageIn = "" + messageNumber + " " + messageIn;
            messageNumber++;
        }
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
                    
                    int posOfSpace = remainingStr.
                            lastIndexOf(' ', maxStringLength - 1);

                    if (posOfSpace <= 0) posOfSpace = maxStringLength - 1;

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
        System.out.println(messageIn);
    }

    
    
    
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "pass") {
            MacOsXFileAndMultipleFileCopingTest.pass();
        } else {
            MacOsXFileAndMultipleFileCopingTest.fail();
        }
    }

}
