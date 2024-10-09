


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;



public class FlipBitTest {

    static final boolean SAVE_IMAGE = false;

    public static void main(final String[] args) {

        final int size = 100;

        
        
        System.setProperty("sun.java2d.renderer.verbose", "true");

        System.out.println("FlipBitTest: size = " + size);

        final BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

        final Graphics2D g2d = (Graphics2D) image.getGraphics();
        try {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

            final AffineTransform at = new AffineTransform();
            at.setToScale(1, -1.01);
            g2d.setTransform(at);

            g2d.translate(0, -image.getHeight());
            g2d.setPaint(Color.WHITE);
            g2d.fill(new Rectangle(image.getWidth(), image.getHeight()));

            g2d.setPaint(Color.BLACK);
            g2d.setStroke(new BasicStroke(0.1f));
            g2d.draw(new Ellipse2D.Double(25, 25, 50, 50));

            if (SAVE_IMAGE) {
                try {
                    final File file = new File("FlipBitTest.png");

                    System.out.println("Writing file: " + file.getAbsolutePath());
                    ImageIO.write(image, "PNG", file);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            boolean nonWhitePixelFound = false;
            for (int x = 0; x < image.getWidth(); ++x) {
                if (image.getRGB(x, 50) != Color.WHITE.getRGB()) {
                    nonWhitePixelFound = true;
                    break;
                }
            }
            if (!nonWhitePixelFound) {
                throw new IllegalStateException("The ellipse was not drawn");
            }
        } finally {
            g2d.dispose();
        }
    }
}
