import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.PrintStream;

public final class DumpOnKey {

    private static volatile boolean dumped;

    public static void main(final String[] args) throws AWTException {
        final boolean dump = Boolean.parseBoolean(args[0]);
        final Window w = new Frame() {

            @Override
            public void list(final PrintStream out, final int indent) {
                super.list(out, indent);
                dumped = true;
            }
        };
        w.setSize(200, 200);
        w.setLocationRelativeTo(null);
        w.setVisible(true);
        final Robot robot = new Robot();
        robot.setAutoDelay(50);
        robot.setAutoWaitForIdle(true);
        robot.mouseMove(w.getX() + w.getWidth() / 2, w.getY() + w.getHeight() / 2);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_F1);
        robot.keyRelease(KeyEvent.VK_F1);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        w.dispose();
        if (dumped != dump) {
            throw new RuntimeException("Exp:" + dump + ", actual:" + dumped);
        }
    }
}
