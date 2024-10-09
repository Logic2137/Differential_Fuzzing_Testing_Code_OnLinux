import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;
import javax.swing.JToolBar;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.Color;

public class ComponentSetNameTest {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                UIManager.LookAndFeelInfo[] looks = UIManager.getInstalledLookAndFeels();
                for (UIManager.LookAndFeelInfo look : looks) {
                    try {
                        UIManager.setLookAndFeel(look.getClassName());
                    } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
                        throw new RuntimeException("Failed to set LookAndFeel " + look.getClassName(), e);
                    }
                    final JComponent[] components = { new JTextArea(), new JTextField(), new JComboBox(), new JCheckBox(), new JRadioButton(), new JButton(), new JPanel(), new JTable(), new JTabbedPane(), new JScrollPane(), new JSlider(), new JMenu(), new JToolBar() };
                    for (JComponent component : components) {
                        testSetName(component, look.getClassName());
                    }
                }
            }
        });
    }

    private static void testSetName(JComponent component, String looknFeelClassName) {
        Color bg1 = component.getBackground();
        if (bg1 == null) {
            printTestInfo(component, looknFeelClassName);
            throw new RuntimeException("background is null");
        }
        component.setName("noname");
        Color bg2 = component.getBackground();
        if (bg2 != bg1) {
            printTestInfo(component, looknFeelClassName);
            throw new RuntimeException("background changed after setting name");
        }
    }

    private static void printTestInfo(JComponent component, String looknFeelClassName) {
        System.err.println("Test fails");
        System.err.println("LookAndFeel " + looknFeelClassName);
        System.err.println("JComponent " + component.getClass());
    }
}
