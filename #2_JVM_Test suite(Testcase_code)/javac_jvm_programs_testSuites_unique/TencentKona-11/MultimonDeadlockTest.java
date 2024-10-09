


import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.reflect.InvocationTargetException;

public class MultimonDeadlockTest {

    public static void main(String argv[]) {
        final GraphicsDevice[] devices = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getScreenDevices();
        if (devices.length < 2) {
            System.out.println("It's a multiscreen test... skipping!");
            return;
        }

        Frame frames[] = new Frame[devices.length];
        try {
            EventQueue.invokeAndWait(() -> {
                for (int i = 0; i < devices.length; i++) {
                    frames[i] = new Frame();
                    frames[i].setSize(100, 100);
                    frames[i].setBackground(Color.BLUE);
                    devices[i].setFullScreenWindow(frames[i]);
                }
            });
            Thread.sleep(5000);
        } catch (InterruptedException | InvocationTargetException ex) {
        } finally {
            for (int i = 0; i < devices.length; i++) {
                devices[i].setFullScreenWindow(null);
                frames[i].dispose();
            }
        }

    }
}
