import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;

public class CustomFont implements Printable {

    private Image opaqueimg, transimg;

    private static void init() {
        String[] instructions = { "On-screen inspection is not possible for this printing-specific", "test therefore its only output is a printed page.", "To be able to run this test it is required to have a default", "printer configured in your user environment.", "", "Visual inspection of the printed page is needed. A passing", "test will print a page on which one line of text will be", "printed: a long string of 'A' characters.", "The A should have of a curly style", "If instead its in the default sansserif font, the test fails" };
        Sysout.createDialog();
        Sysout.printInstructions(instructions);
        PrinterJob pjob = PrinterJob.getPrinterJob();
        Book book = new Book();
        PageFormat portrait = pjob.defaultPage();
        book.append(new CustomFont(), portrait);
        pjob.setPageable(book);
        if (pjob.printDialog()) {
            try {
                pjob.print();
            } catch (PrinterException e) {
                System.err.println(e);
                e.printStackTrace();
            }
        }
        System.out.println("Done Printing");
    }

    Font customFont;

    public CustomFont() {
        try {
            String dir = System.getProperty("test.src", ".");
            String fileName = dir + File.separator + "A.ttf";
            FileInputStream fin = new FileInputStream(fileName);
            Font cf = Font.createFont(Font.TRUETYPE_FONT, fin);
            customFont = cf.deriveFont(Font.PLAIN, 14);
        } catch (Exception ioe) {
            throw new RuntimeException(ioe);
        }
    }

    public int print(Graphics g, PageFormat pgFmt, int pgIndex) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.translate(pgFmt.getImageableX(), pgFmt.getImageableY());
        g2D.setColor(Color.black);
        g2D.setFont(customFont);
        String str = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
        g.drawString(str, 100, 100);
        return Printable.PAGE_EXISTS;
    }

    private void printScale(Graphics2D g2D, Font font, float scale, float x, float y) {
        int RES = 72;
        g2D.scale(scale, scale);
        g2D.setFont(font.deriveFont(10.0f / scale));
        g2D.drawString("This text is scaled by a factor of " + scale, x * RES / scale, y * RES / scale);
        g2D.scale(1 / scale, 1 / scale);
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
            if (!testGeneratedInterrupt)
                throw e;
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
        mainThread.interrupt();
    }

    public static synchronized void fail() {
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

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == "pass") {
            CustomFont.pass();
        } else {
            CustomFont.fail();
        }
    }
}
