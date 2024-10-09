import java.applet.Applet;
import java.awt.*;
import java.awt.print.*;
import java.awt.event.*;
import java.awt.geom.Ellipse2D;

public class XparColor extends Applet implements Printable {

    public void init() {
        String[] instructions = { "This test verify that the BullsEye rings are printed correctly. The printout should show transparent rings with increasing darkness toward the center" };
        Sysout.createDialogWithInstructions(instructions);
    }

    public XparColor() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void main(String[] s) {
        XparColor xc = new XparColor();
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(xc);
        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public int print(Graphics g, PageFormat pf, int pi) throws PrinterException {
        if (pi >= 1) {
            return Printable.NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        g2d.translate(pf.getImageableWidth() / 2, pf.getImageableHeight() / 2);
        Dimension d = new Dimension(400, 400);
        double scale = Math.min(pf.getImageableWidth() / d.width, pf.getImageableHeight() / d.height);
        if (scale < 1.0) {
            g2d.scale(scale, scale);
        }
        g2d.translate(-d.width / 2.0, -d.height / 2.0);
        Graphics2D g2 = (Graphics2D) g;
        drawDemo(d.width, d.height, g2);
        g2.dispose();
        return Printable.PAGE_EXISTS;
    }

    public void drawDemo(int w, int h, Graphics2D g2) {
        Color[] reds = { Color.red.darker(), Color.red };
        for (int N = 0; N < 18; N++) {
            float i = (N + 2) / 2.0f;
            float x = (float) (5 + i * (w / 2 / 10));
            float y = (float) (5 + i * (h / 2 / 10));
            float ew = (w - 10) - (i * w / 10);
            float eh = (h - 10) - (i * h / 10);
            float alpha = (N == 0) ? 0.1f : 1.0f / (19.0f - N);
            if (N >= 16)
                g2.setColor(reds[N - 16]);
            else
                g2.setColor(new Color(0f, 0f, 0f, alpha));
            g2.fill(new Ellipse2D.Float(x, y, ew, eh));
        }
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
}
