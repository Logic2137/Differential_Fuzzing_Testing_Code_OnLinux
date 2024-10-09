import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;

public class PageSetupDialog extends Frame implements Printable {

    PrinterJob myPrinterJob;

    PageFormat myPageFormat;

    Label pw, ph, pglm, pgiw, pgrm, pgtm, pgih, pgbm;

    Label myWidthLabel;

    Label myHeightLabel;

    Label myImageableXLabel;

    Label myImageableYLabel;

    Label myImageableRightLabel;

    Label myImageableBottomLabel;

    Label myImageableWidthLabel;

    Label myImageableHeightLabel;

    Label myOrientationLabel;

    Checkbox reverseCB;

    boolean alpha = false;

    boolean reverse = false;

    protected void displayPageFormatAttributes() {
        myWidthLabel.setText("Format Width = " + (float) myPageFormat.getWidth());
        myHeightLabel.setText("Format Height = " + (float) myPageFormat.getHeight());
        myImageableXLabel.setText("Format Left Margin = " + (float) myPageFormat.getImageableX());
        myImageableRightLabel.setText("Format Right Margin = " + (float) (myPageFormat.getWidth() - (myPageFormat.getImageableX() + myPageFormat.getImageableWidth())));
        myImageableWidthLabel.setText("Format ImageableWidth = " + (float) myPageFormat.getImageableWidth());
        myImageableYLabel.setText("Format Top Margin = " + (float) myPageFormat.getImageableY());
        myImageableBottomLabel.setText("Format Bottom Margin = " + (float) (myPageFormat.getHeight() - (myPageFormat.getImageableY() + myPageFormat.getImageableHeight())));
        myImageableHeightLabel.setText("Format ImageableHeight = " + (float) myPageFormat.getImageableHeight());
        int o = myPageFormat.getOrientation();
        if (o == PageFormat.LANDSCAPE && reverse) {
            o = PageFormat.REVERSE_LANDSCAPE;
            myPageFormat.setOrientation(PageFormat.REVERSE_LANDSCAPE);
        } else if (o == PageFormat.REVERSE_LANDSCAPE && !reverse) {
            o = PageFormat.LANDSCAPE;
            myPageFormat.setOrientation(PageFormat.LANDSCAPE);
        }
        myOrientationLabel.setText("Format Orientation = " + (o == PageFormat.PORTRAIT ? "PORTRAIT" : o == PageFormat.LANDSCAPE ? "LANDSCAPE" : o == PageFormat.REVERSE_LANDSCAPE ? "REVERSE_LANDSCAPE" : "<invalid>"));
        Paper p = myPageFormat.getPaper();
        pw.setText("Paper Width = " + (float) p.getWidth());
        ph.setText("Paper Height = " + (float) p.getHeight());
        pglm.setText("Paper Left Margin = " + (float) p.getImageableX());
        pgiw.setText("Paper Imageable Width = " + (float) p.getImageableWidth());
        pgrm.setText("Paper Right Margin = " + (float) (p.getWidth() - (p.getImageableX() + p.getImageableWidth())));
        pgtm.setText("Paper Top Margin = " + (float) p.getImageableY());
        pgih.setText("Paper Imageable Height = " + (float) p.getImageableHeight());
        pgbm.setText("Paper Bottom Margin = " + (float) (p.getHeight() - (p.getImageableY() + p.getImageableHeight())));
    }

    public PageSetupDialog() {
        super("Page Dialog Test");
        myPrinterJob = PrinterJob.getPrinterJob();
        myPageFormat = new PageFormat();
        Paper p = new Paper();
        double margin = 1.5 * 72;
        p.setImageableArea(margin, margin, p.getWidth() - 2 * margin, p.getHeight() - 2 * margin);
        myPageFormat.setPaper(p);
        Panel c = new Panel();
        c.setLayout(new GridLayout(9, 2, 0, 0));
        c.add(reverseCB = new Checkbox("reverse if landscape"));
        c.add(myOrientationLabel = new Label());
        c.add(myWidthLabel = new Label());
        c.add(pw = new Label());
        c.add(myImageableXLabel = new Label());
        c.add(pglm = new Label());
        c.add(myImageableRightLabel = new Label());
        c.add(pgrm = new Label());
        c.add(myImageableWidthLabel = new Label());
        c.add(pgiw = new Label());
        c.add(myHeightLabel = new Label());
        c.add(ph = new Label());
        c.add(myImageableYLabel = new Label());
        c.add(pgtm = new Label());
        c.add(myImageableHeightLabel = new Label());
        c.add(pgih = new Label());
        c.add(myImageableBottomLabel = new Label());
        c.add(pgbm = new Label());
        reverseCB.addItemListener(new ItemListener() {

            public void itemStateChanged(ItemEvent e) {
                reverse = e.getStateChange() == ItemEvent.SELECTED;
                int o = myPageFormat.getOrientation();
                if (o == PageFormat.LANDSCAPE || o == PageFormat.REVERSE_LANDSCAPE) {
                    displayPageFormatAttributes();
                }
            }
        });
        add("Center", c);
        displayPageFormatAttributes();
        Panel panel = new Panel();
        Button pageButton = new Button("Page Setup...");
        pageButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                myPageFormat = myPrinterJob.pageDialog(myPageFormat);
                displayPageFormatAttributes();
            }
        });
        Button printButton = new Button("Print ...");
        printButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    if (myPrinterJob.printDialog()) {
                        myPrinterJob.setPrintable(PageSetupDialog.this, myPageFormat);
                        alpha = false;
                        myPrinterJob.print();
                    }
                } catch (PrinterException pe) {
                }
            }
        });
        Button printAlphaButton = new Button("Print w/Alpha...");
        printAlphaButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                try {
                    if (myPrinterJob.printDialog()) {
                        myPrinterJob.setPrintable(PageSetupDialog.this, myPageFormat);
                        alpha = true;
                        myPrinterJob.print();
                    }
                } catch (PrinterException pe) {
                }
            }
        });
        panel.add(pageButton);
        panel.add(printButton);
        panel.add(printAlphaButton);
        add("South", panel);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
        pack();
        setVisible(true);
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        g2d.drawString("ORIGIN(" + pageFormat.getImageableX() + "," + pageFormat.getImageableY() + ")", 20, 20);
        g2d.drawString("X THIS WAY", 200, 50);
        g2d.drawString("Y THIS WAY", 60, 200);
        g2d.drawString("Graphics is " + g2d.getClass().getName(), 100, 100);
        g2d.drawRect(0, 0, (int) pageFormat.getImageableWidth(), (int) pageFormat.getImageableHeight());
        if (alpha) {
            g2d.setColor(new Color(0, 0, 255, 192));
        } else {
            g2d.setColor(Color.blue);
        }
        g2d.drawRect(1, 1, (int) pageFormat.getImageableWidth() - 2, (int) pageFormat.getImageableHeight() - 2);
        return Printable.PAGE_EXISTS;
    }

    public static void main(String[] args) {
        String[] instructions = { "You must have a printer available to perform this test", "This test is very flexible and requires much interaction.", "If the platform print dialog supports it, adjust orientation", "and margins and print pages and compare the results with the", "request." };
        Sysout.createDialog();
        Sysout.printInstructions(instructions);
        new PageSetupDialog();
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
        add("Center", messageText);
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
