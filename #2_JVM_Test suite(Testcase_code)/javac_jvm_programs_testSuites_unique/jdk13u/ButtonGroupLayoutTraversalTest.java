import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import javax.swing.JToggleButton;
import javax.swing.LayoutFocusTraversalPolicy;
import javax.swing.UnsupportedLookAndFeelException;

public class ButtonGroupLayoutTraversalTest {

    static int nx = 3;

    static int ny = 3;

    static int[] focusCnt = new int[nx * ny];

    private static JFrame window;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> changeLAF());
        SwingUtilities.invokeAndWait(() -> initLayout(nx, ny));
        Robot robot = new Robot();
        robot.setAutoDelay(100);
        robot.waitForIdle();
        robot.delay(200);
        for (int i = 0; i < nx * ny - nx * ny / 2 - 1; i++) {
            robot.keyPress(KeyEvent.VK_RIGHT);
            robot.keyRelease(KeyEvent.VK_RIGHT);
        }
        for (int i = 0; i < nx * ny / 2; i++) {
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
        }
        robot.waitForIdle();
        robot.delay(200);
        for (int i = 0; i < nx * ny; i++) {
            if (focusCnt[i] < 1) {
                SwingUtilities.invokeLater(window::dispose);
                throw new RuntimeException("Component " + i + " is not reachable in the forward focus cycle");
            } else if (focusCnt[i] > 1) {
                SwingUtilities.invokeLater(window::dispose);
                throw new RuntimeException("Component " + i + " got focus more than once in the forward focus cycle");
            }
        }
        for (int i = 0; i < nx * ny / 2; i++) {
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_SHIFT);
        }
        for (int i = 0; i < nx * ny - nx * ny / 2 - 1; i++) {
            robot.keyPress(KeyEvent.VK_LEFT);
            robot.keyRelease(KeyEvent.VK_LEFT);
        }
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.waitForIdle();
        robot.delay(200);
        for (int i = 0; i < nx * ny; i++) {
            if (focusCnt[i] < 2) {
                SwingUtilities.invokeLater(window::dispose);
                throw new RuntimeException("Component " + i + " is not reachable in the backward focus cycle");
            } else if (focusCnt[i] > 2) {
                SwingUtilities.invokeLater(window::dispose);
                throw new RuntimeException("Component " + i + " got focus more than once in the backward focus cycle");
            }
        }
        SwingUtilities.invokeLater(window::dispose);
    }

    private static void changeLAF() {
        String currentLAF = UIManager.getLookAndFeel().toString();
        currentLAF = currentLAF.toLowerCase();
        if (currentLAF.contains("aqua") || currentLAF.contains("nimbus")) {
            try {
                UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void initLayout(int nx, int ny) {
        window = new JFrame("Test");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel rootPanel = new JPanel();
        rootPanel.setLayout(new BorderLayout());
        JPanel formPanel = new JPanel(new GridLayout(nx, ny));
        formPanel.setFocusTraversalPolicy(new LayoutFocusTraversalPolicy());
        formPanel.setFocusCycleRoot(true);
        ButtonGroup radioButtonGroup = new ButtonGroup();
        for (int i = 0; i < nx * ny; i++) {
            JToggleButton comp;
            if (i % 2 == 0) {
                comp = new JRadioButton("Grouped component");
                radioButtonGroup.add(comp);
            } else {
                comp = new JRadioButton("Single component");
            }
            formPanel.add(comp);
            int fi = i;
            comp.setBackground(Color.red);
            comp.addFocusListener(new FocusAdapter() {

                @Override
                public void focusGained(FocusEvent e) {
                    focusCnt[fi]++;
                    if (focusCnt[fi] == 1) {
                        ((JComponent) e.getSource()).setBackground(Color.yellow);
                    } else if (focusCnt[fi] == 2) {
                        ((JComponent) e.getSource()).setBackground(Color.green);
                    } else {
                        ((JComponent) e.getSource()).setBackground(Color.red);
                    }
                }
            });
        }
        rootPanel.add(formPanel, BorderLayout.CENTER);
        window.add(rootPanel);
        window.pack();
        window.setVisible(true);
    }
}
