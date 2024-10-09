import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;

public class ISCthrownByFileListTest {

    private static Frame frame = null;

    private static FileDialog fd = null;

    static boolean passed = true;

    public static final void main(String[] args) {
        boolean isXToolkit = Toolkit.getDefaultToolkit().getClass().getName().equals("sun.awt.X11.XToolkit");
        if (!isXToolkit) {
            return;
        }
        frame = new Frame("frame");
        frame.setLayout(new FlowLayout());
        frame.setBounds(100, 100, 100, 100);
        frame.setVisible(true);
        fd = new FileDialog(frame, "file dialog", FileDialog.LOAD);
        final Thread.UncaughtExceptionHandler eh = new Thread.UncaughtExceptionHandler() {

            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                ISCthrownByFileListTest.passed = false;
            }
        };
        test();
    }

    private static void test() {
        Robot r;
        try {
            r = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException(e.getMessage());
        }
        r.delay(500);
        new Thread(new Runnable() {

            public void run() {
                fd.setDirectory(System.getProperty("test.src", "."));
                fd.setVisible(true);
            }
        }).start();
        r.delay(2000);
        r.waitForIdle();
        Component focusedWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow();
        if (focusedWindow != fd) {
            throw new RuntimeException("Test failed - the file dialog isn't focused window, owner: " + focusedWindow);
        }
        r.waitForIdle();
        r.keyPress(KeyEvent.VK_SPACE);
        r.delay(50);
        r.keyRelease(KeyEvent.VK_SPACE);
        r.delay(1000);
        fd.setVisible(false);
        r.delay(1000);
        r.waitForIdle();
        if (!ISCthrownByFileListTest.passed) {
            throw new RuntimeException("Test failed.");
        }
    }
}
