



import javax.swing.*;
import java.awt.*;

public class ChildAlwaysOnTopTest {

    private static Window win1;
    private static Window win2;
    private static Point point;

    public static void main(String[] args) throws Exception {
        if( Toolkit.getDefaultToolkit().isAlwaysOnTopSupported() ) {


            test(null);

            Window f = new Frame();
            f.setBackground(Color.darkGray);
            f.setSize(500, 500);
            try {
                test(f);
            } finally {
                f.dispose();
            }

            f = new Frame();
            f.setBackground(Color.darkGray);
            f.setSize(500, 500);
            f.setVisible(true);
            f = new Dialog((Frame)f);
            try {
                test(f);
            } finally {
                ((Frame)f.getParent()).dispose();
            }
        }
        System.out.println("ok");
    }

    public static void test(Window parent) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                win1 = parent == null ? new JDialog() : new JDialog(parent);
                win1.setName("top");
                win2 = parent == null ? new JDialog() : new JDialog(parent);
                win2.setName("behind");
                win1.setSize(200, 200);
                Panel panel = new Panel();
                panel.setBackground(Color.GREEN);
                win1.add(panel);
                panel = new Panel();
                panel.setBackground(Color.RED);
                win2.add(panel);
                win1.setAlwaysOnTop(true);
                win2.setAlwaysOnTop(false);
                win1.setVisible(true);
            }
        });

        Robot robot = new Robot();
        robot.delay(500);
        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                point = win1.getLocationOnScreen();
                win2.setBounds(win1.getBounds());
                win2.setVisible(true);
            }
        });

        robot.delay(500);
        robot.waitForIdle();

        Color color = robot.getPixelColor(point.x + 100, point.y + 100);
        if(!color.equals(Color.GREEN)) {
            win1.dispose();
            win2.dispose();
            throw new RuntimeException("alawaysOnTop window is sent back by " +
                    "another child window setVisible(). " + color);
        }

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                win2.toFront();
                if (parent != null) {
                    parent.setLocation(win1.getLocation());
                    parent.toFront();
                }
            }
        });

        robot.delay(500);
        robot.waitForIdle();

        color = robot.getPixelColor(point.x + 100, point.y + 100);
        if(!color.equals(Color.GREEN)) {
            win1.dispose();
            win2.dispose();
            throw new RuntimeException("alawaysOnTop window is sent back by " +
                    "another child window toFront(). " + color);
        }

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                win1.setAlwaysOnTop(false);
                if (parent != null) {
                    parent.setVisible(false);
                    parent.setVisible(true);
                }
                win2.toFront();
            }
        });

        robot.delay(500);
        robot.waitForIdle();

        color = robot.getPixelColor(point.x + 100, point.y + 100);
        if(!color.equals(Color.RED)) {
            throw new RuntimeException("Failed to unset alawaysOnTop " + color);
        }

        win1.dispose();
        win2.dispose();
    }
}
