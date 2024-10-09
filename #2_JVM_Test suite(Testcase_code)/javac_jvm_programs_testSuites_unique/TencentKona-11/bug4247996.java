


import java.awt.*;
import javax.swing.*;

public class bug4247996 {

    private static JButton button;
    private static JToggleButton toogleButton;

    public static void main(String[] args) throws Exception {

        Robot robot = new Robot();
        robot.setAutoDelay(50);

        UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

        javax.swing.SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                createAndShowGUI();
            }
        });

        robot.waitForIdle();

        Point point = getButtonCenter();
        robot.mouseMove(point.x, point.y);
        robot.waitForIdle();

        checkButtonsSize();

    }

    private static void checkButtonsSize() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                if (!button.getSize().equals(toogleButton.getSize())) {
                    throw new RuntimeException("Button sizes are different!");
                }
            }
        });
    }

    private static Point getButtonCenter() throws Exception {
        final Point[] result = new Point[1];

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                Point p = button.getLocationOnScreen();
                Dimension size = button.getSize();
                result[0] = new Point(p.x + size.width / 2, p.y + size.height / 2);
            }
        });
        return result[0];
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200, 200);

        JButton rButton = new JButton("Rollover");
        rButton.setRolloverEnabled(true);
        JToolBar nrToolbar = new JToolBar();
        nrToolbar.add(rButton);
        nrToolbar.remove(rButton);

        if (!rButton.isRolloverEnabled()) {
            throw new Error("Failed (bug 4260485): "
                    + "toolbar overrode button's rollover property");
        }

        JToolBar rToolbar = new JToolBar();
        rToolbar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        rToolbar.add(button = new JButton("Test"));
        rToolbar.add(toogleButton = new JToggleButton("Test"));

        frame.getContentPane().add(rToolbar, BorderLayout.NORTH);
        frame.setVisible(true);
    }
}
