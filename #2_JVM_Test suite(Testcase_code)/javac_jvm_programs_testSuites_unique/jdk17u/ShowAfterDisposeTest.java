import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;

public class ShowAfterDisposeTest extends Applet {

    boolean traySupported;

    public void init() {
        this.setLayout(new BorderLayout());
        String[] instructions;
        traySupported = SystemTray.isSupported();
        if (traySupported) {
            String[] s = { "1) When the test starts an icon is added to the SystemTray area.", "2a) If you use Apple OS X,", "    right click on this icon (it's important to click before the tooltip is shown).", "    The icon should disappear.", "2b) If you use other os (Windows, Linux, Solaris),", "    double click on this icon (it's important to click before the tooltip is shown).", "    The icon should disappear.", "3) If the bug is reproducible then the test will fail without assistance.", "4) Just press the 'pass' button." };
            instructions = s;
        } else {
            String[] s = { "The test cannot be run because SystemTray is not supported.", "Simply press PASS button." };
            instructions = s;
        }
        Sysout.createDialogWithInstructions(instructions);
    }

    public void start() {
        setSize(200, 200);
        setVisible(true);
        validate();
        if (!traySupported) {
            return;
        }
        BufferedImage img = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 32, 32);
        g.setColor(Color.RED);
        g.fillRect(6, 6, 20, 20);
        g.dispose();
        final SystemTray tray = SystemTray.getSystemTray();
        final TrayIcon icon = new TrayIcon(img);
        icon.setImageAutoSize(true);
        icon.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ev) {
                tray.remove(icon);
            }
        });
        try {
            tray.add(icon);
        } catch (AWTException e) {
            Sysout.println(e.toString());
            Sysout.println("!!! The test coudn't be performed !!!");
            return;
        }
        icon.setToolTip("tooltip");
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
