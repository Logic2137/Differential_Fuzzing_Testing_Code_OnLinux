



import java.io.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.print.*;
import java.awt.image.BufferedImage;
import javax.print.*;
import javax.print.attribute.*;

public class ClippedImages extends Frame implements ActionListener {

    private ClippedImageCanvas c;

    public static void main(String args[]) {

        ClippedImages f = new ClippedImages();
        f.setVisible(true);
    }

    public ClippedImages() {
        super("Clipped Src Area Image Printing Test");
        c = new ClippedImageCanvas();
        add("Center", c);

        Button paintButton = new Button("Toggle Contents");
        paintButton.addActionListener(this);

        Button printThisButton = new Button("Print This");
        printThisButton.addActionListener(this);

        Button printAllButton = new Button("Print All");
        printAllButton.addActionListener(this);

        Panel p = new Panel();
        p.add(paintButton);
        p.add(printThisButton);
        p.add(printAllButton);
        add("South", p);
        add("North", getInstructions());
        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

        pack();
    }

    private TextArea getInstructions() {
        TextArea ta = new TextArea(18, 60);
        ta.setFont(new Font("Dialog", Font.PLAIN, 11));
        ta.setText
            ("This is a manual test as it requires that you compare "+
             "the on-screen rendering with the printed output.\n"+
             "Select the 'Print All' button to print out the test\n"+
             "It will generate 4 sides of content: as it will print "+
             "each of 2 sets of transformed images in portrait, \n"+
             "and landscape orientations. \n"+
             "The sets of images are in turn made up\n"+
             "of two similar sets of pages: one is 'random' images,\n "+
             " the other is 16 squares.\n"+
             "Use the 'Toggle Contents' button to view the screen rendering\n"+
             "For each page compare the printed content to the same\n"+
             "on-screen one taking careful note of\n"+
             "a) the positions of the red/blue circles on the corners\n"+
             "b) that numerical text on the image is displayed similarly\n"+
             "e) that the green quadrilaterals match on-screen\n"+
             "f) that the rendering is clipped at the default (typically 1 inch) "+
             "margins of the page.\n"+
             "The test PASSES if the onscreen and printed rendering match");
        return ta;
    }

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("Print This")) {
            printOne();
        } else if (e.getActionCommand().equals("Print All")) {
            printAll();
        } else if (e.getActionCommand().equals("Toggle Contents")) {
            c.toggleContents();
            c.repaint();
        }
    }

    private void printOne() {
        PrinterJob pj = PrinterJob.getPrinterJob();

        PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
        if (pj != null && (false||pj.printDialog(attrs))) {
            c.setPrinterJob(pj, false);
            pj.setPrintable(c);
            try {
                pj.print(attrs);
            } catch (PrinterException pe) {
                pe.printStackTrace();
                throw new RuntimeException("Exception whilst printing.");
            } finally {
                System.out.println("PRINT RETURNED OK.");
            }
        }
    }

    private void printAll() {
        PrinterJob pj = PrinterJob.getPrinterJob();
        PrintRequestAttributeSet attrs = new HashPrintRequestAttributeSet();
        if (pj != null && (false||pj.printDialog(attrs))) {
            c.setPrinterJob(pj, true);
            pj.setPageable(c);
            try {
                pj.print(attrs);
            } catch (PrinterException pe) {
                pe.printStackTrace();
                throw new RuntimeException("Exception whilst printing.");
            } finally {
                System.out.println("PRINT RETURNED OK.");
            }
        }
    }
}

class ClippedImageCanvas extends Component implements Printable, Pageable {

    BufferedImage img = null;
    int sw=50, sh=50;

    ClippedImageCanvas() {
        img = new BufferedImage(sw, sh, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(Color.red);
        g2d.fillRect(0 ,0, sw, sh);
        g2d.setColor(Color.black);
        int cnt = 0;
        Font font = new Font("Serif", Font.PLAIN, 11);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        for (int y=12;y<sh;y+=12) {
            int x = 0;
            while (x < sw) {
                String s = (new Integer(++cnt)).toString();
                g2d.drawString(s, x, y);
                x+= fm.stringWidth(s);
            }
        }
    }

    private boolean paintSquares = true;
    void toggleContents() {
        paintSquares = !paintSquares;
    }

    public int getNumberOfPages() {
        if (pageable) {
            return 4;
        } else {
            return 1;
        }
    }

    boolean pageable = false;
    PrinterJob myPrinterJob;
    void setPrinterJob(PrinterJob job, boolean pageable) {
        this.myPrinterJob = job;
        this.pageable = pageable;
    }

    public PageFormat getPageFormat(int pageIndex)
        throws IndexOutOfBoundsException {

        if (pageIndex < 0 || pageIndex >= getNumberOfPages()) {
            throw new IndexOutOfBoundsException();
        }

        PageFormat pf = myPrinterJob.defaultPage();
        switch (pageIndex % 2) {

        case 0 :
            pf.setOrientation(PageFormat.PORTRAIT);
            break;

        case 1:
            pf.setOrientation(PageFormat.LANDSCAPE);
             break;
        }
        return pf;
    }

    String getOrientStr(PageFormat pf) {
        if (pf.getOrientation() == PageFormat.PORTRAIT) {
            return "Portrait Orientation, ";
        } else {
            return "Landscape Orientation,";
        }
    }

    public Printable getPrintable(int pageIndex)
        throws IndexOutOfBoundsException {

        if (pageIndex < 0 || pageIndex >= getNumberOfPages()) {
            throw new IndexOutOfBoundsException();
        }
        if (pageIndex < 2) {
            paintSquares = true;
        } else {
            paintSquares = false;
        }
        return this;
    }

    public int print(Graphics g, PageFormat pgFmt, int pgIndex) {

        if (pgIndex > getNumberOfPages()-1) {
            return Printable.NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pgFmt.getImageableX(), pgFmt.getImageableY());
        g.drawString(getOrientStr(pgFmt), 0, 12);
        paint(g2d);
        return Printable.PAGE_EXISTS;
    }

    private void drawImage(Graphics g,
                           int dx1, int dy1, int dx2, int dy2,
                           int sx1, int sy1, int sx2, int sy2) {

        int rx = (dx1 < dx2) ? dx1 : dx2;
        int ry = (dy1 < dy2) ? dy1 : dy2;
        int rw = dx2-dx1;
        if (rw < 0) rw = -rw;
        int rh = dy2-dy1;
        if (rh < 0) rh = -rh;

        g.setColor(Color.green);
        g.drawRect(rx-1 ,ry-1, rw+1, rh+1);
        g.drawImage(img, dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2, null);
        g.setColor(Color.blue);
        int r=5;
        g.drawOval(dx1-r, dy1-r, 2*r, 2*r);
        g.setColor(Color.red);
        g.drawOval(dx2-r, dy2-r, 2*r, 2*r);
    }

    private AffineTransform savedTx = null;

    private void saveTx(Graphics2D g2d) {
        savedTx = g2d.getTransform();
    }

    private void restoreTx(Graphics2D g2d) {
        g2d.setTransform(savedTx);
    }

    public void paint(Graphics g) {
        Dimension size = getSize();
        g.setColor(Color.black);
        for (int p=0;p<size.width;p+=20) {
            g.drawLine(p, 0, p, size.height);
        }
       for (int p=0;p<size.height;p+=20) {
            g.drawLine(0, p, size.width, p);
        }
        if (paintSquares) {
            paintSquares(g);
        } else {
            paintRandom(g);
        }
    }

    private void paintRandom(Graphics g) {

        int dx, dy, dw, dh;

        Graphics2D g2d = (Graphics2D)g;
        g.setColor(Color.black);

        saveTx(g2d);
        int sx = -20, sy=-20;

        dx=300; dy=10; dw=50; dh=50;

        drawImage(g, dx, dy, dx+dw, dy+dh ,sx,sy,1,1);

        dx=20; dy=20; dw=400; dh=80;
        g2d.shear(0.0, Math.PI/20);
        drawImage(g, dx, dy, dx+dw, dy+dh, sx, sy, dw/2, dh/2);

        dx=125; dy=40;
        restoreTx(g2d);

        g2d.rotate(Math.PI/4);
        drawImage(g, dx, dy, dx+dw, dy+dh, sx, sy, dw/2, dh/2);

        restoreTx(g2d);

        dx=290; dy=180; dw=20; dh=20;
        drawImage(g, dx, dy, dx+dw*10, dy+dh*10, 30, sy, dw, dh);
        g2d.scale(-1, -1);
        dx=-280; dy=-200;
        drawImage(g, dx, dy, dx+dw*2, dy+dh*2, 30, sy, dw, dh);

        restoreTx(g2d);

        g2d.scale(1, -1);
        dx=430; dy=-150;
        drawImage(g, dx, dy, dx+dw*5, dy+dh*2, 30, sy, dw, dh);

        restoreTx(g2d);

        dx = 10; dy = 290; dw = 200; dh = 200;
        drawImage(g, dx, dy, dx+dw, dy+dh, sx, sy, sw, sh);

        dx = 0; dy = 400; dw=-30; dh=-50;
        drawImage(g, dx, dy, dx-dw, dy-dh, dx, dy, dx-dw, dy-dh);
    }

    private void paintSquares(Graphics g) {

        
        int sxa = -20, sya =  -20;
        int sxb = 80, syb =  -20;
        int sxc =  -20, syc = 80;
        int sxd = 80, syd = 80;

        int dxa =  0, dya =  0;
        int dxb = 80, dyb =  0;
        int dxc =  0, dyc = 80;
        int dxd = 80, dyd = 80;

        int incX = 100;
        int incY = 100;

        g.translate(20, 20);

         
        drawImage(g, dxa, dya, dxd, dyd, sxa, sya, sxd, syd);
        g.translate(incX, 0);

        
        drawImage(g, dxa, dya, dxd, dyd, sxd, syd, sxa, sya);
        g.translate(incX, 0);

        
        drawImage(g, dxa, dya, dxd, dyd, sxb, syb, sxc, syc);
        g.translate(incX, 0);

        
        drawImage(g, dxa, dya, dxd, dyd, sxc, syc, sxb, syb);

        g.translate(-3*incX, incY);
        

        
        drawImage(g, dxd, dyd, dxa, dya, sxa, sya, sxd, syd);
        g.translate(incX, 0);

        
        drawImage(g, dxd, dyd, dxa, dya, sxd, syd, sxa, sya);
        g.translate(incX, 0);

        
        drawImage(g, dxd, dyd, dxa, dya, sxb, syb, sxc, syc);
        g.translate(incX, 0);

        
        drawImage(g, dxd, dyd, dxa, dya, sxc, syc, sxb, syb);

        g.translate(-3*incX, incY);
        

        
        drawImage(g, dxb, dyb, dxc, dyc, sxa, sya, sxd, syd);
        g.translate(incX, 0);

        
        drawImage(g, dxb, dyb, dxc, dyc, sxd, syd, sxa, sya);
        g.translate(incX, 0);

        
        drawImage(g, dxb, dyb, dxc, dyc, sxb, syb, sxc, syc);
        g.translate(incX, 0);

        
        drawImage(g, dxb, dyb, dxc, dyc, sxc, syc, sxb, syb);

        g.translate(-3*incX, incY);
        


        
        drawImage(g, dxc, dyc, dxb, dyb, sxa, sya, sxd, syd);
        g.translate(incX, 0);

        
        drawImage(g, dxc, dyc, dxb, dyb, sxd, syd, sxa, sya);
        g.translate(incX, 0);

        
        drawImage(g, dxc, dyc, dxb, dyb, sxb, syb, sxc, syc);
        g.translate(incX, 0);

        
        drawImage(g, dxc, dyc, dxb, dyb, sxc, syc, sxb, syb);
    }



     
     public Dimension getPreferredSize() {
        return new Dimension(468, 468);
    }

}
