

import java.awt.Component;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JRootPane;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.JTableHeader;

import static javax.swing.UIManager.LookAndFeelInfo;
import static javax.swing.UIManager.getInstalledLookAndFeels;


public final class InsetsEncapsulation implements Runnable {

    private final Collection<Component> failures = new ArrayList<>();

    public static void main(final String[] args) throws Exception {
        for (final LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            SwingUtilities.invokeAndWait(() -> setLookAndFeel(laf));
            SwingUtilities.invokeAndWait(new InsetsEncapsulation());
        }
    }

    @Override
    public void run() {
        runTest(new JLabel("hi"));
        runTest(new JMenu());
        runTest(new JTree());
        runTest(new JTable());
        runTest(new JMenuItem());
        runTest(new JCheckBoxMenuItem());
        runTest(new JToggleButton());
        runTest(new JSpinner());
        runTest(new JSlider());
        runTest(Box.createVerticalBox());
        runTest(Box.createHorizontalBox());
        runTest(new JTextField());
        runTest(new JTextArea());
        runTest(new JTextPane());
        runTest(new JPasswordField());
        runTest(new JFormattedTextField());
        runTest(new JEditorPane());
        runTest(new JButton());
        runTest(new JColorChooser());
        runTest(new JFileChooser());
        runTest(new JCheckBox());
        runTest(new JInternalFrame());
        runTest(new JDesktopPane());
        runTest(new JTableHeader());
        runTest(new JLayeredPane());
        runTest(new JRootPane());
        runTest(new JMenuBar());
        runTest(new JOptionPane());
        runTest(new JRadioButton());
        runTest(new JRadioButtonMenuItem());
        runTest(new JPopupMenu());
        runTest(new JScrollBar());
        runTest(new JScrollPane());
        runTest(new JViewport());
        runTest(new JSplitPane());
        runTest(new JTabbedPane());
        runTest(new JToolBar());
        runTest(new JSeparator());
        runTest(new JProgressBar());
        if (!failures.isEmpty()) {
            System.out.println("These classes failed");
            for (final Component failure : failures) {
                System.out.println(failure.getClass());
            }
            throw new RuntimeException("Test failed");
        }
    }

    void runTest(final JComponent component) {
        try {
            test(component);
        } catch (final Throwable ignored) {
            failures.add(component);
        }
    }

    void test(final JComponent component) {
        final Insets p = component.getInsets();
        p.top += 3;
        if (p.equals(component.getInsets())) {
            throw new RuntimeException("Insets altered by altering Insets!");
        }
    }

    private static void setLookAndFeel(final LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
            System.out.println("LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException |
                UnsupportedLookAndFeelException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
