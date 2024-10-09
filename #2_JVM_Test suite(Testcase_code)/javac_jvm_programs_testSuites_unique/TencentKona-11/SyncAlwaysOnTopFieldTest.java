

import java.awt.Window;


public class SyncAlwaysOnTopFieldTest {

    private static final int WINDOWS_COUNT = 200;
    private static final int STEPS_COUNT = 20;

    public static void main(String[] args) throws Exception {
        final Window rootWindow = createWindow(null);

        new Thread(() -> {
            for (int i = 0; i < WINDOWS_COUNT; i++) {
                createWindow(rootWindow);
            }
        }).start();

        boolean alwaysOnTop = true;
        for (int i = 0; i < STEPS_COUNT; i++) {
            Thread.sleep(10);
            rootWindow.setAlwaysOnTop(alwaysOnTop);
            alwaysOnTop = !alwaysOnTop;
        }
    }

    private static Window createWindow(Window parent) {
        Window window = new Window(parent);
        window.setSize(200, 200);
        window.setVisible(true);
        return window;
    }
}
