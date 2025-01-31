

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.GradientPaint;
import java.awt.geom.Point2D;

import java.awt.Font;

import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;




public class TextRenderingTest {

    private static final int width  = 450;
    private static final int height = 150;

    public static void main(final String[] args) {

        GraphicsEnvironment ge =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsConfiguration gc =
            ge.getDefaultScreenDevice().getDefaultConfiguration();
        VolatileImage vi = gc.createCompatibleVolatileImage(width, height);

        while (true) {
            vi.validate(gc);
            Graphics2D g2d = vi.createGraphics();
            g2d.setColor(Color.white);
            g2d.fillRect(0, 0, width, height);

            g2d.setPaint(new GradientPaint(
                             new Point2D.Float(0, height / 2), Color.white,
                             new Point2D.Float(width, height / 2), Color.black));
            g2d.fillRect(0, 0, width, height);

            String fnt = g2d.getFont().getFamily();
            g2d.setFont(new Font(fnt, Font.PLAIN, 100));
            g2d.drawString("IIIIIIIIII", 100, 100); 

            g2d.dispose();

            if (vi.validate(gc) != VolatileImage.IMAGE_OK) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
                continue;
            }

            if (vi.contentsLost()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {}
                continue;
            }

            break;
        }

        BufferedImage bi = vi.getSnapshot();

        
        

        int prev = Integer.MAX_VALUE;
        for (int x = 0; x < width; ++x) {
            int color = bi.getRGB(x, height / 2);
            int b = color & 0xFF;

            if (b > prev) {
                throw new RuntimeException("test failed: can see the text rendered!");
            }

            prev = b;
        }
    }
}
