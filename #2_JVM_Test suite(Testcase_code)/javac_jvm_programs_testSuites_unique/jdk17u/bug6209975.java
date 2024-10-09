import javax.swing.*;
import java.awt.*;
import java.awt.event.InputEvent;

public class bug6209975 {

    private static final ReturnObject RO1 = new ReturnObject();

    private static final ReturnObject RO2 = new ReturnObject();

    private static JMenu menu;

    private static JButton button;

    private static JFrame frame;

    public static void main(String[] args) throws Exception {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(500);
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    createAndShowGUI();
                }
            });
            robot.waitForIdle();
            Point clickPoint = getButtonClickPoint();
            robot.mouseMove(clickPoint.x, clickPoint.y);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            robot.waitForIdle();
            clickPoint = getMenuClickPoint();
            robot.mouseMove(clickPoint.x, clickPoint.y);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            robot.waitForIdle();
            if (RO1.itsValue <= RO2.itsValue) {
                throw new RuntimeException("Offset if the second icon is invalid.");
            }
        } finally {
            if (frame != null)
                SwingUtilities.invokeAndWait(() -> frame.dispose());
        }
    }

    private static Point getButtonClickPoint() throws Exception {
        final Point[] result = new Point[1];
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                Point p = button.getLocationOnScreen();
                Dimension size = button.getSize();
                result[0] = new Point(p.x + size.width / 2, p.y + size.height / 2);
            }
        });
        return result[0];
    }

    private static Point getMenuClickPoint() throws Exception {
        final Point[] result = new Point[1];
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                Point p = menu.getLocationOnScreen();
                Dimension size = menu.getSize();
                result[0] = new Point(p.x + size.width / 2, p.y + size.height / 2);
            }
        });
        return result[0];
    }

    private static void createAndShowGUI() {
        frame = new JFrame("Test6209975");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.applyComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        frame.setLayout(new BorderLayout());
        button = new JButton("Focus holder");
        frame.add(button);
        JMenuBar mb = new JMenuBar();
        menu = new JMenu("File");
        JMenuItem item;
        item = new JMenuItem("Just a menu item");
        item.setIcon(new MyIcon(RO1));
        item.setHorizontalTextPosition(SwingConstants.LEADING);
        menu.add(item);
        item = new JMenuItem("Menu Item with another icon");
        item.setIcon(new MyIcon(RO2));
        item.setHorizontalTextPosition(SwingConstants.TRAILING);
        menu.add(item);
        mb.add(menu);
        frame.setJMenuBar(mb);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocation(400, 300);
        frame.setVisible(true);
    }

    public static class ReturnObject {

        public volatile int itsValue;
    }

    public static class MyIcon implements Icon {

        ReturnObject thisObject = null;

        public MyIcon(ReturnObject ro) {
            super();
            thisObject = ro;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            Color color = g.getColor();
            g.setColor(Color.BLACK);
            g.fillRect(x, y, 10, 10);
            g.setColor(color);
            thisObject.itsValue = x;
        }

        public int getIconWidth() {
            return 10;
        }

        public int getIconHeight() {
            return 10;
        }
    }
}
