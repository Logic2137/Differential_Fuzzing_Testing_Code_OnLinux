import java.awt.FlowLayout;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.Window;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class bug7072653 {

    private static JComboBox combobox;

    private static JFrame frame;

    private static Robot robot;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        UIManager.LookAndFeelInfo[] lookAndFeelArray = UIManager.getInstalledLookAndFeels();
        for (GraphicsDevice sd : ge.getScreenDevices()) {
            for (UIManager.LookAndFeelInfo lookAndFeelItem : lookAndFeelArray) {
                executeCase(lookAndFeelItem.getClassName(), sd);
                robot.waitForIdle();
            }
        }
    }

    private static void executeCase(String lookAndFeelString, GraphicsDevice sd) throws Exception {
        if (tryLookAndFeel(lookAndFeelString)) {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    setup(lookAndFeelString, sd);
                    test();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                } finally {
                    frame.dispose();
                }
            });
        }
    }

    private static void setup(String lookAndFeelString, GraphicsDevice sd) throws Exception {
        GraphicsConfiguration gc = sd.getDefaultConfiguration();
        Rectangle gcBounds = gc.getBounds();
        frame = new JFrame("JComboBox Test " + lookAndFeelString, gc);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.getContentPane().setLayout(new FlowLayout());
        frame.setLocation(gcBounds.x + gcBounds.width / 2 - frame.getWidth() / 2, gcBounds.y + gcBounds.height / 2 - frame.getHeight() / 2);
        combobox = new JComboBox(new DefaultComboBoxModel() {

            @Override
            public Object getElementAt(int index) {
                return "Element " + index;
            }

            @Override
            public int getSize() {
                return 100;
            }
        });
        combobox.setMaximumRowCount(100);
        combobox.putClientProperty("JComboBox.isPopDown", true);
        frame.getContentPane().add(combobox);
        frame.setVisible(true);
        combobox.addPopupMenuListener(new PopupMenuListener() {

            @Override
            public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
            }

            @Override
            public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
                int height = 0;
                for (Window window : JFrame.getWindows()) {
                    if (Window.Type.POPUP == window.getType()) {
                        if (window.getOwner().isVisible()) {
                            height = window.getSize().height;
                            break;
                        }
                    }
                }
                GraphicsConfiguration gc = combobox.getGraphicsConfiguration();
                Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
                int gcHeight = gc.getBounds().height;
                gcHeight = gcHeight - insets.top - insets.bottom;
                if (height == gcHeight) {
                    return;
                }
                String exception = "Popup window height " + "For LookAndFeel" + lookAndFeelString + " is wrong" + "\nShould be " + gcHeight + ", Actually " + height;
                throw new RuntimeException(exception);
            }

            @Override
            public void popupMenuCanceled(PopupMenuEvent e) {
            }
        });
    }

    private static void test() throws Exception {
        combobox.setPopupVisible(true);
        combobox.setPopupVisible(false);
    }

    private static boolean tryLookAndFeel(String lookAndFeelString) throws Exception {
        try {
            UIManager.setLookAndFeel(lookAndFeelString);
        } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            return false;
        }
        return true;
    }
}
