

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Robot;

import javax.swing.JButton;
import javax.swing.SwingUtilities;


public final class SetEnabledPerformance {

    private static Frame frame;

    private static void createAndShowGUI() {
        frame = new Frame();
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 25, 0));
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        for (int i = 1; i < 10001; ++i) {
            frame.add(new JButton("Button " + i));
        }
        frame.setVisible(true);
    }

    public static void main(final String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> createAndShowGUI());
        final Robot robot = new Robot();
        robot.waitForIdle();
        robot.mouseMove(frame.getX() + 15, frame.getY() + 300);
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(() -> {
            long m = System.currentTimeMillis();
            for (final Component comp : frame.getComponents()) {
                comp.setEnabled(false);
            }
            m = System.currentTimeMillis() - m;
            System.err.println("Disabled in " + m + " ms");
            frame.dispose();
            
            if (m > 1000) {
                throw new RuntimeException("Too slow");
            }
        });
    }
}
