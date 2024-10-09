import java.awt.AWTEvent;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AWTEventListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;
import javax.swing.event.MenuDragMouseEvent;
import javax.swing.event.MenuDragMouseListener;

public class MenuDragEvents {

    boolean mouseDragged = false;

    boolean mouseEntered = false;

    boolean mouseReleased = false;

    boolean actionReceived = false;

    public static void main(String[] args) {
        MenuDragEvents test = new MenuDragEvents();
        test.doTest();
    }

    public void doTest() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

            public void eventDispatched(AWTEvent event) {
                int id = event.getID();
                if (id == MouseEvent.MOUSE_ENTERED || id == MouseEvent.MOUSE_EXITED) {
                    System.err.println(event);
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK);
        JMenuBar mb = new JMenuBar();
        JMenu m = new JMenu("A Menu");
        mb.add(m);
        JMenuItem i = new JMenuItem("A menu item", KeyEvent.VK_A);
        m.add(i);
        m = new JMenu("Another Menu");
        mb.add(m);
        i = new JMenuItem("Yet another menu item", KeyEvent.VK_Y);
        m.add(i);
        i.addMenuDragMouseListener(new MenuDragMouseListener() {

            public void menuDragMouseDragged(MenuDragMouseEvent e) {
                System.err.println(e);
                mouseDragged = true;
            }

            public void menuDragMouseEntered(MenuDragMouseEvent e) {
                System.err.println(e);
                mouseEntered = true;
            }

            public void menuDragMouseReleased(MenuDragMouseEvent e) {
                System.err.println(e);
                mouseReleased = true;
            }

            public void menuDragMouseExited(MenuDragMouseEvent e) {
                System.err.println(e);
            }
        });
        i.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                System.err.println(ae);
                actionReceived = true;
            }
        });
        JFrame frame = new JFrame("Menu");
        frame.setLayout(new BorderLayout());
        frame.setJMenuBar(mb);
        frame.setSize(200, 200);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        Robot r = null;
        try {
            r = new Robot();
            r.setAutoDelay(50);
        } catch (AWTException ae) {
            throw new RuntimeException(ae);
        }
        r.waitForIdle();
        Point loc = m.getLocationOnScreen();
        loc.x += m.getWidth() / 2;
        loc.y += m.getHeight() / 2;
        r.mouseMove(loc.x, loc.y);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.waitForIdle();
        r.delay(1000);
        Point loc2 = i.getLocationOnScreen();
        loc2.x += i.getWidth() / 2;
        loc2.y += i.getHeight() / 2;
        dragMouse(r, loc, loc2);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
        r.waitForIdle();
        r.delay(1000);
        if (!mouseEntered || !mouseDragged || !mouseReleased || !actionReceived) {
            throw new RuntimeException("we expected to receive both mouseEntered and MouseDragged (" + mouseEntered + ", " + mouseDragged + ", " + mouseReleased + ", " + actionReceived + ")");
        }
        System.out.println("Test passed");
        frame.dispose();
    }

    void dragMouse(Robot r, Point from, Point to) {
        final int n_step = 10;
        int step_x = (to.x - from.x) / n_step;
        int step_y = (to.y - from.y) / n_step;
        int x = from.x;
        int y = from.y;
        for (int idx = 0; idx < n_step; idx++) {
            x += step_x;
            y += step_y;
            r.mouseMove(x, y);
            r.delay(10);
        }
        if (x != to.x || y != to.y) {
            r.mouseMove(to.x, to.y);
            r.delay(10);
        }
    }
}
