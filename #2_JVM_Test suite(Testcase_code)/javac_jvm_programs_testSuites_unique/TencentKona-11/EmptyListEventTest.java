


import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class EmptyListEventTest {

    private static List list;

    public static void main(String[] args) throws Exception {

        Robot robot = new Robot();
        robot.setAutoDelay(50);

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });

        robot.waitForIdle();

        
        Point point = getClickPoint();
        robot.mouseMove(point.x, point.y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);

        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                list.requestFocusInWindow();
            }
        });

        robot.waitForIdle();

        if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner() != list) {
            throw new RuntimeException("Test failed - list isn't focus owner.");
        }

        
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.waitForIdle();

        
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        robot.waitForIdle();

        
        robot.setAutoDelay(10);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
    }

    private static Point getClickPoint() throws Exception {
        final Point[] result = new Point[1];

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                Point point = list.getLocationOnScreen();
                point.translate(list.getWidth() / 2, list.getHeight() / 2);
                result[0] = point;

            }
        });

        return result[0];


    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame();
        frame.setSize(200, 200);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        frame.getToolkit().addAWTEventListener(new AWTEventListener() {

            public void eventDispatched(AWTEvent e) {
                System.out.println(e);
            }
        }, AWTEvent.FOCUS_EVENT_MASK | AWTEvent.WINDOW_FOCUS_EVENT_MASK);


        MyListener listener = new MyListener();

        list = new List(4, true);
        list.addActionListener(listener);
        list.addItemListener(listener);

        panel.add(list);

        frame.getContentPane().add(panel);
        frame.setVisible(true);

    }

    static class MyListener implements ActionListener, ItemListener {

        public void actionPerformed(ActionEvent ae) {
            System.err.println(ae);
            throw new RuntimeException("Test failed - list is empty so event is redundant");
        }

        public void itemStateChanged(ItemEvent ie) {
            System.err.println(ie);
            throw new RuntimeException("Test failed - list is empty so event is redundant");
        }
    }
}
