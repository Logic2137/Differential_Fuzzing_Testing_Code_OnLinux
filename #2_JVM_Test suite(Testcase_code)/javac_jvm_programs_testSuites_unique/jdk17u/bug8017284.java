import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.Robot;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class bug8017284 {

    private static final int TAB_COUNT = 100;

    private static final int ITERATIONS = 100;

    private static JFrame frame;

    private static JTabbedPane tabbedPane;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        SwingUtilities.invokeAndWait(() -> {
            frame = new JFrame();
            frame.setSize(500, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            tabbedPane = new JTabbedPane();
            for (int i = 0; i < TAB_COUNT; i++) {
                tabbedPane.add("Header " + i, new JLabel("Content: " + i));
            }
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
            frame.setVisible(true);
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(() -> {
            for (int j = 0; j < ITERATIONS; j++) {
                for (int i = 0; i < TAB_COUNT; i++) {
                    tabbedPane.setTitleAt(i, getHtmlText(j * TAB_COUNT + i));
                }
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(() -> frame.dispose());
    }

    private static String getHtmlText(int i) {
        return "<html><b><i>" + i + "</b></i>";
    }
}
