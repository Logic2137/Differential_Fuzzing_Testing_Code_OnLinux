import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.SwingUtilities;

public class NofocusListDblClickTest {

    static final int EXPECTED_ACTION_COUNT = 2;

    static Robot robot;

    static final AtomicInteger actionPerformed = new AtomicInteger(0);

    static List lst;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });
        robot = new Robot();
        robot.setAutoDelay(50);
        robot.waitForIdle();
        Thread.sleep(1000);
        clickTwiceOn(lst);
        Thread.sleep(500);
        clickTwiceOn(lst);
        robot.waitForIdle();
        Thread.sleep(1000);
        synchronized (actionPerformed) {
            if (actionPerformed.get() != EXPECTED_ACTION_COUNT) {
                try {
                    actionPerformed.wait(3000);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted unexpectedly!");
                    throw new RuntimeException(e);
                }
            }
        }
        if (actionPerformed.get() != EXPECTED_ACTION_COUNT) {
            System.out.println("No ActionEvent was generated. " + actionPerformed.get());
            throw new RuntimeException("Test failed!");
        }
        System.out.println("Test passed.");
    }

    public static void createAndShowGUI() {
        Frame f = new Frame("Owner");
        Window w = new Window(f);
        lst = new List(3, true);
        f.setBounds(800, 0, 100, 100);
        w.setLocation(800, 150);
        lst.add("item 0");
        lst.add("item 1");
        lst.add("item 2");
        lst.setFocusable(false);
        lst.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println(e.toString());
                synchronized (actionPerformed) {
                    if (EXPECTED_ACTION_COUNT == actionPerformed.incrementAndGet()) {
                        actionPerformed.notifyAll();
                    }
                }
            }
        });
        w.add(lst);
        w.pack();
        f.setVisible(true);
        w.setVisible(true);
    }

    static void clickTwiceOn(Component c) throws Exception {
        Point p = c.getLocationOnScreen();
        Dimension d = c.getSize();
        if (c instanceof Frame) {
            robot.mouseMove(p.x + (int) (d.getWidth() / 2), p.y + ((Frame) c).getInsets().top / 2);
        } else {
            robot.mouseMove(p.x + (int) (d.getWidth() / 2), p.y + (int) (d.getHeight() / 2));
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        Thread.sleep(20);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }
}
