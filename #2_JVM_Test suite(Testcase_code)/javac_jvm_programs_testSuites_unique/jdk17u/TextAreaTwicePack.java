import java.awt.Dimension;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.Robot;

public final class TextAreaTwicePack {

    public static void main(final String[] args) {
        final Frame frame = new Frame();
        final TextArea ta = new TextArea();
        frame.add(ta);
        frame.pack();
        frame.setVisible(true);
        sleep();
        final Dimension before = frame.getSize();
        frame.pack();
        final Dimension after = frame.getSize();
        if (!after.equals(before)) {
            throw new RuntimeException("Expected size: " + before + ", actual size: " + after);
        }
        frame.dispose();
    }

    private static void sleep() {
        try {
            Robot robot = new Robot();
            robot.waitForIdle();
            Thread.sleep(500L);
        } catch (Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
