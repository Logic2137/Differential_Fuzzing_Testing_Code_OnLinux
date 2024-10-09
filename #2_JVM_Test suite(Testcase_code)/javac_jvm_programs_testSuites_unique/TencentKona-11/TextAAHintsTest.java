


import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

public class TextAAHintsTest extends Component {

    String black = "This text should be solid black";
    String gray  = "This text should be gray scale anti-aliased";
    String lcd   = "This text should be LCD sub-pixel text (coloured).";

    public void paint(Graphics g) {

        Graphics2D g2d = (Graphics2D)g.create();
        g2d.setColor(Color.white);
        g2d.fillRect(0,0,getSize().width, getSize().height);

        drawText(g.create(0, 0, 500, 100));
        bufferedImageText(g.create(0, 100, 500, 100));
        volatileImageText(g.create(0, 200, 500, 100));
    }

    private void drawText(Graphics g) {

        Graphics2D g2d = (Graphics2D)g;

        g2d.setColor(Color.white);
        g2d.fillRect(0,0,500,100);

        g2d.setColor(Color.black);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
        g2d.drawString(black, 10, 20);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
        g2d.drawString(black, 10, 35);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
        g2d.drawString(gray, 10, 50);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.drawString(gray, 10, 65);

        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.drawString(gray, 10, 80);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                             RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2d.drawString(lcd, 10, 95);
    }

    public void bufferedImageText(Graphics g) {
        BufferedImage bi =
                 new BufferedImage(500, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();

        drawText(g2d);
        g.drawImage(bi, 0, 0, null);
    }

    public VolatileImage getVolatileImage(int w, int h) {
        VolatileImage image;
        try {
            image = createVolatileImage(w, h, new ImageCapabilities(true));
        } catch (AWTException e) {
            System.out.println(e);
            System.out.println("Try creating non-accelerated VI instead.");
            try {
                image = createVolatileImage(w, h,
                                            new ImageCapabilities(false));
            } catch (AWTException e1) {
                System.out.println("Skipping volatile image test.");
                image = null;
            }
        }
        return image;
    }

    public void volatileImageText(Graphics g) {
        VolatileImage image = getVolatileImage(500, 100);
        if (image == null) {
            return;
        }
        boolean painted = false;
        while (!painted) {
            int status = image.validate(getGraphicsConfiguration());
            if (status == VolatileImage.IMAGE_INCOMPATIBLE) {
                image = getVolatileImage(500, 100);
                if (image == null) {
                    return;
                }
            }
            drawText(image.createGraphics());
            g.drawImage(image, 0, 0, null);
            painted = !image.contentsLost();
            System.out.println("painted = " + painted);
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(500,300);
    }

    public static void main(String[] args) throws Exception {

        Frame f = new Frame("Composite and Text Test");
        f.add(new TextAAHintsTest(), BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
    }
}
