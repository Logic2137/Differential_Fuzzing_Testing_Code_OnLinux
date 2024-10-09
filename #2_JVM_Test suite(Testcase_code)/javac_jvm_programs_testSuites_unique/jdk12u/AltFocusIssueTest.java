



import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.awt.Robot;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


public class AltFocusIssueTest {

    
    private static JMenu menu;

    
    private static JTextArea ta;

    private static JFrame frame;

    
    public static void testAltEvents() throws Exception {
        Robot robot = new Robot();
        SwingUtilities.invokeAndWait(() -> {
            try {
                createUI();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(() -> ta.requestFocusInWindow());
        robot.waitForIdle();
        if (!ta.isFocusOwner()) {
            throw new RuntimeException("textarea should have input focus");
        }
        if (menu.isSelected()) {
            throw new RuntimeException("menu is selected...");
        }

        
        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyRelease(KeyEvent.VK_ALT);
        robot.waitForIdle();

        
        if (!ta.isFocusOwner()) {
            throw new RuntimeException("textarea should still have input focus");
        }
        
        if (SwingUtilities.getRootPane(ta).isFocusOwner()) {
            throw new RuntimeException("Focus should not be changed from the text area");
        }
        
        if (menu.isSelected()) {
            throw new RuntimeException("Menu must not be selected");
        }
    }

    
    private static void createUI() throws Exception {
        
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);

        menu = new JMenu("Menu");
        menu.add(new JMenuItem("Menu item"));
        menuBar.add(menu);

        ta = new JTextArea();
        frame.getContentPane().add(ta);

        ta.addKeyListener( new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ALT) {
                    
                    e.consume();
                }
            }
        });

        frame.setSize(400, 300);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        try {
            testAltEvents();
        } finally {
            SwingUtilities.invokeAndWait(() -> frame.dispose());
        }
    }
}
