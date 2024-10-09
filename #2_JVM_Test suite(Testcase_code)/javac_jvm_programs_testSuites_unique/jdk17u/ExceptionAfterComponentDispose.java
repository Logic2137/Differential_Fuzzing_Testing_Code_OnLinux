import java.awt.Canvas;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.image.BufferStrategy;
import java.util.List;
import javax.swing.JFrame;

public final class ExceptionAfterComponentDispose {

    public static void main(final String[] args) throws Exception {
        for (int size = -3; size < 3; ++size) {
            for (int numBuffers = 1; numBuffers < 3; ++numBuffers) {
                testCanvas(size, numBuffers);
            }
        }
        EventQueue.invokeAndWait(() -> {
            for (int size = -3; size < 3; ++size) {
                for (int numBuffers = 1; numBuffers < 3; ++numBuffers) {
                    testJFrame(size, numBuffers);
                }
            }
        });
    }

    private static void testCanvas(final int size, final int numBuffers) {
        final Frame frame = new Frame();
        try {
            final Canvas canvas = new Canvas();
            frame.setLayout(null);
            frame.add(canvas);
            frame.pack();
            canvas.setSize(size, size);
            canvas.createBufferStrategy(numBuffers);
            final BufferStrategy bs = canvas.getBufferStrategy();
            if (bs.getDrawGraphics() == null) {
                throw new RuntimeException("DrawGraphics is null");
            }
            frame.dispose();
            frame.pack();
            if (canvas.getBufferStrategy() != bs) {
                throw new RuntimeException("BS was changed");
            }
            if (bs.getDrawGraphics() == null) {
                throw new RuntimeException("DrawGraphics is null");
            }
        } finally {
            frame.dispose();
        }
    }

    private static void testJFrame(final int size, final int numBuffers) {
        for (final boolean undecorated : List.of(true, false)) {
            final JFrame frame = new JFrame();
            try {
                frame.setUndecorated(undecorated);
                frame.pack();
                frame.setSize(size, size);
                frame.createBufferStrategy(numBuffers);
                final BufferStrategy bs = frame.getBufferStrategy();
                if (bs.getDrawGraphics() == null) {
                    throw new RuntimeException("DrawGraphics is null");
                }
                frame.dispose();
                frame.pack();
                if (frame.getBufferStrategy() != bs) {
                    throw new RuntimeException("BS was changed");
                }
                if (bs.getDrawGraphics() == null) {
                    throw new RuntimeException("DrawGraphics is null");
                }
            } finally {
                frame.dispose();
            }
        }
    }
}
