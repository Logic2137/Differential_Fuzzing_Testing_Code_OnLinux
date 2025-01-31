import javax.accessibility.AccessibleContext;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.plaf.basic.BasicComboPopup;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

public class bug4743225 extends JFrame {

    private static JComboBox cb;

    private static volatile boolean flag;

    public bug4743225() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());
        cb = new JComboBox(new Object[] { "one", "two", "three" });
        cb.addPopupMenuListener(new PopupMenuListener() {

            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
                cb.addItem("Test");
            }

            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
            }

            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
        add(cb);
        pack();
        setLocationRelativeTo(null);
    }

    public static BasicComboPopup getPopup() {
        AccessibleContext c = cb.getAccessibleContext();
        for (int i = 0; i < c.getAccessibleChildrenCount(); i++) {
            if (c.getAccessibleChild(i) instanceof BasicComboPopup) {
                return (BasicComboPopup) c.getAccessibleChild(i);
            }
        }
        throw new AssertionError("No BasicComboPopup found");
    }

    public static void main(String... args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(100);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                new bug4743225().setVisible(true);
            }
        });
        robot.waitForIdle();
        Point point = cb.getLocationOnScreen();
        robot.mouseMove(point.x + 10, point.y + 10);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                if (getPopup().getList().getLastVisibleIndex() == 3) {
                    flag = true;
                }
            }
        });
        if (!flag) {
            throw new RuntimeException("The ComboBox popup wasn't correctly updated");
        }
    }
}
