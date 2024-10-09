import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.Window;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.NANOSECONDS;

public final class TreeLockDeadlock extends Frame {

    @Override
    public synchronized GraphicsConfiguration getGraphicsConfiguration() {
        return super.getGraphicsConfiguration();
    }

    @Override
    public synchronized void reshape(int x, int y, int width, int height) {
        super.reshape(x, y, width, height);
    }

    @Override
    public synchronized float getOpacity() {
        return super.getOpacity();
    }

    public static void main(final String[] args) throws Exception {
        final Window window = new TreeLockDeadlock();
        window.setSize(300, 300);
        test(window);
    }

    private static void test(final Window window) throws Exception {
        final long start = System.nanoTime();
        final long end = start + NANOSECONDS.convert(1, MINUTES);
        final Runnable r1 = () -> {
            while (System.nanoTime() < end) {
                window.setBounds(window.getBounds());
            }
        };
        final Runnable r2 = () -> {
            while (System.nanoTime() < end) {
                window.getGraphicsConfiguration();
                window.getOpacity();
            }
        };
        final Thread t1 = new Thread(r1);
        final Thread t2 = new Thread(r1);
        final Thread t3 = new Thread(r2);
        final Thread t4 = new Thread(r2);
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
    }
}
