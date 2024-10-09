
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import static java.awt.RenderingHints.*;
import java.awt.Toolkit;
import java.util.Map;
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
    private static Object aaHint = null;

    public static void main(String[] args) throws Exception {
        Map map = (Map)Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
        aaHint = map.get(RenderingHints.KEY_TEXT_ANTIALIASING);
        if (aaHint == null) {
            aaHint = VALUE_TEXT_ANTIALIAS_DEFAULT;
        }

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
                Graphics2D g2d = (Graphics2D)g;
                int width1 = getFontMetrics(font).stringWidth(TEXT);
                
                g2d.setRenderingHint(KEY_TEXT_ANTIALIASING, aaHint);
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
