import java.awt.*;

public class GetScreenLocationTest {

    public static void main(String[] args) throws Exception {
        Window frame = new Frame();
        frame.pack();
        int width = Math.max(frame.getWidth() * 2, 200);
        int height = Math.max(frame.getHeight() * 2, 100);
        Robot robot = new Robot();
        for (int i = 0; i < 30; i++) {
            frame.dispose();
            frame = new Dialog((Frame) null);
            frame.setBounds(0, 0, width, height);
            frame.setVisible(true);
            robot.waitForIdle();
            robot.delay(200);
            frame.setLocation(421, 321);
            robot.waitForIdle();
            robot.delay(200);
            Dimension size = frame.getSize();
            if (size.width != width || size.height != height) {
                frame.dispose();
                throw new RuntimeException("getSize() is wrong " + size);
            }
            Rectangle r = frame.getBounds();
            frame.dispose();
            if (r.x != 421 || r.y != 321) {
                throw new RuntimeException("getLocation() returns " + "wrong coordinates " + r.getLocation());
            }
            if (r.width != width || r.height != height) {
                throw new RuntimeException("getSize() is wrong " + r.getSize());
            }
        }
        System.out.println("ok");
    }
}
