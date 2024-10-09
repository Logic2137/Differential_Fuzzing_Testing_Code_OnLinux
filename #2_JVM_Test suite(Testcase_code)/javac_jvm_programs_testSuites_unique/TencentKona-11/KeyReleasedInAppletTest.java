

import javax.swing.*;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import java.awt.*;
import java.awt.FileDialog;
import java.awt.Label;
import java.awt.event.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.lang.*;
import java.lang.Override;
import java.lang.String;
import java.lang.System;
import java.lang.Throwable;
import java.util.Hashtable;



public class KeyReleasedInAppletTest extends JApplet {
    private static final String TEST_HTML_NAME = "TestApplet.html";

    public void init() {
        
        
        
        this.setLayout(new BorderLayout());

        try {
            String testFilePath = System.getProperty("test.classes");
            FileWriter testHTML = null;
            try {
                testHTML = new FileWriter(testFilePath + "/" + TEST_HTML_NAME);
                testHTML.write("<html>\n" +
                        "<head>\n" +
                        "<title>KeyReleasedInAppletTest </title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<h1>KeyReleasedInAppletTest<br>Bug ID:8010009 </h1>\n" +
                        "<p>Make sure the applet is focuced and type any character on the keyboard. <br>"+
                        "The applet should show keyPressed, keyTyped and keyReleased messages.</p>\n" +
                        "<APPLET CODE=\"TestApplet.class\" WIDTH=400 HEIGHT=200></APPLET>\n" +
                        "</body>");
            } finally {
                if (testHTML != null) {
                    testHTML.close();
                }
            }

            String[] instructions =
                    {
                            "(1) Install the tested JDK to be used by the Java Plugin.\n",
                            "(2) Open Java Preferences and set security level to minimal.\n",
                            "(3) Open the " + TEST_HTML_NAME + " in Firefox in firefox web browser\n" +
                                    " It is located at: " + testFilePath,
                            "(5) Continue the test according to the instructions in the applet.\n",
                    };
            Sysout.createDialogWithInstructions(instructions);
        } catch (Throwable e) {
            
            throw new RuntimeException(e.getMessage());
        }

    }

    public void start() {
    }
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


class TestDialog extends Dialog {

    TextArea instructionsText;
    TextArea messageText;
    int maxStringLength = 80;

    
    public TestDialog(Frame frame, String name) {
        super(frame, name);
        int scrollBoth = TextArea.SCROLLBARS_BOTH;
        instructionsText = new TextArea("", 15, maxStringLength, scrollBoth);
        add("North", instructionsText);

        messageText = new TextArea("", 5, maxStringLength, scrollBoth);
        add("Center", messageText);

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

}
