import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class ActionListenerExceptionTest {

    static final int TOTAL_MENU_ITEMS = 3;

    private volatile int count = 0;

    private JFrame frame;

    private JComboBox combo;

    private int menuItemHeight = 0;

    private int yPos = 0;

    private Point cbPos = null;

    private Dimension cbSize = null;

    public static void main(String[] args) throws Exception {
        EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        queue.push(new EventQueueProxy());
        ActionListenerExceptionTest testObject = new ActionListenerExceptionTest();
        testObject.createGUI();
        testObject.test();
        testObject.disposeGUI();
        if (testObject.getCount() != TOTAL_MENU_ITEMS) {
            throw new RuntimeException("ActionListener is not invoked expected number of times");
        }
    }

    private void createGUI() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                combo = new JComboBox(new String[] { "One", "Two", "Three" });
                combo.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
                        count++;
                        throw new RuntimeException();
                    }
                });
                combo.setSize(200, 20);
                frame.add(combo);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    private void disposeGUI() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                frame.dispose();
            }
        });
    }

    private void test() throws Exception {
        Robot testRobot = new Robot();
        testRobot.delay(200);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                cbPos = combo.getLocationOnScreen();
                cbSize = combo.getSize();
            }
        });
        Point center = new Point((cbPos.x + cbSize.width / 2), (cbPos.y + cbSize.height - 5));
        testRobot.mouseMove(center.x, center.y);
        testRobot.delay(100);
        testRobot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        testRobot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        testRobot.delay(500);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                Object comp = combo.getUI().getAccessibleChild(combo, 0);
                int i = 0;
                JComponent scrollPane;
                do {
                    scrollPane = (JComponent) ((JPopupMenu) comp).getComponent(i++);
                } while (!(scrollPane instanceof JScrollPane));
                menuItemHeight = scrollPane.getSize().height / TOTAL_MENU_ITEMS;
                yPos = scrollPane.getLocationOnScreen().y + menuItemHeight / 2;
            }
        });
        for (int i = 0; i < TOTAL_MENU_ITEMS; i++) {
            testRobot.mouseMove(center.x, yPos);
            testRobot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            testRobot.delay(100);
            testRobot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            testRobot.delay(200);
            yPos += menuItemHeight;
        }
    }

    private int getCount() {
        return count;
    }
}

class EventQueueProxy extends EventQueue {

    protected void dispatchEvent(AWTEvent evt) {
        try {
            super.dispatchEvent(evt);
        } catch (Exception e) {
            System.out.println("Intentionally consumed Exception from ActionListener");
            e.printStackTrace();
        }
    }
}
