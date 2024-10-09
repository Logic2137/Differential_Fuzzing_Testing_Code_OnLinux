import java.awt.Frame;

public final class FrameSetSizeStressTest {

    public static void main(final String[] args) {
        final Frame frame = new Frame();
        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        for (int i = 0; i < 1000; ++i) {
            frame.setSize(100, 100);
            frame.setSize(200, 200);
            frame.setSize(300, 300);
        }
        frame.dispose();
    }
}
