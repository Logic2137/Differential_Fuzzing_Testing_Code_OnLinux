

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Robot;


public class MaximizedToMaximized {

    public static void main(String[] args) throws Exception {

        Frame frame = new Frame();
        Robot robot = new Robot();
        final Toolkit toolkit = Toolkit.getDefaultToolkit();
        final GraphicsEnvironment graphicsEnvironment =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        final GraphicsDevice graphicsDevice =
                graphicsEnvironment.getDefaultScreenDevice();

        final Dimension screenSize = toolkit.getScreenSize();
        final Insets screenInsets = toolkit.getScreenInsets(
                graphicsDevice.getDefaultConfiguration());

        final Rectangle availableScreenBounds = new Rectangle(screenSize);

        availableScreenBounds.x += screenInsets.left;
        availableScreenBounds.y += screenInsets.top;
        availableScreenBounds.width -= (screenInsets.left + screenInsets.right);
        availableScreenBounds.height -= (screenInsets.top + screenInsets.bottom);

        frame.setBounds(availableScreenBounds.x, availableScreenBounds.y,
                availableScreenBounds.width, availableScreenBounds.height);
        frame.setVisible(true);
        robot.waitForIdle();

        Rectangle frameBounds = frame.getBounds();
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        robot.waitForIdle();

        Rectangle maximizedFrameBounds = frame.getBounds();

        frame.dispose();
        if (maximizedFrameBounds.width < frameBounds.width
                || maximizedFrameBounds.height < frameBounds.height) {
            throw new RuntimeException("Maximized frame is smaller than non maximized");
        }
    }
}
