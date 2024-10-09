import javax.swing.*;
import java.awt.*;

public class bug6495408 {

    static JTabbedPane tabbedPane;

    public static void main(String[] args) throws Exception {
        final Robot robot = new Robot();
        robot.setAutoDelay(50);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                final JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                tabbedPane = new JTabbedPane();
                tabbedPane.setTabPlacement(JTabbedPane.LEFT);
                tabbedPane.addTab("Hello", null);
                frame.add(tabbedPane);
                frame.setSize(400, 400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
        robot.waitForIdle();
        final Rectangle d = new Rectangle();
        final Point p = new Point();
        for (int i = 0; i < 7; i++) {
            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    int tab = tabbedPane.getTabCount() - 1;
                    Rectangle bounds = tabbedPane.getBoundsAt(tab);
                    if (bounds != null) {
                        d.setBounds(bounds);
                        p.setLocation(d.x + d.width / 2, d.y + d.height / 2);
                        SwingUtilities.convertPointToScreen(p, tabbedPane);
                        robot.mouseMove(p.x, p.y + d.height);
                        tabbedPane.addTab("Hello", null);
                    }
                }
            });
        }
    }
}
