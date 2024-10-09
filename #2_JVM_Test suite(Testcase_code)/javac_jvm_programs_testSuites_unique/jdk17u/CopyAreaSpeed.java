import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class CopyAreaSpeed extends Applet implements Runnable {

    int top = 0;

    public void init() {
    }

    public CopyAreaSpeed() {
        super();
        String[] instructions = { "This test prints out the time it takes for a certain amount ", "of copyArea calls to be completed. Because the performance ", "measurement is relative, this code only provides a benchmark ", "to run with different releases to compare the outcomes." };
        Sysout.createDialogWithInstructions(instructions);
        (new Thread(this)).start();
        Button bt = new Button("Hello");
        bt.setBounds(50, 10, 50, 22);
        bt.setVisible(false);
        add(bt);
    }

    public void update(Graphics g) {
        paint(g);
    }

    public void paint(Graphics g) {
        synchronized (this) {
            Rectangle rct = g.getClipBounds();
            g.setColor(Color.white);
            g.fillRect(rct.x, rct.y, rct.width, rct.height);
            g.setFont(getFont());
            g.setColor(Color.black);
            Dimension dm = getSize();
            for (int y = 0; y <= (dm.height + 10); y += 20) {
                if (y > rct.y) {
                    int z = y / 20 + top;
                    g.drawString("" + z, 10, y);
                }
            }
        }
    }

    static long millsec(Date s, Date e) {
        long ts = s.getTime();
        long te = e.getTime();
        return te - ts;
    }

    public void run() {
        int count = 1000;
        int loops = count;
        Date start;
        Date end;
        start = new Date();
        while (count-- > 0) {
            Dimension dm = getSize();
            if (dm != null && dm.width != 0 && dm.height != 0) {
                synchronized (this) {
                    top++;
                    Graphics g = getGraphics();
                    g.copyArea(0, 20, dm.width, dm.height - 20, 0, -20);
                    g.setClip(0, dm.height - 20, dm.width, 20);
                    paint(g);
                    g.dispose();
                }
            }
            try {
                Thread.sleep(1);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        end = new Date();
        Sysout.println("copyArea X " + loops + " = " + millsec(start, end) + " msec");
    }

    public static void main(String[] args) {
        Frame frm = new Frame("CopyAreaSpeed");
        frm.add(new CopyAreaSpeed());
        frm.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent ev) {
                System.exit(0);
            }
        });
        frm.setSize(500, 500);
        frm.show();
    }
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
        add("South", messageText);
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
