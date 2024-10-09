
import java.awt.Font;
import java.awt.Graphics;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;


public class SwingFontMetricsTest {

    private static final String LOWER_CASE_TEXT = "the quick brown fox jumps over the lazy dog";
    private static final String UPPER_CASE_TEXT = LOWER_CASE_TEXT.toUpperCase();
    private static final String TEXT = LOWER_CASE_TEXT + UPPER_CASE_TEXT;
    private static boolean passed = false;
    private static CountDownLatch latch = new CountDownLatch(1);

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(SwingFontMetricsTest::createAndShowGUI);
        latch.await(5, TimeUnit.SECONDS);

        if (!passed) {
            throw new RuntimeException("Test Failed!");
        }
    }

    private static void createAndShowGUI() {
        final JFrame frame = new JFrame();
        frame.setSize(300, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel label = new JLabel(TEXT) {
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                Font font = getFont();
                int width1 = getFontMetrics(font).stringWidth(TEXT);
                int width2 = g.getFontMetrics(font).stringWidth(TEXT);
                passed = (width1 == width2);
                latch.countDown();
                frame.dispose();
            }
        };
        frame.add(label);
        frame.setVisible(true);
    }
}
