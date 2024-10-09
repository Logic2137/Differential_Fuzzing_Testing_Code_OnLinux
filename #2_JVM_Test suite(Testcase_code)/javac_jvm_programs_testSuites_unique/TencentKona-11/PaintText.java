



import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.print.*;
import javax.swing.*;

public class PaintText extends Component implements Printable {

    static int preferredSize;
    static int NUMTABS = 6;
    int tabNumber;

    public static void main(String args[]) {

        PrinterJob pjob = PrinterJob.getPrinterJob();
        if (pjob.getPrintService() == null) {
            System.out.println("No printers: cannot continue");
            return;
        }

        PageFormat pf = pjob.defaultPage();
        preferredSize = (int)pf.getImageableWidth();

        Book book = new Book();

        JTabbedPane p = new JTabbedPane();

        for (int id=1; id <= NUMTABS; id++) {
            String name = "Tab " + new Integer(id);
            PaintText ptt = new PaintText(id);
            p.add(name, ptt);
            book.append(ptt, pf);
        }
        pjob.setPageable(book);

        JFrame f = new JFrame();
        f.add(BorderLayout.CENTER, p);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {System.exit(0);}
        });
        f.pack();
        f.show();

        
        if (System.getProperty("test.jdk") == null) {
            if (!pjob.printDialog()) {
                return;
            }
        }
        try {
            pjob.print();
        } catch (PrinterException e) {
            throw new RuntimeException(e.getMessage());
        } finally {
            f.dispose();
        }
    }

    public PaintText(int id) {
        tabNumber = id;
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        System.out.println(""+pageIndex);
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(),  pf.getImageableY());
        g.drawString("ID="+tabNumber,100,20);
        g.translate(0, 25);
        paint(g);
        return PAGE_EXISTS;
    }

    public Dimension getMinimumSize() {
        return getPreferredSize();
    }

    public Dimension getPreferredSize() {
        return new Dimension(preferredSize, preferredSize);
    }

    public void paint(Graphics g) {

        
        g.setColor(Color.white);
        g.fillRect(0, 0, getSize().width, getSize().height);

        Graphics2D g2d = (Graphics2D)g;

        Font f = new Font(Font.DIALOG, Font.PLAIN, 40);
        Color c = new Color(0,0,255,96);
        Paint p = new GradientPaint(0f, 0f, Color.green,
                                    10f, 10f, Color.red,
                                    true);
        String s = "Sample Text To Paint";
        float x = 20, y= 50;

        switch (tabNumber) {
        case 1:
            g2d.setFont(f);
            g2d.setColor(c);
            g2d.drawString(s, x, y);
            break;

        case 2:
            g2d.setFont(f);
            g2d.setPaint(p);
            g2d.drawString(s, x, y);
            break;

        case 3:
            AttributedString as = new AttributedString(s);
            as.addAttribute(TextAttribute.FONT, f);
            as.addAttribute(TextAttribute.FOREGROUND, c);
            g2d.drawString(as.getIterator(), x, y);
            break;

        case 4:
            as = new AttributedString(s);
            as.addAttribute(TextAttribute.FONT, f);
            as.addAttribute(TextAttribute.FOREGROUND, p);
            g2d.drawString(as.getIterator(), x, y);
            break;

        case 5:
            as = new AttributedString(s);
            as.addAttribute(TextAttribute.FONT, f);
            as.addAttribute(TextAttribute.FOREGROUND, c);
            FontRenderContext frc = g2d.getFontRenderContext();
            TextLayout tl = new TextLayout(as.getIterator(), frc);
            tl.draw(g2d, x, y);
            break;

        case 6:
            as = new AttributedString(s);
            as.addAttribute(TextAttribute.FONT, f);
            as.addAttribute(TextAttribute.FOREGROUND, p);
            frc = g2d.getFontRenderContext();
            tl = new TextLayout(as.getIterator(), frc);
            tl.draw(g2d, x, y);
            break;

        default:
        }
    }

}
