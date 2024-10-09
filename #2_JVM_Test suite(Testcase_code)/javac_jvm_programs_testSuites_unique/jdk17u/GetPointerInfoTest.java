import java.awt.*;

public class GetPointerInfoTest {

    private static final String successStage = "Test stage completed.Passed.";

    public static void main(String[] args) throws Exception {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gds = ge.getScreenDevices();
        int gdslen = gds.length;
        System.out.println("There are " + gdslen + " Graphics Devices");
        if (gdslen == 0) {
            System.out.println("Nothing to be done.");
            return;
        }
        Robot robot = new Robot(gds[0]);
        robot.setAutoDelay(0);
        robot.setAutoWaitForIdle(true);
        robot.delay(10);
        robot.waitForIdle();
        Point p = new Point(101, 99);
        robot.mouseMove(p.x, p.y);
        PointerInfo pi = MouseInfo.getPointerInfo();
        if (pi == null) {
            throw new RuntimeException("Test failed. getPointerInfo() returned null value.");
        } else {
            System.out.println(successStage);
        }
        Point piLocation = pi.getLocation();
        if (piLocation.x != p.x || piLocation.y != p.y) {
            throw new RuntimeException("Test failed.getPointerInfo() returned incorrect result.");
        } else {
            System.out.println(successStage);
        }
        System.out.println("Test PASSED.");
    }
}
