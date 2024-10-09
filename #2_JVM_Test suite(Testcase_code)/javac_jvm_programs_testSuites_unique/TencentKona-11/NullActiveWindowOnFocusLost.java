import java.awt.Button;
import java.awt.Frame;
import java.util.concurrent.TimeUnit;
import sun.awt.SunToolkit;

public final class NullActiveWindowOnFocusLost {

    private static volatile long endtime;

    private static Throwable failed;

    public static void main(final String[] args) throws Exception {
        endtime = System.nanoTime() + TimeUnit.SECONDS.toNanos(30);
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> failed = e);
        final Thread[] threads = new Thread[20];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = testThread(i);
        }
        for (final Thread thread : threads) {
            thread.start();
        }
        for (final Thread thread : threads) {
            thread.join();
        }
        if (failed != null) {
            failed.printStackTrace();
            throw new RuntimeException(failed);
        }
    }

    private static Thread testThread(int index) {
        return new Thread(new ThreadGroup("TG " + index), () -> {
            SunToolkit.createNewAppContext();
            while (!isComplete()) {
                final Frame frame = new Frame();
                frame.setSize(300, 300);
                frame.add(new Button("Button"));
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                try {
                    Thread.sleep(index);
                } catch (InterruptedException ignored) {
                }
                frame.dispose();
            }
        });
    }

    private static boolean isComplete() {
        return endtime - System.nanoTime() < 0 || failed != null;
    }
}
