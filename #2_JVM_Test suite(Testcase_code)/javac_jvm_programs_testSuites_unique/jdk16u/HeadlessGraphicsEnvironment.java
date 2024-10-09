

import java.awt.*;
import java.awt.image.BufferedImage;



public class HeadlessGraphicsEnvironment {
    public static void main(String args[]) {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (!GraphicsEnvironment.isHeadless())
            throw new RuntimeException("GraphicsEnvironment.isHeadless says it's not headless mode when it is");

        boolean exceptions = false;
        try {
            GraphicsDevice[] gdl = ge.getScreenDevices();
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when excepted");

        exceptions = false;
        try {
            GraphicsDevice gdl = ge.getDefaultScreenDevice();
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when excepted");

        Graphics2D gd = ge.createGraphics(new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR));

        for (Font font : ge.getAllFonts()) {
            for (float j = 8; j < 17; j++) {
                Font f1 = font.deriveFont(Font.PLAIN, j);
                Font f2 = font.deriveFont(Font.BOLD, j);
                Font f3 = font.deriveFont(Font.ITALIC, j);
                Font f4 = font.deriveFont(Font.BOLD | Font.ITALIC, j);

                f1.hasUniformLineMetrics();
                f2.hasUniformLineMetrics();
                f3.hasUniformLineMetrics();
                f4.hasUniformLineMetrics();
            }
        }

        String[] fNames = ge.getAvailableFontFamilyNames();
    }
}
