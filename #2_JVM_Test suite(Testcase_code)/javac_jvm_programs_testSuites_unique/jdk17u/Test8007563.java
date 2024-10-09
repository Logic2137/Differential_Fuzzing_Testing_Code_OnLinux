import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import static javax.swing.UIManager.*;
import static javax.swing.SwingUtilities.*;

public class Test8007563 implements Runnable {

    private static final ArrayList<String> LIST = new ArrayList<>();

    private static final LookAndFeelInfo[] INFO = getInstalledLookAndFeels();

    private static final CountDownLatch LATCH = new CountDownLatch(INFO.length);

    private static Robot ROBOT;

    public static void main(String[] args) throws Exception {
        ROBOT = new Robot();
        invokeLater(new Test8007563());
        LATCH.await();
        if (!LIST.isEmpty()) {
            throw new Error(LIST.toString());
        }
    }

    private static void addOpaqueError(boolean opaque) {
        LIST.add(getLookAndFeel().getName() + " opaque=" + opaque);
    }

    private static boolean updateLookAndFeel() {
        int index = (int) LATCH.getCount() - 1;
        if (index >= 0) {
            try {
                LookAndFeelInfo info = INFO[index];
                System.err.println("L&F: " + info.getName());
                setLookAndFeel(info.getClassName());
                return true;
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        return false;
    }

    private JFrame frame;

    private JTabbedPane pane;

    public void run() {
        if (this.frame == null) {
            if (!updateLookAndFeel()) {
                return;
            }
            this.pane = new JTabbedPane();
            this.pane.setOpaque(false);
            this.pane.setBackground(Color.RED);
            for (int i = 0; i < 3; i++) {
                this.pane.addTab("Tab " + i, new JLabel("Content area " + i));
            }
            this.frame = new JFrame(getClass().getSimpleName());
            this.frame.getContentPane().setBackground(Color.BLUE);
            this.frame.add(this.pane);
            this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.frame.setSize(400, 200);
            this.frame.setLocationRelativeTo(null);
            this.frame.setVisible(true);
        } else {
            Point point = new Point(this.pane.getWidth() - 2, 2);
            convertPointToScreen(point, this.pane);
            Color actual = ROBOT.getPixelColor(point.x, point.y);
            boolean opaque = this.pane.isOpaque();
            Color expected = opaque ? this.pane.getBackground() : this.frame.getContentPane().getBackground();
            if (!expected.equals(actual)) {
                addOpaqueError(opaque);
            }
            if (!opaque) {
                this.pane.setOpaque(true);
                this.pane.repaint();
            } else {
                this.frame.dispose();
                this.frame = null;
                this.pane = null;
                LATCH.countDown();
            }
        }
        SecondaryLoop secondaryLoop = Toolkit.getDefaultToolkit().getSystemEventQueue().createSecondaryLoop();
        new Thread() {

            @Override
            public void run() {
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                }
                secondaryLoop.exit();
                invokeLater(Test8007563.this);
            }
        }.start();
        secondaryLoop.enter();
    }
}
