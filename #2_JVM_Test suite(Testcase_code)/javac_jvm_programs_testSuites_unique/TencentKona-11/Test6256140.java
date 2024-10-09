



import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class Test6256140 {

    private static volatile JFormattedTextField ft;

    private final static String initialText = "value";
    private final static JLabel toolTipLabel = new JLabel("tip");

    public static void main(String[] args) throws Exception {

        Robot robot = new Robot();
        robot.setAutoDelay(10);

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        robot.waitForIdle();

        Point point = ft.getLocationOnScreen();
        robot.mouseMove(point.x, point.y);
        robot.mouseMove(point.x + 3, point.y + 3);

        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.waitForIdle();

        if (!isTooltipShowning()) {
            throw new RuntimeException("Tooltip is not shown");
        }

        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        robot.waitForIdle();

        if (isTooltipShowning()) {
            throw new RuntimeException("Tooltip must be hidden now");
        }

        if (isTextEqual()) {
            throw new RuntimeException("FormattedTextField must *not* cancel the updated value this time");
        }

        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        robot.waitForIdle();

        if (!isTextEqual()) {
            throw new RuntimeException("FormattedTextField must cancel the updated value");
        }
    }

    private static boolean isTooltipShowning() throws Exception {
        final boolean[] result = new boolean[1];

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                result[0] = toolTipLabel.isShowing();
            }
        });

        return result[0];
    }

    private static boolean isTextEqual() throws Exception {
        final boolean[] result = new boolean[1];

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                result[0] = initialText.equals(ft.getText());
            }
        });

        return result[0];
    }

    private static void createAndShowGUI() {
        ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);
        ToolTipManager.sharedInstance().setInitialDelay(0);

        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        ft = new JFormattedTextField() {

            public JToolTip createToolTip() {
                JToolTip toolTip = super.createToolTip();
                toolTip.setLayout(new BorderLayout());
                toolTip.add(toolTipLabel);
                return toolTip;
            }
        };
        ft.setToolTipText("   ");
        ft.setValue(initialText);
        frame.add(ft);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        ft.requestFocus();
    }
}
