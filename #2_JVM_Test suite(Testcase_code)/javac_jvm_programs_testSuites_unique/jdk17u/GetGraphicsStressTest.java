import java.awt.Frame;
import java.awt.Graphics;
import java.util.concurrent.TimeUnit;

public final class GetGraphicsStressTest {

    static volatile Throwable failed;

    static volatile long endtime;

    public static void main(final String[] args) throws Exception {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> failed = e);
        for (int i = 0; i < 4; i++) {
            endtime = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);
            test();
        }
    }

    private static void test() throws Exception {
        Frame f = new Frame();
        f.setSize(100, 100);
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        Thread thread1 = new Thread(() -> {
            while (!isComplete()) {
                f.removeNotify();
                f.addNotify();
            }
        });
        Thread thread2 = new Thread(() -> {
            while (!isComplete()) {
                Graphics g = f.getGraphics();
                if (g != null) {
                    g.dispose();
                }
            }
        });
        Thread thread3 = new Thread(() -> {
            while (!isComplete()) {
                Graphics g = f.getGraphics();
                if (g != null) {
                    g.dispose();
                }
            }
        });
        Thread thread4 = new Thread(() -> {
            while (!isComplete()) {
                Graphics g = f.getGraphics();
                if (g != null) {
                    g.drawLine(0, 0, 4, 4);
                    g.dispose();
                }
            }
        });
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        thread1.join();
        thread2.join();
        thread3.join();
        thread4.join();
        f.dispose();
        if (failed != null) {
            System.err.println("Test failed");
            failed.printStackTrace();
            throw new RuntimeException(failed);
        }
    }

    private static boolean isComplete() {
        return endtime - System.nanoTime() < 0 || failed != null;
    }
}
