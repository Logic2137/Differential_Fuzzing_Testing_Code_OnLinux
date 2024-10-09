import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.print.*;

public class PrintVolatileImage extends Component implements ActionListener, Printable {

    VolatileImage vimg = null;

    public static void main(String[] args) {
        Frame f = new Frame();
        PrintVolatileImage pvi = new PrintVolatileImage();
        f.add("Center", pvi);
        Button b = new Button("Print");
        b.addActionListener(pvi);
        f.add("South", b);
        f.pack();
        f.show();
    }

    public PrintVolatileImage() {
    }

    public Dimension getPreferredSize() {
        return new Dimension(100, 100);
    }

    public void paint(Graphics g) {
        if (vimg == null) {
            vimg = createVolatileImage(100, 100);
            Graphics ig = vimg.getGraphics();
            ig.setColor(Color.white);
            ig.fillRect(0, 0, 100, 100);
            ig.setColor(Color.black);
            ig.drawLine(0, 0, 100, 100);
            ig.drawLine(100, 0, 0, 100);
        }
        g.drawImage(vimg, 0, 0, null);
    }

    public void actionPerformed(ActionEvent e) {
        PrinterJob pj = PrinterJob.getPrinterJob();
        if (pj != null && pj.printDialog()) {
            pj.setPrintable(this);
            try {
                pj.print();
            } catch (PrinterException pe) {
            } finally {
                System.err.println("PRINT RETURNED");
            }
        }
    }

    public int print(Graphics g, PageFormat pgFmt, int pgIndex) {
        if (pgIndex > 0)
            return Printable.NO_SUCH_PAGE;
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(pgFmt.getImageableX(), pgFmt.getImageableY());
        paint(g);
        return Printable.PAGE_EXISTS;
    }
}
