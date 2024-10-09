



import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;


public class TestIntrospector {
    private static final Class[] TYPES = {
            javax.swing.Box.class,
            javax.swing.DefaultComboBoxModel.class,
            javax.swing.DefaultCellEditor.class,
            javax.swing.DefaultComboBoxModel.class,
            javax.swing.DefaultListModel.class,
            javax.swing.ImageIcon.class,
            javax.swing.JApplet.class,
            javax.swing.JButton.class,
            javax.swing.JCheckBox.class,
            javax.swing.JColorChooser.class,
            javax.swing.JComboBox.class,
            javax.swing.JDesktopPane.class,
            javax.swing.JDialog.class,
            javax.swing.JEditorPane.class,
            javax.swing.JFileChooser.class,
            javax.swing.JFrame.class,
            javax.swing.JInternalFrame.class,
            javax.swing.JLabel.class,
            javax.swing.JList.class,
            javax.swing.JMenu.class,
            javax.swing.JMenuBar.class,
            javax.swing.JMenuItem.class,
            javax.swing.JOptionPane.class,
            javax.swing.JPanel.class,
            javax.swing.JPasswordField.class,
            javax.swing.JPopupMenu.class,
            javax.swing.JProgressBar.class,
            javax.swing.JRadioButton.class,
            javax.swing.JRadioButtonMenuItem.class,
            javax.swing.JRootPane.class,
            javax.swing.JScrollPane.class,
            javax.swing.JSeparator.class,
            javax.swing.JSlider.class,
            javax.swing.JSplitPane.class,
            javax.swing.JTabbedPane.class,
            javax.swing.JTable.class,
            javax.swing.JTextField.class,
            javax.swing.JTextArea.class,
            javax.swing.JTextPane.class,
            javax.swing.JToggleButton.class,
            javax.swing.JToolBar.class,
            javax.swing.JToolTip.class,
            javax.swing.JTree.class,
            javax.swing.JWindow.class,
            java.awt.Button.class,
            java.awt.Canvas.class,
            java.awt.Checkbox.class,
            java.awt.Choice.class,
            java.awt.Dialog.class,
            java.awt.FileDialog.class,
            java.awt.Frame.class,
            java.awt.Image.class,
            java.awt.List.class,
            java.awt.Menu.class,
            java.awt.MenuBar.class,
            java.awt.MenuItem.class,
            java.awt.Panel.class,
            java.awt.Point.class,
            java.awt.Rectangle.class,
            java.awt.Scrollbar.class,
            java.awt.TextArea.class,
            java.awt.TextField.class,
            java.awt.Window.class,
    };

    public static void main(String[] args) throws IntrospectionException {
        StringBuilder sb = null;
        if (args.length > 0) {
            if (args[0].equals("show")) {
                sb = new StringBuilder(65536);
            }
        }
        Introspector.flushCaches();
        int count = (sb != null) ? 10 : 100;
        long time = -System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            test(sb);
            test(sb);
            Introspector.flushCaches();
        }
        time += System.currentTimeMillis();
        System.out.println("Time (average): " + time / count);
    }

    private static void test(StringBuilder sb) throws IntrospectionException {
        long time = 0L;
        if (sb != null) {
            sb.append("Time\t#Props\t#Events\t#Methods\tClass\n");
            sb.append("----------------------------------------");
            time = -System.currentTimeMillis();
        }
        for (Class type : TYPES) {
            test(sb, type);
        }
        if (sb != null) {
            time += System.currentTimeMillis();
            sb.append("\nTime: ").append(time).append(" ms\n");
            System.out.println(sb);
            sb.setLength(0);
        }
    }

    private static void test(StringBuilder sb, Class type) throws IntrospectionException {
        long time = 0L;
        if (sb != null) {
            time = -System.currentTimeMillis();
        }
        BeanInfo info = Introspector.getBeanInfo(type);
        if (sb != null) {
            time += System.currentTimeMillis();
            sb.append('\n').append(time);
            sb.append('\t').append(info.getPropertyDescriptors().length);
            sb.append('\t').append(info.getEventSetDescriptors().length);
            sb.append('\t').append(info.getMethodDescriptors().length);
            sb.append('\t').append(type.getName());
        }
    }
}
