



import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.CountDownLatch;

public class WheelModifier {

    JFrame f;
    JButton fb;

    CountDownLatch pressSema = new CountDownLatch(1);
    CountDownLatch exitSema = new CountDownLatch(1);
    CountDownLatch releaseSema = new CountDownLatch(1);
    volatile CountDownLatch wheelSema;

    void createGui() {
        f = new JFrame("frame");
        fb = new JButton("frame_button");
        fb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                System.out.println("WheelModifier.mouseReleased: " + e);
                releaseSema.countDown();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                System.out.println("WheelModifier.mouseEntered: " + e);

            }

            @Override
            public void mouseExited(MouseEvent e) {
                System.out.println("WheelModifier.mouseExited: " + e);
                exitSema.countDown();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("WheelModifier.mousePressed: " + e);
                pressSema.countDown();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                System.out.println("WheelModifier.mouseDragged: " + e);
            }
        });

        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                System.out.println("WheelModifier.mouseWheel: " + event);
                wheelSema.countDown();
            }
        }, MouseEvent.MOUSE_WHEEL_EVENT_MASK);

        f.setLayout(new FlowLayout());
        f.add(fb);
        f.setSize(200, 200);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
    }

    void run() throws Exception {
        Robot r = new Robot();
        r.waitForIdle();
        System.out.println("# Started");

        Point sLoc = fb.getLocationOnScreen();
        Dimension bSize = fb.getSize();
        r.mouseMove(sLoc.x + bSize.width / 2, sLoc.y + bSize.height / 2);
        r.mousePress(MouseEvent.BUTTON1_MASK);
        pressSema.await();
        System.out.println("# Pressed");

        r.mouseMove(sLoc.x + bSize.width / 2, sLoc.y + bSize.height * 2);
        exitSema.await();
        System.out.println("# Exited");

        wheelSema = new CountDownLatch(1);
        r.mouseWheel(1);
        wheelSema.await();
        System.out.println("# Wheeled 1");

        wheelSema = new CountDownLatch(1);
        r.mouseWheel(-1);
        wheelSema.await();
        System.out.println("# Wheeled 2");

        r.mouseRelease(MouseEvent.BUTTON1_MASK);
        releaseSema.await();
        System.out.println("# Released!");
    }

    public static void main(String[] args) throws Exception {
        WheelModifier test = new WheelModifier();

        SwingUtilities.invokeAndWait(() -> test.createGui());
        test.run();

        System.out.println("Done.");
    }
}
