



import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;

public class XRenderTranslucentColorDrawTest {

    public static void main(String[] args) throws Exception {
        GraphicsEnvironment env = GraphicsEnvironment.
                getLocalGraphicsEnvironment();
        GraphicsConfiguration translucentGC = null;
        SCREENS: for (GraphicsDevice screen : env.getScreenDevices()) {
            for (GraphicsConfiguration gc : screen.getConfigurations()) {
                if (gc.isTranslucencyCapable()) {
                    translucentGC = gc;
                    break SCREENS;
                }
            }
        }
        if (translucentGC == null) {
            throw new RuntimeException("No suitable gc found.");
        }
        int width = 10;
        int height = 10;
        VolatileImage image = translucentGC.
                createCompatibleVolatileImage(width, height);
        Graphics2D g = image.createGraphics();
        
        g.setColor(new Color(0xff000000, true));
        g.fillRect(0, 0, width, height);
        
        g.setColor(new Color(0x80ffffff, true));
        g.fillRect(0, 0, width, height);
        g.dispose();
        
        BufferedImage snapshot = image.getSnapshot();
        int argb = snapshot.getRGB(width / 2, height / 2);
        
        if (!(Integer.toHexString(argb).equals("ff808080"))) {
            throw new RuntimeException("Using X Render extension for drawing"
                    + " translucent color is not giving expected results.");
        }
    }
}

