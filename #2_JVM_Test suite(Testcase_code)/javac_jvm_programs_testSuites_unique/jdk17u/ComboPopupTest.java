import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ComboPopupTest {

    JFrame frame = null;

    JComboBox<String> comboBox = null;

    private volatile Point p = null;

    private volatile Dimension d = null;

    void blockTillDisplayed(JComponent comp) throws Exception {
        while (p == null) {
            try {
                SwingUtilities.invokeAndWait(() -> {
                    p = comp.getLocationOnScreen();
                    d = comboBox.getSize();
                });
            } catch (IllegalStateException e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ie) {
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        new ComboPopupTest();
    }

    public ComboPopupTest() throws Exception {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(200);
            SwingUtilities.invokeAndWait(() -> start());
            blockTillDisplayed(comboBox);
            robot.waitForIdle();
            robot.mouseMove(p.x + d.width - 1, p.y + d.height / 2);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            robot.waitForIdle();
            System.out.println("popmenu visible " + comboBox.isPopupVisible());
            if (!comboBox.isPopupVisible()) {
                throw new RuntimeException("combobox popup is not visible");
            }
        } finally {
            if (frame != null) {
                SwingUtilities.invokeAndWait(() -> frame.dispose());
            }
        }
    }

    public void start() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        comboBox = new JComboBox<String>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" });
        contentPane.setLayout(new FlowLayout());
        contentPane.add(comboBox);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
