import java.awt.EventQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.swing.JFrame;
import javax.swing.JPanel;

public final class OrderOfGConfigNotify {

    private static String name = "graphicsConfiguration";

    public static void main(final String[] args) throws Exception {
        EventQueue.invokeAndWait(() -> {
            AtomicBoolean parentCalled = new AtomicBoolean(false);
            AtomicBoolean childCalled = new AtomicBoolean(false);
            JFrame frame = new JFrame();
            JPanel parent = new JPanel();
            parent.addPropertyChangeListener(evt -> {
                if (!evt.getPropertyName().equals(name)) {
                    return;
                }
                if (!childCalled.get()) {
                    throw new RuntimeException("Parent is called/child is not");
                }
                parentCalled.set(true);
                if (parent.getGraphicsConfiguration() == null) {
                    throw new RuntimeException("GraphicsConfiguration is null");
                }
            });
            JPanel child = new JPanel();
            child.addPropertyChangeListener(evt -> {
                if (!evt.getPropertyName().equals(name)) {
                    return;
                }
                childCalled.set(true);
                if (child.getGraphicsConfiguration() == null) {
                    throw new RuntimeException("GraphicsConfiguration is null");
                }
            });
            parent.add(child);
            frame.add(parent);
            if (!parentCalled.get() || !childCalled.get()) {
                throw new RuntimeException("Property listener was not called");
            }
        });
    }
}
