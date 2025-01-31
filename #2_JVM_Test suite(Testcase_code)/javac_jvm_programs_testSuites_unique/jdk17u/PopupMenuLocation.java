import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Point;
import java.awt.PopupMenu;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.*;

public final class PopupMenuLocation {

    private static final int SIZE = 350;

    public static final String TEXT = "Long-long-long-long-long-long-long text in the item-";

    private static volatile boolean action = false;

    public static void main(final String[] args) throws Exception {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] sds = ge.getScreenDevices();
        for (GraphicsDevice sd : sds) {
            GraphicsConfiguration gc = sd.getDefaultConfiguration();
            Rectangle bounds = gc.getBounds();
            Point point = new Point(bounds.x, bounds.y);
            Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
            while (point.y < bounds.y + bounds.height - insets.bottom - SIZE) {
                while (point.x < bounds.x + bounds.width - insets.right - SIZE) {
                    test(point);
                    point.translate(bounds.width / 5, 0);
                }
                point.setLocation(bounds.x, point.y + bounds.height / 5);
            }
        }
    }

    private static void test(final Point tmp) throws Exception {
        PopupMenu pm = new PopupMenu();
        for (int i = 1; i < 7; i++) {
            pm.add(TEXT + i);
        }
        pm.addActionListener(e -> action = true);
        Frame frame = new Frame();
        try {
            frame.setAlwaysOnTop(true);
            frame.setLayout(new FlowLayout());
            frame.add(pm);
            frame.pack();
            frame.setSize(SIZE, SIZE);
            frame.setVisible(true);
            frame.setLocation(tmp.x, tmp.y);
            frame.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    show(e);
                }

                public void mouseReleased(MouseEvent e) {
                    show(e);
                }

                private void show(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        pm.show(frame, 0, 50);
                    }
                }
            });
            openPopup(frame);
        } finally {
            frame.dispose();
        }
    }

    private static void openPopup(final Frame frame) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(200);
        robot.waitForIdle();
        Point pt = frame.getLocationOnScreen();
        robot.mouseMove(pt.x + frame.getWidth() / 2, pt.y + 50);
        robot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        int x = pt.x + frame.getWidth() / 2;
        int y = pt.y + 130;
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
        if (!action) {
            throw new RuntimeException();
        }
        action = false;
    }
}
