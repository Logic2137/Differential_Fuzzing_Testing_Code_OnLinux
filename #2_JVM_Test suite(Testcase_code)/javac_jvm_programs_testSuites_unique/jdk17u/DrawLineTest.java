import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;

public class DrawLineTest extends Frame {

    volatile static boolean done = false;

    public static void main(String[] args) throws Exception {
        EventQueue.invokeLater(() -> {
            DrawLineTest frame = new DrawLineTest();
            frame.setVisible(true);
            Image img = frame.createVolatileImage(1000, 1000);
            img.getGraphics().drawLine(0, 0, 34005, 34005);
            done = true;
            frame.setVisible(false);
            frame.dispose();
            return;
        });
        int cnt = 0;
        while (!done && (cnt++ < 60)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        if (!done) {
            if ((System.getProperty("test.src") != null)) {
                throw new RuntimeException("Test Failed");
            } else {
                System.out.println("Test failed.");
                Runtime.getRuntime().halt(-1);
            }
        }
        return;
    }
}
