import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Point;
import java.awt.Robot;

public class ContextualMenuClearsTextSelectionTest {

    private static Robot robot;

    private static Point p;

    private static JTextField textField;

    private static JPopupMenu popupMenu;

    private static JFrame frame;

    private static boolean isSelectionCleared;

    public static void main(String[] args) throws Exception {
        if (!System.getProperty("os.name").startsWith("Mac")) {
            System.out.println("This test is meant for Mac platform only");
            return;
        }
        try {
            robot = new Robot();
            robot.setAutoDelay(50);
            SwingUtilities.invokeAndWait(() -> {
                createAndShowGUI();
            });
            robot.waitForIdle();
            SwingUtilities.invokeAndWait(() -> {
                p = textField.getLocationOnScreen();
                textField.requestFocusInWindow();
            });
            robot.delay(200);
            robot.waitForIdle();
            robot.mouseMove(p.x + 10, p.y + 10);
            robot.waitForIdle();
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.waitForIdle();
            robot.delay(2000);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.waitForIdle();
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.waitForIdle();
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.waitForIdle();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.waitForIdle();
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.waitForIdle();
            SwingUtilities.invokeAndWait(() -> {
                isSelectionCleared = textField.getSelectedText() == null ? true : false;
            });
            if (isSelectionCleared) {
                throw new RuntimeException("Text selection is cleared");
            }
        } finally {
            if (frame != null)
                SwingUtilities.invokeAndWait(() -> frame.dispose());
        }
    }

    private static void createAndShowGUI() {
        frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.setSize(200, 200);
        textField = new JTextField("word");
        textField.setSize(100, 20);
        popupMenu = new JPopupMenu();
        popupMenu.add(new JMenuItem("Apple"));
        popupMenu.add(new JMenuItem("Pear"));
        popupMenu.add(new JMenuItem("Grape"));
        createPopup(textField);
        frame.add(textField, BorderLayout.NORTH);
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    private static void createPopup(JTextField f) {
        MouseAdapter a = new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopupMenu(e);
                }
            }

            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopupMenu(e);
                }
            }

            private void showPopupMenu(MouseEvent e) {
                popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
            }
        };
        f.addMouseListener(a);
    }
}
