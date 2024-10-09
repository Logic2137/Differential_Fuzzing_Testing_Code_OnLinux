import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GraphicsEnvironment;
import javax.swing.JFrame;
import javax.swing.JLabel;
import sun.java2d.SunGraphicsEnvironment;

public final class DisplayListenerLeak {

    private static JFrame frame;

    private volatile static boolean failed = false;

    private static void createAndShowGUI() {
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
            e.printStackTrace();
            failed = true;
        });
        frame = new JFrame();
        JLabel emptyLabel = new JLabel("");
        emptyLabel.setPreferredSize(new Dimension(600, 400));
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(final String[] args) throws Exception {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        if (!(ge instanceof SunGraphicsEnvironment)) {
            return;
        }
        EventQueue.invokeAndWait(() -> createAndShowGUI());
        SunGraphicsEnvironment sge = (SunGraphicsEnvironment) ge;
        final long startTime = System.nanoTime();
        while (!failed) {
            if (System.nanoTime() - startTime > 60_000_000_000L) {
                break;
            }
            System.gc();
            EventQueue.invokeAndWait(() -> {
                frame.setSize(frame.getHeight(), frame.getWidth());
                frame.pack();
            });
            EventQueue.invokeAndWait(sge::displayChanged);
        }
        EventQueue.invokeAndWait(frame::dispose);
        if (failed) {
            throw new RuntimeException();
        }
    }
}
