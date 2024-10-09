

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;


public class bug4361477 {

    static JTabbedPane tabbedPane;
    volatile static boolean bStateChanged = false;
    volatile static Rectangle bounds;

    public static void main(String args[]) throws Exception {

        Robot robot = new Robot();
        robot.setAutoDelay(50);

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                createAndShowUI();
            }
        });

        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                bounds = tabbedPane.getUI().getTabBounds(tabbedPane, 0);
            }
        });

        Point location = bounds.getLocation();
        SwingUtilities.convertPointToScreen(location, tabbedPane);
        robot.mouseMove(location.x + 1, location.y + 1);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);

        if (!bStateChanged) {
            throw new RuntimeException("Tabbed pane state is not changed");
        }
    }

    static void createAndShowUI() {

        final JFrame frame = new JFrame();
        tabbedPane = new JTabbedPane();
        tabbedPane.add("Tab0", new JPanel());
        tabbedPane.add("Tab1", new JPanel());
        tabbedPane.add("Tab2", new JPanel());
        tabbedPane.setSelectedIndex(2);
        tabbedPane.addChangeListener(new ChangeListener() {

            public void stateChanged(final ChangeEvent pick) {
                bStateChanged = true;
                if (tabbedPane.getTabCount() == 3) {
                    tabbedPane.remove(2);
                }
            }
        });

        frame.getContentPane().add(tabbedPane);
        frame.setSize(300, 200);
        frame.setVisible(true);
    }
}
