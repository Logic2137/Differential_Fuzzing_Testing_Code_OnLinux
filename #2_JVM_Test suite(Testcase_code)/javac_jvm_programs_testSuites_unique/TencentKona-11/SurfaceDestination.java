import java.awt.Component;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Window;
import java.awt.image.BufferedImage;
import sun.java2d.SunGraphics2D;
import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;
import static java.awt.Transparency.BITMASK;
import static java.awt.Transparency.OPAQUE;
import static java.awt.Transparency.TRANSLUCENT;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public final class SurfaceDestination {

    public static void main(final String[] args) {
        final GraphicsEnvironment lge = getLocalGraphicsEnvironment();
        final GraphicsDevice dev = lge.getDefaultScreenDevice();
        final GraphicsConfiguration config = dev.getDefaultConfiguration();
        test(config.createCompatibleImage(10, 10).getGraphics());
        test(config.createCompatibleImage(10, 10, OPAQUE).getGraphics());
        test(config.createCompatibleImage(10, 10, BITMASK).getGraphics());
        test(config.createCompatibleImage(10, 10, TRANSLUCENT).getGraphics());
        test(new BufferedImage(10, 10, TYPE_INT_ARGB).getGraphics());
        final Window frame = new Frame();
        frame.pack();
        try {
            test(frame.getGraphics());
            test(frame.createImage(10, 10).getGraphics());
        } finally {
            frame.dispose();
        }
    }

    private static void test(final Graphics graphics) {
        try {
            if (graphics instanceof SunGraphics2D) {
                final Object dst = ((SunGraphics2D) graphics).getDestination();
                if (!(dst instanceof Image) && !(dst instanceof Component)) {
                    throw new RuntimeException("Wrong type:" + dst);
                }
            }
        } finally {
            graphics.dispose();
        }
    }
}
