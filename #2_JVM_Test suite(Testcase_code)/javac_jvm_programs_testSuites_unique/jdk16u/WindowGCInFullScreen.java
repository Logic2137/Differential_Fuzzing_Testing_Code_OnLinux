


import java.awt.Color;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;


public class WindowGCInFullScreen {

    public static void main(final String[] args)
            throws InvocationTargetException, InterruptedException {
        SwingUtilities.invokeAndWait(() -> {
            final GraphicsDevice[] devices =
                    GraphicsEnvironment.getLocalGraphicsEnvironment()
                                       .getScreenDevices();
            final Frame frame = new Frame();
            frame.setBackground(Color.GREEN);
            frame.setUndecorated(true);
            frame.setSize(100, 100);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            sleep();
            for (final GraphicsDevice gd : devices) {
                try {
                    gd.setFullScreenWindow(frame);
                    if (gd.getFullScreenWindow() != frame) {
                        throw new RuntimeException("Wrong window");
                    }
                    if (frame.getGraphicsConfiguration().getDevice() != gd) {
                        throw new RuntimeException("Wrong new GraphicsDevice");
                    }
                } finally {
                    
                    gd.setFullScreenWindow(null);
                }
            }
            frame.dispose();
        });
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {
        }
    }
}
