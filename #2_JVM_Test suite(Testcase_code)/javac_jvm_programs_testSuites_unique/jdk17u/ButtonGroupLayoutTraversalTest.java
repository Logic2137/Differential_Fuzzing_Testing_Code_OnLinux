import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.Arrays;
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

    private static final int NX = 3;

    private static final int NY = 3;

    private static final int[] focusCnt = new int[NX * NY];

    private static JFrame window;

    private static Robot robot;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.setAutoDelay(100);
        for (UIManager.LookAndFeelInfo laf : UIManager.getInstalledLookAndFeels()) {
            try {
                SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
                SwingUtilities.invokeAndWait(() -> initLayout(NX, NY));
                test();
            } finally {
                SwingUtilities.invokeAndWait(() -> {
                    if (window != null) {
                        window.dispose();
                    }
                    window = null;
                    synchronized (focusCnt) {
                        Arrays.fill(focusCnt, 0);
                    }
                });
            }
        }
    }

    private static void test() {
        robot.waitForIdle();
        robot.delay(1000);
        for (int i = 0; i < NX * NY - NX * NY / 2 - 1; i++) {
            robot.keyPress(KeyEvent.VK_RIGHT);
            robot.keyRelease(KeyEvent.VK_RIGHT);
            robot.waitForIdle();
        }
        for (int i = 0; i < NX * NY / 2; i++) {
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.waitForIdle();
        }
        robot.delay(200);
        synchronized (focusCnt) {
            for (int i = 0; i < NX * NY; i++) {
                if (focusCnt[i] < 1) {
                    throw new RuntimeException("Component " + i + " is not reachable in the forward focus cycle");
                } else if (focusCnt[i] > 1) {
                    throw new RuntimeException("Component " + i + " got focus more than once in the forward focus cycle");
                }
            }
        }
        for (int i = 0; i < NX * NY / 2; i++) {
            robot.keyPress(KeyEvent.VK_SHIFT);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_SHIFT);
            robot.waitForIdle();
        }
        for (int i = 0; i < NX * NY - NX * NY / 2 - 1; i++) {
            robot.keyPress(KeyEvent.VK_LEFT);
            robot.keyRelease(KeyEvent.VK_LEFT);
            robot.waitForIdle();
        }
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.waitForIdle();
        robot.delay(200);
        synchronized (focusCnt) {
            for (int i = 0; i < NX * NY; i++) {
                if (focusCnt[i] < 2) {
                    throw new RuntimeException("Component " + i + " is not reachable in the backward focus cycle");
                } else if (focusCnt[i] > 2) {
                    throw new RuntimeException("Component " + i + " got focus more than once in the backward focus cycle");
                }
            }
        }
    }

    private static void setLookAndFeel(UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
            System.out.println(laf.getName());
        } catch (UnsupportedLookAndFeelException ignored) {
            System.out.println("Unsupported LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
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
                    synchronized (focusCnt) {
                        focusCnt[fi]++;
                        JComponent btn = (JComponent) e.getSource();
                        if (focusCnt[fi] == 1) {
                            btn.setBackground(Color.yellow);
                        } else if (focusCnt[fi] == 2) {
                            btn.setBackground(Color.green);
                        } else {
                            btn.setBackground(Color.red);
                        }
                    }
                }
            });
        }
        rootPanel.add(formPanel, BorderLayout.CENTER);
        window.add(rootPanel);
        window.setLocationRelativeTo(null);
        window.pack();
        window.setVisible(true);
    }
}
