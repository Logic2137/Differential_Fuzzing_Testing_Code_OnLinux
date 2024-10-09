import java.awt.AWTException;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Robot;

public class WindowOpacity {

    public static void main(String[] args) throws Exception {
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        if (!gd.isWindowTranslucencySupported(GraphicsDevice.WindowTranslucency.TRANSLUCENT)) {
            System.out.println("Either the Toolkit or the native system does not support" + " controlling the window opacity level.");
            return;
        }
        Frame f = new Frame("Opacity test");
        try {
            test(f);
        } finally {
            f.dispose();
        }
    }

    private static void test(final Frame f) throws AWTException {
        boolean passed;
        f.setUndecorated(true);
        float curOpacity = f.getOpacity();
        if (curOpacity < 1.0f || curOpacity > 1.0f) {
            throw new RuntimeException("getOpacity() reports the initial opacity level " + "other than 1.0: " + curOpacity);
        }
        passed = false;
        try {
            f.setOpacity(-0.5f);
        } catch (IllegalArgumentException e) {
            passed = true;
        }
        if (!passed) {
            throw new RuntimeException("setOpacity() allows passing negative opacity level.");
        }
        passed = false;
        try {
            f.setOpacity(1.5f);
        } catch (IllegalArgumentException e) {
            passed = true;
        }
        if (!passed) {
            throw new RuntimeException("setOpacity() allows passing opacity level greater than 1.0.");
        }
        f.setOpacity(0.5f);
        curOpacity = f.getOpacity();
        if (curOpacity < 0.5f || curOpacity > 0.5f) {
            throw new RuntimeException("setOpacity() reports the opacity level that " + "differs from the value set with " + "setWindowOpacity: " + curOpacity);
        }
        f.setOpacity(0.75f);
        curOpacity = f.getOpacity();
        if (curOpacity < 0.75f || curOpacity > 0.75f) {
            throw new RuntimeException("getOpacity() reports the opacity level that " + "differs from the value set with " + "setWindowOpacity the second time: " + curOpacity);
        }
        f.setBounds(100, 100, 300, 200);
        f.setVisible(true);
        Robot robot = new Robot();
        robot.waitForIdle();
        curOpacity = f.getOpacity();
        if (curOpacity < 0.75f || curOpacity > 0.75f) {
            throw new RuntimeException("getOpacity() reports the opacity level that " + "differs from the value set with " + "setWindowOpacity before showing the frame: " + curOpacity);
        }
        f.setOpacity(0.5f);
        robot.waitForIdle();
        curOpacity = f.getOpacity();
        if (curOpacity < 0.5f || curOpacity > 0.5f) {
            throw new RuntimeException("getOpacity() reports the opacity level that " + "differs from the value set with " + "setWindowOpacity after showing the frame: " + curOpacity);
        }
    }
}
