import java.awt.Component;
import java.awt.Container;
import java.awt.Robot;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

public class SelectAllFilesFilterTest {

    private static final String LABEL_TEXT = "File Format:";

    private static volatile JFileChooser fileChooser;

    private static JComboBox comboBox;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(SelectAllFilesFilterTest::createAndShowGUI);
        while (fileChooser == null) {
            Thread.sleep(100);
        }
        Robot robot = new Robot();
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(() -> {
            comboBox = findComboBox(fileChooser);
            comboBox.setSelectedIndex(0);
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(() -> {
            int selectedIndex = comboBox.getSelectedIndex();
            fileChooser.setVisible(false);
            if (selectedIndex != 0) {
                throw new RuntimeException("Select All file filter is not selected!");
            }
        });
    }

    private static void createAndShowGUI() {
        fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(true);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        FileFilter txtFilter = new FileNameExtensionFilter("Text files", "txt");
        fileChooser.addChoosableFileFilter(txtFilter);
        fileChooser.setFileFilter(txtFilter);
        fileChooser.showOpenDialog(null);
    }

    private static JComboBox findComboBox(Component comp) {
        if (comp instanceof JLabel) {
            JLabel label = (JLabel) comp;
            if (LABEL_TEXT.equals(label.getText())) {
                return (JComboBox) label.getLabelFor();
            }
        }
        if (comp instanceof Container) {
            Container cont = (Container) comp;
            for (int i = 0; i < cont.getComponentCount(); i++) {
                JComboBox result = findComboBox(cont.getComponent(i));
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
