


import java.awt.*;

public class GetScreenLocationTest {

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        Window frame = null;
        for(int i = 0; i < 30; i++) {
            if(frame != null) frame.dispose();
            frame = new Dialog((Frame)null);
            frame.setBounds(0, 0, 200, 100);
            frame.setVisible(true);
            robot.waitForIdle();
            robot.delay(200);
            frame.setLocation(321, 121);
            robot.waitForIdle();
            robot.delay(200);
            Dimension size = frame.getSize();
            if(size.width != 200 || size.height != 100) {
                frame.dispose();
                throw new RuntimeException("getSize() is wrong " + size);
            }
            Rectangle r = frame.getBounds();
            frame.dispose();
            if(r.x != 321 || r.y != 121) {
                throw new RuntimeException("getLocation() returns " +
                        "wrong coordinates " + r.getLocation());
            }
            if(r.width != 200 || r.height != 100) {
                throw new RuntimeException("getSize() is wrong " + r.getSize());
            }
        }
        System.out.println("ok");
    }

}
