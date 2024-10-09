import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Robot;
import java.util.stream.IntStream;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class LabelDotTest {

    private final static String longText = "show a very long text to have it " + "automatically shortened";

    private final static String shortText = "show short text";

    private static JFrame frame;

    private static JLabel label;

    private static JMenu menu;

    private static volatile boolean isException = false;

    private static void createUI() {
        System.out.println("BEFORE CREATION");
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(50, 150));
        frame.setLocationRelativeTo(null);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.weightx = 1.0;
        c.weighty = 0.0;
        c.gridwidth = GridBagConstraints.REMAINDER;
        JMenuBar menuBar = new JMenuBar();
        menu = new JMenu("Menu");
        menuBar.add(menu);
        frame.add(menuBar, c);
        frame.add(new JLabel("Title", SwingConstants.CENTER), c);
        c.weighty = 1.0;
        frame.add(new JPanel(new GridBagLayout()), c);
        c.weighty = 0.0;
        label = new JLabel(shortText);
        frame.add(label, c);
        frame.setVisible(true);
    }

    private static void runTest(int iterations) throws Exception {
        Robot robot = new Robot();
        IntStream.range(0, iterations).forEach((i) -> {
            SwingUtilities.invokeLater(() -> {
                if (label.getText().equals(shortText)) {
                    label.setText(longText);
                } else {
                    label.setText(shortText);
                }
                isException = (menu.getMinimumSize().height == 1 && !menu.getMinimumSize().equals(menu.getPreferredSize())) || isException;
            });
            robot.waitForIdle();
        });
    }

    public static void main(String[] args) throws Exception {
        try {
            SwingUtilities.invokeAndWait(() -> createUI());
            runTest(50);
        } finally {
            if (frame != null) {
                SwingUtilities.invokeAndWait(() -> frame.dispose());
            }
            if (isException)
                throw new RuntimeException("Size of Menu bar is not correct.");
        }
    }
}
