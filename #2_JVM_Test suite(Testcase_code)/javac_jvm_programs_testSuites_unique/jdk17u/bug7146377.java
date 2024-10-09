import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class bug7146377 {

    private static JLabel label;

    private static JFrame frame;

    private static volatile Point point;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                frame = new JFrame();
                label = new JLabel("A label");
                label.addMouseListener(new MouseListener() {

                    @Override
                    public void mouseClicked(MouseEvent e) {
                        checkEvent(e);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        checkEvent(e);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        checkEvent(e);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        checkEvent(e);
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        checkEvent(e);
                    }
                });
                frame.add(label);
                frame.setSize(200, 100);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
        Robot robot = new Robot();
        robot.waitForIdle();
        Thread.sleep(1000);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                point = label.getLocationOnScreen();
            }
        });
        robot.setAutoDelay(200);
        for (int i = 0; i < 20; i++) {
            robot.mouseMove(point.x + i, point.y + i);
        }
        for (int button : new int[] { InputEvent.BUTTON1_MASK, InputEvent.BUTTON2_MASK, InputEvent.BUTTON3_MASK }) {
            robot.mouseMove(point.x, point.y);
            robot.mousePress(button);
            for (int i = 0; i < 20; i++) {
                robot.mouseMove(point.x + i, point.y + i);
            }
            robot.mouseRelease(button);
        }
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                frame.dispose();
            }
        });
        System.out.println("Test passed");
    }

    private static void checkEvent(MouseEvent e) {
        String eventAsStr = eventToString(e);
        System.out.println("Checking event " + eventAsStr);
        check("isLeftMouseButton", SwingUtilities.isLeftMouseButton(e), oldIsLeftMouseButton(e), eventAsStr);
        check("isRightMouseButton", SwingUtilities.isRightMouseButton(e), oldIsRightMouseButton(e), eventAsStr);
        check("isMiddleMouseButton", SwingUtilities.isMiddleMouseButton(e), oldIsMiddleMouseButton(e), eventAsStr);
    }

    private static void check(String methodName, boolean newValue, boolean oldValue, String eventAsStr) {
        if (newValue != oldValue) {
            throw new RuntimeException("Regression on " + methodName + ", newValue = " + newValue + ", oldValue = " + oldValue + ", e = " + eventAsStr);
        }
    }

    private static String eventToString(MouseEvent e) {
        StringBuilder result = new StringBuilder();
        switch(e.getID()) {
            case MouseEvent.MOUSE_PRESSED:
                result.append("MOUSE_PRESSED");
                break;
            case MouseEvent.MOUSE_RELEASED:
                result.append("MOUSE_RELEASED");
                break;
            case MouseEvent.MOUSE_CLICKED:
                result.append("MOUSE_CLICKED");
                break;
            case MouseEvent.MOUSE_ENTERED:
                result.append("MOUSE_ENTERED");
                break;
            case MouseEvent.MOUSE_EXITED:
                result.append("MOUSE_EXITED");
                break;
            case MouseEvent.MOUSE_MOVED:
                result.append("MOUSE_MOVED");
                break;
            case MouseEvent.MOUSE_DRAGGED:
                result.append("MOUSE_DRAGGED");
                break;
            case MouseEvent.MOUSE_WHEEL:
                result.append("MOUSE_WHEEL");
                break;
            default:
                result.append("unknown type");
        }
        result.append(", modifiers = " + MouseEvent.getMouseModifiersText(e.getModifiers()));
        result.append(", modifiersEx = " + MouseEvent.getMouseModifiersText(e.getModifiersEx()));
        result.append(", button = " + e.getButton());
        return result.toString();
    }

    private static boolean oldIsLeftMouseButton(MouseEvent e) {
        return ((e.getModifiers() & InputEvent.BUTTON1_MASK) != 0);
    }

    private static boolean oldIsMiddleMouseButton(MouseEvent e) {
        return ((e.getModifiers() & InputEvent.BUTTON2_MASK) == InputEvent.BUTTON2_MASK);
    }

    private static boolean oldIsRightMouseButton(MouseEvent e) {
        return ((e.getModifiers() & InputEvent.BUTTON3_MASK) == InputEvent.BUTTON3_MASK);
    }
}
