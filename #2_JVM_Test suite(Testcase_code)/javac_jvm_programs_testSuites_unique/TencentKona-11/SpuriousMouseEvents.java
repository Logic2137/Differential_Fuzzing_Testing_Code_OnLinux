

 
import java.awt.AWTException;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.Robot;
import java.awt.GraphicsConfiguration;
import java.awt.Frame;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;

public class SpuriousMouseEvents {

    private static volatile boolean testPassed = true;

    public static void main(String args[]) throws AWTException {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gds = ge.getScreenDevices();
        if (gds.length < 2) {
            return;
        }

        Robot r = null;
        try {
            r = new Robot();
        } catch (Exception e) {
            throw new RuntimeException("Couldn't create AWT robot" + e);
        }

        for (int i = 1; i >= 0; i--) {
            GraphicsDevice gd = gds[i];
            GraphicsDevice gdo = gds[1 - i];
            GraphicsConfiguration gc = gd.getDefaultConfiguration();
            GraphicsConfiguration gco = gdo.getDefaultConfiguration();
            Frame f = new Frame("Frame", gc);
            f.setBounds(gc.getBounds().x + 100, gc.getBounds().y + 100, 200, 200);
            f.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseMoved(MouseEvent me) {
                    testPassed = false;
                }
            });
            f.setVisible(true);

            r = new Robot(gdo);
            int x = (int) gco.getBounds().x;
            for (int j = x; j < x + 400; j += 10) {
                r.mouseMove(j, 200);
                r.delay(10);
            }
            r.delay(1000);

            f.setVisible(false);
            f.dispose();

            if (!testPassed) {
                break;
            }
        }

        if (!testPassed) {
            throw new RuntimeException("Wrong mouse events are sent");
        }
    }
}
