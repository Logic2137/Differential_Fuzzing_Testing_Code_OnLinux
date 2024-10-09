

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class IconifyTest {
    private static volatile boolean windowIconifiedIsCalled = false;
    private static volatile boolean frameIsRepainted = false;
    static JFrame frame;
    static JButton button;

    public static void main(String[] args) throws Throwable {
        Robot robot = new Robot();
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                frame = new JFrame();
                button = new JButton("HI");
                frame.getContentPane().add(button);
                frame.addWindowListener(new WindowAdapter() {
                    public void windowIconified(WindowEvent e) {
                        windowIconifiedIsCalled = true;
                        RepaintManager rm = RepaintManager.currentManager(null);
                        rm.paintDirtyRegions();
                        button.repaint();
                        if (!rm.getDirtyRegion(button).isEmpty()) {
                            frameIsRepainted = true;
                        }
                    }
                });
                frame.pack();
                frame.setVisible(true);
            }
        });
        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                frame.setExtendedState(Frame.ICONIFIED);
            }
        });
        robot.waitForIdle();

        if (!windowIconifiedIsCalled) {
            throw new Exception("Test failed: window was not iconified.");
        }
        if (frameIsRepainted) {
            throw new Exception("Test failed: frame was repainted when window was iconified.");
        }
    }
}
