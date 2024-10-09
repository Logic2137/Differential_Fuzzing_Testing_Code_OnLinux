import java.awt.*;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.swing.*;

public class bug6917744 {

    private static JFrame frame;

    private static JEditorPane editorPane;

    private static JScrollPane scrollPane;

    private static Robot robot;

    private static Point p = null;

    static void blockTillDisplayed(JComponent comp) throws Exception {
        while (p == null) {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    p = comp.getLocationOnScreen();
                });
            } catch (IllegalStateException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.setAutoDelay(100);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                frame = new JFrame();
                editorPane = new JEditorPane();
                try {
                    editorPane.setPage(bug6917744.class.getResource("/test.html"));
                } catch (IOException e) {
                    frame.dispose();
                    throw new RuntimeException("HTML resource not found", e);
                }
                scrollPane = new JScrollPane(editorPane);
                frame.getContentPane().add(scrollPane);
                frame.setSize(400, 300);
                frame.setVisible(true);
            }
        });
        blockTillDisplayed(editorPane);
        robot.mouseMove(p.x + 50, p.y + 50);
        robot.waitForIdle();
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
        for (int i = 0; i < 50; i++) {
            robot.keyPress(KeyEvent.VK_PAGE_DOWN);
            robot.keyRelease(KeyEvent.VK_PAGE_DOWN);
        }
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                BoundedRangeModel model = scrollPane.getVerticalScrollBar().getModel();
                if (model.getValue() + model.getExtent() != model.getMaximum()) {
                    frame.dispose();
                    throw new RuntimeException("Invalid HTML position");
                }
            }
        });
        robot.waitForIdle();
        for (int i = 0; i < 50; i++) {
            robot.keyPress(KeyEvent.VK_PAGE_UP);
            robot.keyRelease(KeyEvent.VK_PAGE_UP);
        }
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                BoundedRangeModel model = scrollPane.getVerticalScrollBar().getModel();
                if (model.getValue() != model.getMinimum()) {
                    frame.dispose();
                    throw new RuntimeException("Invalid HTML position");
                }
                frame.dispose();
            }
        });
    }
}
