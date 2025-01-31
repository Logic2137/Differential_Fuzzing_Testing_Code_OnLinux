import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class bug4962534 extends Applet {

    Robot robot;

    volatile Point framePosition;

    volatile Point newFrameLocation;

    static JFrame frame;

    Rectangle gcBounds;

    Component titleComponent;

    JLayeredPane lPane;

    volatile boolean titleFound = false;

    public static Object LOCK = new Object();

    public static void main(final String[] args) throws Exception {
        try {
            bug4962534 app = new bug4962534();
            app.init();
            app.start();
        } finally {
            if (frame != null)
                SwingUtilities.invokeAndWait(() -> frame.dispose());
        }
    }

    @Override
    public void init() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    createAndShowGUI();
                }
            });
        } catch (Exception ex) {
            throw new RuntimeException("Init failed. " + ex.getMessage());
        }
    }

    @Override
    public void start() {
        validate();
        try {
            setJLayeredPaneEDT();
            setTitleComponentEDT();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Test failed. " + ex.getMessage());
        }
        if (!titleFound) {
            throw new RuntimeException("Test Failed. Unable to determine title's size.");
        }
        Random r = new Random();
        for (int iteration = 0; iteration < 10; iteration++) {
            try {
                setFramePosEDT();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Test failed.");
            }
            try {
                robot = new Robot();
                robot.setAutoDelay(70);
                robot.waitForIdle();
                robot.mouseMove(framePosition.x + getJFrameWidthEDT() / 2, framePosition.y + titleComponent.getHeight() / 2);
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.waitForIdle();
                gcBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0].getConfigurations()[0].getBounds();
                robot.mouseMove(framePosition.x + getJFrameWidthEDT() / 2, framePosition.y + titleComponent.getHeight() / 2);
                robot.waitForIdle();
                int multier = gcBounds.height / 2 - 10;
                for (int i = 0; i < 10; i++) {
                    robot.mouseMove(gcBounds.width / 2 - (int) (r.nextDouble() * multier), gcBounds.height / 2 - (int) (r.nextDouble() * multier));
                }
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                robot.waitForIdle();
            } catch (AWTException e) {
                throw new RuntimeException("Test Failed. AWTException thrown." + e.getMessage());
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Test Failed.");
            }
            System.out.println("Mouse  lies in " + MouseInfo.getPointerInfo().getLocation());
            boolean frameIsOutOfScreen = false;
            try {
                setNewFrameLocationEDT();
                System.out.println("Now Frame lies in " + newFrameLocation);
                frameIsOutOfScreen = checkFrameIsOutOfScreenEDT();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Test Failed.");
            }
            if (frameIsOutOfScreen) {
                throw new RuntimeException("Test failed. JFrame is out of screen.");
            }
        }
        System.out.println("Test passed.");
    }

    private void createAndShowGUI() {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        JFrame.setDefaultLookAndFeelDecorated(true);
        frame = new JFrame("JFrame Dance Test");
        frame.pack();
        frame.setSize(450, 260);
        frame.setVisible(true);
    }

    private void setJLayeredPaneEDT() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                lPane = frame.getLayeredPane();
                System.out.println("JFrame's LayeredPane " + lPane);
            }
        });
    }

    private void setTitleComponentEDT() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                for (int j = 0; j < lPane.getComponentsInLayer(JLayeredPane.FRAME_CONTENT_LAYER.intValue()).length; j++) {
                    titleComponent = lPane.getComponentsInLayer(JLayeredPane.FRAME_CONTENT_LAYER.intValue())[j];
                    if (titleComponent.getClass().getName().equals("javax.swing.plaf.metal.MetalTitlePane")) {
                        titleFound = true;
                        break;
                    }
                }
            }
        });
    }

    private void setFramePosEDT() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                framePosition = frame.getLocationOnScreen();
            }
        });
    }

    private boolean checkFrameIsOutOfScreenEDT() throws Exception {
        final boolean[] result = new boolean[1];
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                if (newFrameLocation.x > gcBounds.width || newFrameLocation.x < 0 || newFrameLocation.y > gcBounds.height || newFrameLocation.y < 0) {
                    result[0] = true;
                }
            }
        });
        return result[0];
    }

    private void setNewFrameLocationEDT() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                newFrameLocation = new Point(frame.getLocationOnScreen().x + frame.getWidth() / 2, frame.getLocationOnScreen().y + titleComponent.getHeight() / 2);
            }
        });
    }

    private int getJFrameWidthEDT() throws Exception {
        final int[] result = new int[1];
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                result[0] = frame.getWidth();
            }
        });
        return result[0];
    }
}
