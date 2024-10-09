import java.awt.*;
import javax.swing.*;
import java.awt.print.*;

public class SwingUIText implements Printable {

    static String[] instructions = { "This tests that when a Swing UI is printed, that the text", "in each component properly matches the length of the component", "as seen on-screen, and that the spacing of the text is of", "reasonable even-ness. This latter part is very subjective and", "the comparison has to be with JDK1.5 GA, or JDK 1.6 GA" };

    static JFrame frame;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                createUI();
            }
        });
    }

    public static void createUI() {
        Sysout.createDialogWithInstructions(instructions);
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));
        String text = "marvelous suspicious solving";
        displayText(panel, text);
        String itext = "\u0641\u0642\u0643 \u0644\u0627\u064b";
        itext = itext + itext + itext + itext + itext + itext + itext;
        displayText(panel, itext);
        String itext2 = "\u0641" + text;
        displayText(panel, itext2);
        JEditorPane editor = new JEditorPane();
        editor.setContentType("text/html");
        String CELL = "<TD align=\"center\"><font style=\"font-size: 18;\">Text</font></TD>";
        String TABLE_BEGIN = "<TABLE BORDER=1 cellpadding=1 cellspacing=0 width=100%>";
        String TABLE_END = "</TABLE>";
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html><body>").append(TABLE_BEGIN);
        for (int j = 0; j < 15; j++) {
            buffer.append(CELL);
        }
        buffer.append("</tr>");
        buffer.append(TABLE_END).append("</body></html>");
        editor.setText(buffer.toString());
        panel.add(editor);
        frame = new JFrame("Swing UI Text Printing Test");
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pf = job.defaultPage();
        job.setPrintable(new SwingUIText(), pf);
        if (job.printDialog()) {
            try {
                job.print();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    static void displayText(JPanel p, String text) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        JPanel row = new JPanel();
        Font font = new Font("Dialog", Font.PLAIN, 12);
        JLabel label = new JLabel(text);
        label.setFont(font);
        row.add(label);
        JButton button = new JButton("Print " + text);
        button.setMnemonic('P');
        button.setFont(font);
        row.add(button);
        panel.add(row);
        row = new JPanel();
        JTextField textField = new JTextField(text);
        row.add(textField);
        JTextArea textArea = new JTextArea();
        textArea.setText(text);
        row.add(textArea);
        panel.add(row);
        p.add(panel);
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) throws PrinterException {
        if (pageIndex >= 1) {
            return Printable.NO_SUCH_PAGE;
        }
        g.translate((int) pf.getImageableX(), (int) pf.getImageableY());
        frame.printAll(g);
        return Printable.PAGE_EXISTS;
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
        instructionsText = new TextArea("", 10, maxStringLength, scrollBoth);
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
