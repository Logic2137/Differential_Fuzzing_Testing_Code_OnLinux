import java.awt.*;

public class WarningWindowDisposeCrashTest {

    public static void main(String[] args) throws Exception {
        Frame f = new Frame();
        f.setVisible(true);
        Robot robot;
        try {
            robot = new Robot();
            robot.waitForIdle();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Cannot create Robot");
        }
        Thread.sleep(1000);
        f.dispose();
        for (int i = 0; i < 1000; i++) Toolkit.getDefaultToolkit().sync();
    }
}
