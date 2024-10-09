import java.awt.Font;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicReference;

public class FontLayoutStressTest {

    private static final int NUMBER_OF_THREADS = Runtime.getRuntime().availableProcessors() * 2;

    private static final long TIME_TO_RUN_NS = 1_000_000_000;

    private static final Font FONT = new Font(Font.SERIF, Font.PLAIN, 12);

    private static final FontRenderContext FRC = new FontRenderContext(null, false, false);

    private static final char[] TEXT = "Lorem ipsum dolor sit amet, ...".toCharArray();

    private static double doLayout() {
        GlyphVector gv = FONT.layoutGlyphVector(FRC, TEXT, 0, TEXT.length, Font.LAYOUT_LEFT_TO_RIGHT);
        return gv.getGlyphPosition(gv.getNumGlyphs()).getX();
    }

    public static void main(String[] args) throws Throwable {
        double expectedWidth = doLayout();
        AtomicReference<Throwable> throwableRef = new AtomicReference<>();
        CyclicBarrier barrier = new CyclicBarrier(NUMBER_OF_THREADS);
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_THREADS; i++) {
            Thread thread = new Thread(() -> {
                try {
                    barrier.await();
                    long timeToStop = System.nanoTime() + TIME_TO_RUN_NS;
                    while (System.nanoTime() < timeToStop) {
                        double width = doLayout();
                        if (width != expectedWidth) {
                            throw new RuntimeException("Unexpected layout result");
                        }
                    }
                } catch (Throwable e) {
                    throwableRef.set(e);
                }
            });
            threads.add(thread);
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        Throwable throwable = throwableRef.get();
        if (throwable != null) {
            throw throwable;
        }
    }
}
