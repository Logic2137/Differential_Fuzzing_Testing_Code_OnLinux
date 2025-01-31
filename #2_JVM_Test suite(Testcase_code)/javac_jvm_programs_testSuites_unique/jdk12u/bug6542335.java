



import javax.swing.*;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.InputEvent;

public class bug6542335 {
    private static JScrollBar sb;
    private static MyScrollBarUI ui;

    public static void main(String[] args) throws Exception {
        final Robot robot = new Robot();
        robot.setAutoDelay(10);

        final Rectangle[] thumbBounds = new Rectangle[1];

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                final JFrame frame = new JFrame("bug6542335");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                sb = new JScrollBar(0, 0, 1, 0, 1);

                ui = new MyScrollBarUI();
                sb.setUI(ui);

                sb.setPreferredSize(new Dimension(200, 17));
                DefaultBoundedRangeModel rangeModel = new DefaultBoundedRangeModel();
                rangeModel.setMaximum(100);
                rangeModel.setMinimum(0);
                rangeModel.setExtent(50);
                rangeModel.setValue(50);

                sb.setModel(rangeModel);
                frame.add(sb, BorderLayout.NORTH);

                frame.setSize(200, 100);
                frame.setVisible(true);
            }
        });

        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                thumbBounds[0] = new Rectangle(ui.getThumbBounds());

                Point l = sb.getLocationOnScreen();

                robot.mouseMove(l.x + (int) (0.75 * sb.getWidth()), l.y + sb.getHeight() / 2);
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            }
        });

        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                Rectangle newThumbBounds = ui.getThumbBounds();

                if (!thumbBounds[0].equals(newThumbBounds)) {
                    throw new RuntimeException("Test failed.\nOld bounds: " + thumbBounds[0] +
                    "\nNew bounds: " + newThumbBounds);
                }
            }
        });
    }

    static class MyScrollBarUI extends BasicScrollBarUI {
        public Rectangle getThumbBounds() {
            return super.getThumbBounds();
        }
    }
}
