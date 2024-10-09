



import java.awt.Component;
import java.awt.Event;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class bug7088744 {

    private static volatile JLabel label;
    private static volatile JFrame frame;

    private static volatile Point point = new Point();

    private static final int MOUSE_CLICKED = 1;
    private static final int MOUSE_PRESSED = 2;
    private static final int MOUSE_RELEASED = 3;

    
    private static final int[][] BUTTON_EVENTS_SEQUENCE = {
            {MOUSE_PRESSED, 1},
            {MOUSE_PRESSED, 2},
            {MOUSE_PRESSED, 3},
            {MOUSE_RELEASED, 1},
            {MOUSE_CLICKED, 1},
            {MOUSE_RELEASED, 2},
            {MOUSE_CLICKED, 2},
            {MOUSE_RELEASED, 3},
            {MOUSE_CLICKED, 3}
    };

    private static int eventCount;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                Component source = new JLabel();

                MouseEvent mouseEventNoButtons = new MouseEvent(source, 0, System.currentTimeMillis(),
                        Event.ALT_MASK | Event.META_MASK | InputEvent.ALT_DOWN_MASK | InputEvent.META_DOWN_MASK,
                        0, 0, 0, false, MouseEvent.NOBUTTON);

                
                if (SwingUtilities.isLeftMouseButton(mouseEventNoButtons)) {
                    throw new RuntimeException("SwingUtilities.isLeftMouseButton fails 1");
                }

                if (!SwingUtilities.isLeftMouseButton(new MouseEvent(source, 0, System.currentTimeMillis(),
                        InputEvent.BUTTON1_MASK, 0, 0, 1, false, MouseEvent.BUTTON1))) {
                    throw new RuntimeException("SwingUtilities.isLeftMouseButton fails 2");
                }

                if (!SwingUtilities.isLeftMouseButton(new MouseEvent(source, 0, System.currentTimeMillis(),
                        InputEvent.BUTTON1_DOWN_MASK, 0, 0, 1, false, MouseEvent.BUTTON1))) {
                    throw new RuntimeException("SwingUtilities.isLeftMouseButton fails 3");
                }

                
                if (SwingUtilities.isMiddleMouseButton(mouseEventNoButtons)) {
                    throw new RuntimeException("SwingUtilities.isMiddleMouseButton fails 1");
                }

                if (!SwingUtilities.isMiddleMouseButton(new MouseEvent(source, 0, System.currentTimeMillis(),
                        InputEvent.BUTTON2_MASK, 0, 0, 1, false, MouseEvent.BUTTON2))) {
                    throw new RuntimeException("SwingUtilities.isMiddleMouseButton fails 2");
                }

                if (!SwingUtilities.isMiddleMouseButton(new MouseEvent(source, 0, System.currentTimeMillis(),
                        InputEvent.BUTTON2_DOWN_MASK, 0, 0, 1, false, MouseEvent.BUTTON2))) {
                    throw new RuntimeException("SwingUtilities.isMiddleMouseButton fails 3");
                }

                
                if (SwingUtilities.isRightMouseButton(mouseEventNoButtons)) {
                    throw new RuntimeException("SwingUtilities.isRightMouseButton fails 1");
                }

                if (!SwingUtilities.isRightMouseButton(new MouseEvent(source, 0, System.currentTimeMillis(),
                        InputEvent.BUTTON3_MASK, 0, 0, 1, false, MouseEvent.BUTTON3))) {
                    throw new RuntimeException("SwingUtilities.isRightMouseButton fails 2");
                }

                if (!SwingUtilities.isRightMouseButton(new MouseEvent(source, 0, System.currentTimeMillis(),
                        InputEvent.BUTTON3_DOWN_MASK, 0, 0, 1, false, MouseEvent.BUTTON3))) {
                    throw new RuntimeException("SwingUtilities.isRightMouseButton fails 3");
                }
            }
        });

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                frame = new JFrame();

                label = new JLabel("A label");

                label.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        processEvent(MOUSE_CLICKED, e);
                    }

                    public void mousePressed(MouseEvent e) {
                        processEvent(MOUSE_PRESSED, e);
                    }

                    public void mouseReleased(MouseEvent e) {
                        processEvent(MOUSE_RELEASED, e);
                    }
                });
                frame.add(label);
                frame.setSize(200, 100);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });

        Robot robot = new Robot();
        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                Point pt = label.getLocationOnScreen();
                point.x = pt.x + label.getWidth() / 2;
                point.y = pt.y + label.getHeight() / 2;
            }
        });

        robot.setAutoDelay(100);
        robot.setAutoWaitForIdle(true);
        robot.mouseMove(point.x, point.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mousePress(InputEvent.BUTTON2_MASK);
        robot.mousePress(InputEvent.BUTTON3_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON2_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_MASK);

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                frame.dispose();
                if (eventCount != BUTTON_EVENTS_SEQUENCE.length) {
                    throw new RuntimeException("Not all events received");
                }

            }
        });

        System.out.println("Test passed");
    }

    private static void processEvent(int eventType, MouseEvent e) {
        if (eventCount >= BUTTON_EVENTS_SEQUENCE.length) {
            throw new RuntimeException("Unexpected event " + e);
        }

        int[] arr = BUTTON_EVENTS_SEQUENCE[eventCount];

        if (arr[0] != eventType) {
            throw new RuntimeException("Unexpected eventType " + eventType + "on step " + eventCount);
        }

        boolean result;

        switch (arr[1]) {
            case 1:
                result = SwingUtilities.isLeftMouseButton(e);

                break;

            case 2:
                result = SwingUtilities.isMiddleMouseButton(e);

                break;

            case 3:
                result = SwingUtilities.isRightMouseButton(e);

                break;

            default:
                throw new RuntimeException("Incorrect arr[1] on step " + eventCount);
        }

        if (!result) {
            throw new RuntimeException("Test failed on step " + eventCount);
        }

        eventCount++;
    }
}
