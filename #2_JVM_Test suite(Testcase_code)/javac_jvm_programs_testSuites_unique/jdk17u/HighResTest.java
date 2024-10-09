import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.JobAttributes;
import java.awt.PageAttributes;
import java.awt.PrintJob;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.JobAttributes.DialogType;
import java.awt.JobAttributes.SidesType;
import java.awt.PageAttributes.OrientationRequestedType;
import java.awt.PageAttributes.OriginType;
import java.awt.Dialog;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HighResTest {

    static Frame f = new Frame();

    private static void init() {
        String[] instructions = { "To be able to run this test it is required to have a default", "printer configured in your user environment.", "If no default printer exists, then test passes.", " ", "There will be 2 print dialogs.  The first dialog should show", "portrait as the selected orientation.  The 2nd dialog should show", "landscape as the selected orientation.", " ", "Visual inspection of the printed pages is needed. A passing", "test will print 2 pages in portrait and 2 pages in landscape.", "The pages have on the center of the page the text \"Center\"", "2 rectangles will appear above and below it, the one below is", "filled." };
        Sysout.createDialog();
        Sysout.printInstructions(instructions);
        PrintJob job = null;
        Dimension dim = null;
        JobAttributes jobAttributes = new JobAttributes();
        PageAttributes pageAttributes = new PageAttributes();
        String center = "Center";
        Font font = new Font("SansSerif", Font.PLAIN, 200);
        FontMetrics metrics = null;
        int width = 0;
        Graphics g = null;
        jobAttributes.setDialog(DialogType.NATIVE);
        pageAttributes.setOrigin(OriginType.PRINTABLE);
        pageAttributes.setPrinterResolution(new int[] { 1200, 1200, 3 });
        pageAttributes.setOrientationRequested(OrientationRequestedType.PORTRAIT);
        jobAttributes.setSides(SidesType.TWO_SIDED_LONG_EDGE);
        job = f.getToolkit().getPrintJob(f, "Portrait Test", jobAttributes, pageAttributes);
        if (job != null) {
            dim = job.getPageDimension();
            for (int i = 0; i < 2; i++) {
                g = job.getGraphics();
                g.drawLine(0, 0, dim.width, 0);
                g.drawLine(dim.width, 0, dim.width, dim.height);
                g.drawLine(dim.width, dim.height, 0, dim.height);
                g.drawLine(0, dim.height, 0, 0);
                g.drawRect(dim.width / 2 - 200, dim.height / 3 - 300, 400, 600);
                g.fillRect(dim.width / 2 - 200, 2 * dim.height / 3 - 300, 400, 600);
                g.setFont(font);
                metrics = g.getFontMetrics();
                width = metrics.stringWidth(center);
                g.setColor(Color.black);
                g.drawString(center, (dim.width / 2) - (width / 2), dim.height / 2);
                g.dispose();
            }
            job.end();
            job = null;
        }
        pageAttributes.setOrientationRequested(OrientationRequestedType.LANDSCAPE);
        job = f.getToolkit().getPrintJob(f, "Landscape Test", jobAttributes, pageAttributes);
        if (job != null) {
            dim = job.getPageDimension();
            for (int i = 0; i < 2; i++) {
                g = job.getGraphics();
                g.drawLine(0, 0, dim.width, 0);
                g.drawLine(dim.width, 0, dim.width, dim.height);
                g.drawLine(dim.width, dim.height, 0, dim.height);
                g.drawLine(0, dim.height, 0, 0);
                g.drawRect(dim.width / 2 - 200, dim.height / 3 - 300, 400, 600);
                g.fillRect(dim.width / 2 - 200, 2 * dim.height / 3 - 300, 400, 600);
                g.setFont(font);
                metrics = g.getFontMetrics();
                width = metrics.stringWidth(center);
                g.setColor(Color.black);
                g.drawString(center, (dim.width / 2) - (width / 2), dim.height / 2);
                g.dispose();
            }
            job.end();
            job = null;
        }
        System.out.println("done");
    }

    private static boolean theTestPassed = false;

    private static boolean testGeneratedInterrupt = false;

    private static String failureMessage = "";

    private static Thread mainThread = null;

    private static int sleepTime = 300000;

    public static void main(String[] args) throws InterruptedException {
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
            if (!testGeneratedInterrupt) {
                throw e;
            }
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
        if (mainThread == Thread.currentThread()) {
            theTestPassed = true;
            throw new TestPassedException();
        }
        theTestPassed = true;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
        Sysout.dispose();
    }

    public static synchronized void fail() {
        fail("it just plain failed! :-)");
    }

    public static synchronized void fail(String whyFailed) {
        Sysout.println("The test failed: " + whyFailed);
        if (mainThread == Thread.currentThread()) {
            throw new RuntimeException(whyFailed);
        }
        theTestPassed = false;
        testGeneratedInterrupt = true;
        failureMessage = whyFailed;
        mainThread.interrupt();
        Sysout.dispose();
    }
}

class TestPassedException extends RuntimeException {
}

class Sysout {

    private static TestDialog dialog;

    public static void createDialogWithInstructions(String[] instructions) {
        dialog = new TestDialog(new Frame(), "Instructions");
        dialog.printInstructions(instructions);
        println("Any messages for the tester will display here.");
    }

    public static void createDialog() {
        dialog = new TestDialog(new Frame(), "Instructions");
        String[] defInstr = { "Instructions will appear here. ", "" };
        dialog.printInstructions(defInstr);
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
        HighResTest.f.dispose();
        dialog.dispose();
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

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "pass") {
            HighResTest.pass();
        } else {
            HighResTest.fail();
        }
    }
}
