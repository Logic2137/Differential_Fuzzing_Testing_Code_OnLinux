



import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;

public class ReverseLandscapeTest extends Frame  {

 private TextCanvas c;

 public static void main(String args[]) {
    ReverseLandscapeTest f = new ReverseLandscapeTest();
    f.show();
 }

 public ReverseLandscapeTest() {
    super("JDK 1.2 Text Printing");

    c = new TextCanvas();
    add("Center", c);

    PrinterJob pj = PrinterJob.getPrinterJob();

    PageFormat pf = pj.defaultPage();
    pf.setOrientation(PageFormat.REVERSE_LANDSCAPE);

    













    addWindowListener(new WindowAdapter() {
       public void windowClosing(WindowEvent e) {
             System.exit(0);
            }
    });

    pack();
 }

 class TextCanvas extends Panel implements Printable {

    public int print(Graphics g, PageFormat pgFmt, int pgIndex) {
      int iw = getWidth();
      int ih = getHeight();
      Graphics2D g2d = (Graphics2D)g;

      if (pgIndex > 0)
         return Printable.NO_SUCH_PAGE;

      g2d.translate(pgFmt.getImageableX(), pgFmt.getImageableY());
      g2d.translate(iw/2, ih/2);
      g2d.setFont(new Font("Times",Font.PLAIN, 12));
      g2d.setPaint(new Color(0,0,0));
      g2d.setStroke(new BasicStroke(1f));
      g2d.drawString("Print REVERSE_LANDSCAPE", 30, 40);

      return Printable.PAGE_EXISTS;
    }

    public void paint(Graphics g) {
      g.drawString("Print REVERSE_LANDSCAPE", 30, 40);
    }

     public Dimension getPreferredSize() {
        return new Dimension(250, 100);
    }
 }

}
