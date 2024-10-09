



import java.awt.Dimension;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

public class Test7163696 implements Runnable {

    private static final boolean AUTO = null != System.getProperty("test.src", null);

    public static void main(String[] args) throws Exception {
        new Test7163696().test();
    }

    private JScrollBar bar;

    private void test() throws Exception {
        Robot robot = new Robot();
        for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            UIManager.setLookAndFeel(info.getClassName());

            SwingUtilities.invokeAndWait(this);
            robot.waitForIdle(); 
            Thread.sleep(1000);

            Point point = this.bar.getLocation();
            SwingUtilities.convertPointToScreen(point, this.bar);
            point.x += this.bar.getWidth() >> 2;
            point.y += this.bar.getHeight() >> 1;
            robot.mouseMove(point.x, point.y);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);

            robot.waitForIdle(); 
            Thread.sleep(1000);
            SwingUtilities.invokeAndWait(this);

            if (this.bar != null) {
                this.bar = null; 
                if (AUTO) { 
                    throw new Error("TEST FAILED");
                }
            }
        }
    }

    public void run() {
        if (this.bar == null) {
            this.bar = new JScrollBar(JScrollBar.HORIZONTAL, 50, 10, 0, 100);
            this.bar.setPreferredSize(new Dimension(400, 20));

            JFrame frame = new JFrame();
            frame.add(this.bar);
            frame.pack();
            frame.setVisible(true);
        }
        else if (40 != this.bar.getValue()) {
            System.out.println("name = " + UIManager.getLookAndFeel().getName());
            System.out.println("value = " + this.bar.getValue());
        }
        else {
            SwingUtilities.getWindowAncestor(this.bar).dispose();
            this.bar = null;
        }
    }
}
