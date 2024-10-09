import java.applet.Applet;
import java.awt.*;

public class MouseDraggedOutCauseScrollingTest extends Applet {

    Choice choice;

    List singleList;

    List multipleList;

    public void init() {
        this.setLayout(new GridLayout(1, 3));
        choice = new Choice();
        singleList = new List(3, false);
        multipleList = new List(3, true);
        choice.add("Choice");
        for (int i = 1; i < 100; i++) {
            choice.add("" + i);
        }
        singleList.add("Single list");
        for (int i = 1; i < 100; i++) singleList.add("" + i);
        multipleList.add("Multiple list");
        for (int i = 1; i < 100; i++) multipleList.add("" + i);
        this.add(choice);
        this.add(singleList);
        this.add(multipleList);
        String toolkitName = Toolkit.getDefaultToolkit().getClass().getName();
        if (!toolkitName.equals("sun.awt.X11.XToolkit")) {
            String[] instructions = { "This test is not applicable to the current platform. Press PASS" };
            Sysout.createDialogWithInstructions(instructions);
        } else {
            String[] instructions = { "0) Please note, that this is only Motif/XAWT test. At first, make the applet active", "1.1) Click on the choice", "1.2) Press the left button of the mouse and keep on any item of the choice, for example 5", "1.3) Drag mouse out of the area of the unfurled list, at the same time hold the X coordinate of the mouse position about the same", "1.4) To make sure, that when the Y coordinate of the mouse position higher of the upper bound of the list then scrolling UP of the list and selected item changes on the upper. If not, the test failed", "1.5) To make sure, that when the Y coordinate of the mouse position under of the lower bound of the list then scrolling DOWN of the list and selected item changes on the lower. If not, the test failed", "-----------------------------------", "2.1) Click on the single list", "2.2) Press the left button of the mouse and keep on any item of the list, for example 5", "2.3) Drag mouse out of the area of the unfurled list, at the same time hold the X coordinate of the mouse position about the same", "2.4) To make sure, that when the Y coordinate of the mouse position higher of the upper bound of the list then scrolling UP of the list and selected item changes on the upper. If not, the test failed", "2.5) To make sure, that when the Y coordinate of the mouse position under of the lower bound of the list then scrolling DOWN of the list and selected item changes on the lower. If not, the test failed", "-----------------------------------", "3.1) Click on the multiple list", "3.2) Press the left button of the mouse and keep on any item of the list, for example 5", "3.3) Drag mouse out of the area of the unfurled list, at the same time hold the X coordinate of the mouse position about the same", "3.4) To make sure, that when the Y coordinate of the mouse position higher of the upper bound of the list then scrolling of the list NO OCCURED and selected item NO CHANGES on the upper. If not, the test failed", "3.5) To make sure, that when the Y coordinate of the mouse position under of the lower bound of the list then scrolling of the list NO OCCURED and selected item NO CHANGES on the lower. If not, the test failed", "4) Test passed." };
            Sysout.createDialogWithInstructions(instructions);
        }
    }

    public void start() {
        setSize(400, 100);
        setVisible(true);
        validate();
    }
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
}
