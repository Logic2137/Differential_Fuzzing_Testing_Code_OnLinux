import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.awt.image.VolatileImage;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public final class ClassCastExceptionForInvalidSurface {

    static GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

    static GraphicsConfiguration gc = ge.getDefaultScreenDevice().getDefaultConfiguration();

    static volatile VolatileImage vi = gc.createCompatibleVolatileImage(10, 10);

    static volatile Throwable failed;

    static BlockingQueue<VolatileImage> list = new ArrayBlockingQueue<>(50);

    static long endtime = System.nanoTime() + TimeUnit.SECONDS.toNanos(15);

    public static void main(final String[] args) throws InterruptedException {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> failed = e);
        BufferedImage bi = new BufferedImage(10, 10, TYPE_INT_ARGB);
        FontRenderContext frc = new FontRenderContext(null, false, false);
        Font font = new Font("Serif", Font.PLAIN, 12);
        GlyphVector gv = font.createGlyphVector(frc, new char[] { 'a', '1' });
        Thread t1 = new Thread(() -> {
            while (!isComplete()) {
                vi = gc.createCompatibleVolatileImage(10, 10);
                if (!list.offer(vi)) {
                    vi.flush();
                }
            }
            list.forEach(Image::flush);
        });
        Thread t2 = new Thread(() -> {
            while (!isComplete()) {
                VolatileImage vi = list.poll();
                if (vi != null) {
                    vi.flush();
                }
            }
        });
        Thread t3 = new Thread(() -> {
            while (!isComplete()) {
                vi.createGraphics().drawImage(bi, 1, 1, null);
            }
        });
        Thread t4 = new Thread(() -> {
            while (!isComplete()) {
                vi.createGraphics().drawGlyphVector(gv, 0, 0);
                vi.createGraphics().drawOval(0, 0, 10, 10);
                vi.createGraphics().drawLine(0, 0, 10, 10);
                vi.createGraphics().drawString("123", 1, 1);
                vi.createGraphics().draw(new Rectangle(0, 0, 10, 10));
                vi.createGraphics().fillOval(0, 0, 10, 10);
                final Graphics2D graphics = vi.createGraphics();
                graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                graphics.fillPolygon(new int[] { 0, 10, 10, 0 }, new int[] { 0, 0, 10, 10 }, 4);
            }
        });
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t1.join();
        t2.join();
        t3.join();
        t4.join();
        if (failed != null) {
            System.err.println("Test failed");
            failed.printStackTrace();
        }
    }

    private static boolean isComplete() {
        return endtime - System.nanoTime() < 0 || failed != null;
    }
}
