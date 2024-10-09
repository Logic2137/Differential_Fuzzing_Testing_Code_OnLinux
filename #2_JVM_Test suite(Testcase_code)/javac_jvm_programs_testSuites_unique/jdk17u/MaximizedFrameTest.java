import java.awt.AWTException;
import java.awt.Component;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class MaximizedFrameTest {

    final static int ITERATIONS_COUNT = 5;

    private static JFrame frame;

    private static Point tempMousePosition;

    private static Component titleComponent;

    public void init() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            throw new RuntimeException("Test Failed. MetalLookAndFeel not set " + "for frame");
        }
        frame = new JFrame("JFrame Maximization Test");
        frame.pack();
        frame.setSize(450, 260);
        frame.setVisible(true);
    }

    public void getTitleComponent() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                JLayeredPane lPane = frame.getLayeredPane();
                boolean titleFound = false;
                for (int j = 0; j < lPane.getComponentsInLayer(JLayeredPane.FRAME_CONTENT_LAYER.intValue()).length; j++) {
                    titleComponent = lPane.getComponentsInLayer(JLayeredPane.FRAME_CONTENT_LAYER.intValue())[j];
                    if (titleComponent.getClass().getName().equals("javax.swing.plaf.metal.MetalTitlePane")) {
                        titleFound = true;
                        break;
                    }
                }
                if (!titleFound) {
                    try {
                        dispose();
                    } catch (Exception ex) {
                        Logger.getLogger(MaximizedFrameTest.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    throw new RuntimeException("Test Failed. Unable to " + "determine title component");
                }
            }
        });
    }

    public void doMaximizeFrameTest() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                Point framePosition = frame.getLocationOnScreen();
                tempMousePosition = new Point(framePosition.x + frame.getWidth() / 2, framePosition.y + titleComponent.getHeight() / 2);
            }
        });
        try {
            Robot robot = new Robot();
            robot.mouseMove(tempMousePosition.x, tempMousePosition.y);
            robot.waitForIdle();
            for (int iteration = 0; iteration < ITERATIONS_COUNT; iteration++) {
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.waitForIdle();
                tempMousePosition.x += 5;
                robot.mouseMove(tempMousePosition.x, tempMousePosition.y);
                robot.waitForIdle();
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                robot.waitForIdle();
                if (frame.getExtendedState() != 0) {
                    dispose();
                    throw new RuntimeException("Test failed. JFrame was " + "maximized. ExtendedState is : " + frame.getExtendedState());
                }
            }
        } catch (AWTException e) {
            dispose();
            throw new RuntimeException("Test Failed. AWTException thrown.");
        }
        System.out.println("Test passed.");
    }

    private void dispose() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                if (null != frame) {
                    frame.dispose();
                }
            }
        });
    }

    public static void main(String[] args) throws Exception {
        MaximizedFrameTest maximizedFrameTest = new MaximizedFrameTest();
        maximizedFrameTest.init();
        maximizedFrameTest.getTitleComponent();
        maximizedFrameTest.doMaximizeFrameTest();
        maximizedFrameTest.dispose();
    }
}
