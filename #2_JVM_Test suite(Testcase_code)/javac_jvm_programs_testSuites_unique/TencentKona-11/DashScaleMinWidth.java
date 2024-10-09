

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.awt.image.VolatileImage;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;


public final class DashScaleMinWidth  {

    public static void main(final String[] args) {
        BufferedImage img = new BufferedImage(200, 40, TYPE_INT_ARGB);
        draw(img);
        validate(img);

        GraphicsConfiguration gc =
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice().getDefaultConfiguration();
        if (gc.getColorModel() instanceof IndexColorModel) {
            System.err.println("Skipping VolatileImage because of IndexColorModel");
            return;
        }

        VolatileImage vi = gc.createCompatibleVolatileImage(200, 40);
        BufferedImage snapshot;
        int attempt = 0;
        while (true) {
            if (++attempt > 10) {
                throw new RuntimeException("Too many attempts: " + attempt);
            }
            vi.validate(gc);
            draw(vi);
            snapshot = vi.getSnapshot();
            if (!vi.contentsLost()) {
                break;
            }
        }
        validate(snapshot);
    }

    private static void draw(final Image img) {
        float[] dashes = {200.0f, 200.0f};
        BasicStroke bs = new BasicStroke(20.0f,
                                         BasicStroke.CAP_BUTT,
                                         BasicStroke.JOIN_MITER,
                                         1.0f,
                                         dashes,
                                         0.0f);
        Graphics2D g = (Graphics2D) img.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, 200, 40);
        Line2D line = new Line2D.Double(400, 400, 3600, 400);
        g.setColor(Color.BLACK);
        g.scale(0.05, 0.05);
        g.setStroke(bs);
        g.draw(line);
        g.dispose();
    }

    private static void validate(final BufferedImage img) {
        int white = Color.white.getRGB();
        int point = img.getRGB(35, 20);   
        if (point != white) {
            throw new RuntimeException("Line should be dashed");
        }
    }
}
