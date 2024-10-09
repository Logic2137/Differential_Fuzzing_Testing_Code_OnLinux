



import java.awt.*;
import java.awt.event.*;

public class bug8013581 {
    private static Frame frame;
    private static volatile int listenerCallCounter = 0;

    public static void main(String[] args) throws Exception {
        final GraphicsEnvironment ge = GraphicsEnvironment
                .getLocalGraphicsEnvironment();
        final GraphicsDevice[] devices = ge.getScreenDevices();

        final Robot robot = new Robot();
        robot.setAutoDelay(50);

        createAndShowGUI();
        robot.waitForIdle();

        Exception error = null;
        for (final GraphicsDevice device : devices) {
            if (!device.isFullScreenSupported()) {
                continue;
            }

            device.setFullScreenWindow(frame);
            sleep(robot);

            robot.keyPress(KeyEvent.VK_A);
            robot.keyRelease(KeyEvent.VK_A);
            robot.waitForIdle();

            device.setFullScreenWindow(null);
            sleep(robot);

            if (listenerCallCounter != 2) {
                error = new Exception("Test failed: KeyListener called " + listenerCallCounter + " times instead of 2!");
                break;
            }

            listenerCallCounter = 0;
        }

        frame.dispose();

        if (error != null) {
            throw error;
        }
     }

    private static void createAndShowGUI() {
        frame = new Frame("Test");
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                listenerCallCounter++;
            }

            @Override
            public void keyReleased(KeyEvent e) {
                listenerCallCounter++;
            }
        });

        frame.setUndecorated(true);
        frame.setVisible(true);
    }

    private static void sleep(Robot robot) {
        robot.waitForIdle();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ignored) {
        }
    }
}
