import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.print.*;

public class PrintGlyphVectorTest extends Component implements Printable {

    public void drawGVs(Graphics g) {
        String testString = "0123456789abcdefghijklm";
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.black);
        Font font = new Font("SansSerif", Font.PLAIN, 30);
        FontRenderContext frc = g2d.getFontRenderContext();
        GlyphVector v = font.createGlyphVector(frc, testString);
        float x = 50f, y = 50f;
        g2d.drawGlyphVector(v, x, y);
        Rectangle2D r = v.getVisualBounds();
        r.setRect(r.getX() + x, r.getY() + y, r.getWidth(), r.getHeight());
        g2d.draw(r);
        Point2D p;
        for (int i = 0; i < v.getNumGlyphs(); i++) {
            p = v.getGlyphPosition(i);
            p.setLocation(p.getX() + 50, p.getY());
            v.setGlyphPosition(i, p);
        }
        x = 0;
        y += 50;
        g2d.drawGlyphVector(v, x, y);
        r = v.getVisualBounds();
        r.setRect(r.getX() + x, r.getY() + y, r.getWidth(), r.getHeight());
        g2d.draw(r);
    }

    public void paint(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, getSize().width, getSize().height);
        drawGVs(g);
    }

    public Dimension getPreferredSize() {
        return new Dimension(600, 200);
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        if (pageIndex > 0) {
            return Printable.NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        drawGVs(g2d);
        return Printable.PAGE_EXISTS;
    }

    public static void main(String[] arg) throws Exception {
        Frame f = new Frame();
        PrintGlyphVectorTest pvt = new PrintGlyphVectorTest();
        f.add("Center", pvt);
        f.add("South", new PrintInstructions());
        f.pack();
        f.show();
    }
}

class PrintInstructions extends Panel implements ActionListener {

    static final String INSTRUCTIONS = "You must have a printer installed for this test.\n" + "Press the PRINT button below and OK the print dialog\n" + "Retrieve the output and compare the printed and on-screen text\n" + " to confirm that in both cases the text is aligned and the boxes\n" + "are around the text, not offset from the text.";

    PrintInstructions() {
        setLayout(new GridLayout(2, 1));
        TextArea t = new TextArea(INSTRUCTIONS, 8, 80);
        add(t);
        Button b = new Button("PRINT");
        b.setFont(new Font("Dialog", Font.BOLD, 30));
        b.addActionListener(this);
        add(b);
    }

    public void actionPerformed(ActionEvent e) {
        PrinterJob pj = PrinterJob.getPrinterJob();
        if (pj == null || pj.getPrintService() == null || !pj.printDialog()) {
            return;
        }
        pj.setPrintable(new PrintGlyphVectorTest());
        try {
            pj.print();
        } catch (PrinterException ex) {
            System.err.println(ex);
        }
    }
}
