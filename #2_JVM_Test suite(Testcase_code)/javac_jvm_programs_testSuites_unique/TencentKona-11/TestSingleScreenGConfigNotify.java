

import java.awt.EventQueue;
import java.awt.GraphicsConfiguration;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;


public final class TestSingleScreenGConfigNotify {

    private static String name = "graphicsConfiguration";
    private static CountDownLatch go = new CountDownLatch(1);
    private static JFrame frame;
    private static GraphicsConfiguration after;
    private static GraphicsConfiguration before;
    private static JButton button;

    public static void main(final String[] args) throws Exception {
        EventQueue.invokeAndWait(() -> {
            frame = new JFrame();

            frame.setSize(300,300);
            frame.setLocationRelativeTo(null);
            button = new JButton();
            button.addPropertyChangeListener(evt -> {
                if (evt.getPropertyName().equals(name)) {
                    go.countDown();
                }
            });

            before = button.getGraphicsConfiguration();

            frame.add(button);
            frame.setVisible(true);
        });

        boolean called = go.await(10, TimeUnit.SECONDS);

        EventQueue.invokeAndWait(() -> {
            after = button.getGraphicsConfiguration();
            frame.dispose();
        });

        if (Objects.equals(before, after) && called) {
            throw new RuntimeException("propertyChange() should not be called");
        }
        if (!Objects.equals(before, after) && !called) {
            throw new RuntimeException("propertyChange() should be called");
        }
    }
}
