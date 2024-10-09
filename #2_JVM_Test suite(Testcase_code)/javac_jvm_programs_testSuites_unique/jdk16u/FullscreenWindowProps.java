

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;


public final class FullscreenWindowProps {

    public static void main(String[] args) throws Exception {
        var ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (!gd.isFullScreenSupported()) {
            return;
        }

        Frame frame = new Frame() {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                g.setColor(Color.GREEN);
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        try {
            frame.setUndecorated(true); 
            frame.setBackground(Color.MAGENTA);
            frame.setVisible(true);
            gd.setFullScreenWindow(frame);
            Thread.sleep(4000);

            for (DisplayMode dm : gd.getDisplayModes()) {
                if (dm.getWidth() == 1024 && dm.getHeight() == 768) {
                    gd.setDisplayMode(dm);
                    Thread.sleep(4000);
                    break;
                }
            }

            GraphicsConfiguration frameGC = frame.getGraphicsConfiguration();
            Rectangle frameBounds = frame.getBounds();

            GraphicsConfiguration screenGC = gd.getDefaultConfiguration();
            Rectangle screenBounds = screenGC.getBounds();

            if (frameGC != screenGC) {
                System.err.println("Expected: " + screenGC);
                System.err.println("Actual: " + frameGC);
                throw new RuntimeException();
            }

            checkSize(frameBounds.x, screenBounds.x, "x");
            checkSize(frameBounds.y, screenBounds.y, "Y");
            checkSize(frameBounds.width, screenBounds.width, "width");
            checkSize(frameBounds.height, screenBounds.height, "height");
        } finally {
            gd.setFullScreenWindow(null);
            frame.dispose();
            Thread.sleep(10000);
        }
    }

    private static void checkSize(int actual, int expected, String prop) {
        if (Math.abs(actual - expected) > 30) { 
                                                
            System.err.println("Expected: " + expected);
            System.err.println("Actual: " + actual);
            throw new RuntimeException(prop + " is wrong");
        }
    }
}
