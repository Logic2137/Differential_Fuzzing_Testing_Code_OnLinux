




import java.awt.*;
import java.awt.event.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.print.*;

public class PrintRotatedText extends Frame implements ActionListener {
 static String fontname="Lucida Sans Regular"; 
 private TextCanvas c;

 public static void main(String args[]) {

    PrintRotatedText f = new PrintRotatedText();
    f.show();
 }

 public PrintRotatedText() {
    super("JDK 1.2 Text Printing");

    String []fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    for (int i=0;i<fonts.length;i++) {
       if (fonts[i].equals("Times New Roman")) {
         fontname = "Times New Roman";
       }
    }
    c = new TextCanvas();
    add("Center", c);

    Button printButton = new Button("Print");
    printButton.addActionListener(this);
    add("South", printButton);

    addWindowListener(new WindowAdapter() {
       public void windowClosing(WindowEvent e) {
             System.exit(0);
            }
    });

    pack();
 }

 public void actionPerformed(ActionEvent e) {

   PrinterJob pj = PrinterJob.getPrinterJob();

   if (pj != null && pj.printDialog()) {

       pj.setPageable(c);
       try {
            pj.print();
      } catch (PrinterException pe) {
      } finally {
         System.err.println("PRINT RETURNED");
      }
   }
 }

 class TextCanvas extends Panel implements Pageable, Printable {

    public static final int MAXPAGE = 8;
    
    public static final String extra ="\u0394\u03A9ABCD";
    public String estr=extra;

    public int getNumberOfPages() {
        return MAXPAGE;
    }

    public PageFormat getPageFormat(int pageIndex) {
       if (pageIndex > MAXPAGE) throw new IndexOutOfBoundsException();
       PageFormat pf = new PageFormat();
       Paper p = pf.getPaper();
       p.setImageableArea(36, 36, p.getWidth()-72, p.getHeight()-72);
       pf.setPaper(p);



       return pf;
    }

    public Printable getPrintable(int pageIndex) {
       if (pageIndex > MAXPAGE) throw new IndexOutOfBoundsException();
       return this;
    }

    public int print(Graphics g, PageFormat pgFmt, int pgIndex) {
System.out.println("****"+pgIndex);
        double iw = pgFmt.getImageableWidth();
        double ih = pgFmt.getImageableHeight();
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pgFmt.getImageableX(), pgFmt.getImageableY());
        
        int modulo = pgIndex % 4;
        int divvy = pgIndex / 4;
        if (divvy != 0 ) {
           g2d.setFont(new Font(fontname,Font.PLAIN, 18));
           estr = "";
        } else {
           estr = extra;
        }

        int xs = 1;
        int ys = 1;

        if (modulo == 1) {
            xs = -1;
        }
        if (modulo == 2) {
            ys = -1;
        }
        if (modulo == 3) {
            xs = -1;
            ys = -1;
        }

        g2d.translate(iw*0.25, ih*0.2);
        drawTheText((Graphics2D)g2d.create(), xs*1.0,ys* 1.0);
        g2d.translate(iw*0.25, ih*0.2);
        drawTheText((Graphics2D)g2d.create(), xs*1.0,ys* 1.5);
        g2d.translate(-iw*0.2, ih*0.3);
        drawTheText((Graphics2D)g2d.create(), xs*1.5, ys*1.0);

        return Printable.PAGE_EXISTS;
    }

   private void drawTheText(Graphics2D g2d, double sx, double sy) {
      double mat[]= new double[6];

      g2d.drawOval(-75,-75,150,150);
      int degrees = 30;
      for (int i=0;i<360;i=i+degrees) {
          AffineTransform saveXfm = g2d.getTransform();
          g2d.scale(sx, sy);
          int ttype = g2d.getTransform().getType();
          String s = "ANGLE="+i;
          s +=estr;
          g2d.drawString(s, 20, 0);
          FontRenderContext frc = g2d.getFontRenderContext();
          Font f = g2d.getFont();
          Rectangle2D r2d = f.getStringBounds(s, frc);
          g2d.drawLine(20, 1, 20+(int)r2d.getWidth(), 1);
          g2d.scale(1.0/sx, 1.0/sy);
          g2d.setTransform(saveXfm);

          g2d.rotate(Math.toRadians(degrees));
      }
   }

    public void paint(Graphics g) {
      g.translate(200,200);
      g.setFont(new Font("serif", Font.PLAIN, 12));
      drawTheText((Graphics2D)g, 1.0, 1.5);
    }

     public Dimension getPreferredSize() {
        return new Dimension(400, 400);
    }
 }

}
