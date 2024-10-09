



import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Robot;
import java.awt.Point;

public class ChangeWindowResizabiltyTest {
    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        for(int i = 0; i < 10; i++) {
            Dialog dialog = new Dialog((Frame) null);
            dialog.setLocation(100, 100);
            Component panel = new Panel();
            panel.setPreferredSize(new Dimension(200, 100));
            dialog.add(panel);
            dialog.pack();
            dialog.setVisible(true);
            robot.waitForIdle();
            robot.delay(200);

            Point frameLoc = dialog.getLocationOnScreen();
            Point contentLoc = panel.getLocationOnScreen();

            System.out.println("Decor location " + frameLoc);
            System.out.println("Content location " + contentLoc);

            dialog.setResizable(false);
            robot.waitForIdle();
            robot.delay(200);

            Point l = dialog.getLocationOnScreen();
            if (!l.equals(frameLoc)) {
                dialog.dispose();
                throw new RuntimeException("Decorated frame location moved " +
                        "after setResizable(false)" + l);
            }

            l = panel.getLocationOnScreen();
            if (!l.equals(contentLoc)) {
                dialog.dispose();
                throw new RuntimeException("Content location moved after " +
                        "setResizable(false)" + l);
            }

            if (panel.getLocationOnScreen().y <
                      dialog.getLocationOnScreen().y + dialog.getInsets().top) {
                dialog.dispose();
                throw new RuntimeException(
                            "Wrong content position after setResizable(false)");
            }

            dialog.setResizable(true);
            robot.waitForIdle();
            robot.delay(200);

            l = dialog.getLocationOnScreen();
            if (!l.equals(frameLoc)) {
                dialog.dispose();
                throw new RuntimeException("Decorated frame location moved " +
                        "after setResizable(true)" + l);
            }

            l = panel.getLocationOnScreen();
            if (!l.equals(contentLoc)) {
                dialog.dispose();
                throw new RuntimeException("Content location moved after " +
                        "setResizable(true)" + l);
            }
            if (panel.getLocationOnScreen().y <
                      dialog.getLocationOnScreen().y + dialog.getInsets().top) {
                dialog.dispose();
                throw new RuntimeException(
                             "Wrong content position after setResizable(true)");
            }

            dialog.dispose();
        }
    }
}
