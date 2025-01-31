



import java.awt.*;
import java.awt.geom.*;

import static java.awt.GraphicsDevice.WindowTranslucency.*;


public class TranslucentWindow {
    public static void main(String args[]) {
        Robot robot;
        try {
            robot = new Robot();
        }catch(Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unexpected failure");
        }

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        Frame f = new Frame("Test frame");
        f.setUndecorated(true);
        f.setBounds(100, 100, 320, 240);

        
        gd.setFullScreenWindow(f);
        robot.waitForIdle();

        gd.setFullScreenWindow(null);
        robot.waitForIdle();

        
        
        if (gd.isWindowTranslucencySupported(PERPIXEL_TRANSPARENT)) {
            f.setShape(new Ellipse2D.Float(0, 0, f.getWidth(), f.getHeight()));
        }
        if (gd.isWindowTranslucencySupported(TRANSLUCENT)) {
            f.setOpacity(0.5f);
        }
        if (gd.isWindowTranslucencySupported(PERPIXEL_TRANSLUCENT)) {
            f.setBackground(new Color(0, 0, 0, 128));
        }
        gd.setFullScreenWindow(f);
        robot.waitForIdle();

        
        if (f.getShape() != null) {
            throw new RuntimeException("Test FAILED: fullscreen window shape is not null");
        }
        if (Math.abs(f.getOpacity() - 1.0f) > 1e-4) {
            throw new RuntimeException("Test FAILED: fullscreen window opacity is not 1.0f");
        }
        Color bgColor = f.getBackground();
        if ((bgColor != null) && (bgColor.getAlpha() != 255)) {
            throw new RuntimeException("Test FAILED: fullscreen window background color is not opaque");
        }

        f.dispose();
        System.out.println("Test PASSED");
    }
}
