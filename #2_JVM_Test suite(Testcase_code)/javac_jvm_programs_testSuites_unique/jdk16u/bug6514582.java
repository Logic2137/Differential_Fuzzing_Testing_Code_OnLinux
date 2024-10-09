



import javax.swing.*;
import java.awt.*;

public class bug6514582 {
    private static JPopupMenu popup;

    public static void main(String ...args) throws Exception {
        Robot robot = new Robot();

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                popup = new JPopupMenu();
                popup.add(new JMenuItem("item"));
                popup.setVisible(true);

            }
        });

        robot.waitForIdle();

        if (!popup.isShowing()) {
            throw new RuntimeException("Where is my popup ?");
        }

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                popup.setVisible(false);
                popup.removeAll();
                popup.setVisible(true);
            }
        });

        robot.waitForIdle();

        if (popup.isShowing()) {
            throw new RuntimeException("Empty popup is shown");
        }

        popup.setVisible(false);
    }
}
