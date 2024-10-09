import javax.swing.*;
import javax.swing.plaf.basic.*;

public class bug4928019 {

    public static void main(String[] args) throws Throwable {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            System.err.println("GTKLookAndFeel is not supported on this platform." + " Test is considered passed.");
            return;
        } catch (ClassNotFoundException ex) {
            System.err.println("GTKLookAndFeel class is not found." + " Test is considered passed.");
            return;
        }
        new JButton() {

            public void updateUI() {
                setUI(new BasicButtonUI());
            }
        };
        new JCheckBox() {

            public void updateUI() {
                setUI(new BasicCheckBoxUI());
            }
        };
        new JCheckBoxMenuItem() {

            public void updateUI() {
                setUI(new BasicCheckBoxMenuItemUI());
            }
        };
        new JColorChooser() {

            public void updateUI() {
                setUI(new BasicColorChooserUI());
            }
        };
        new JComboBox() {

            public void updateUI() {
                setUI(new BasicComboBoxUI());
            }
        };
        new JDesktopPane() {

            public void updateUI() {
                setUI(new BasicDesktopPaneUI());
            }
        };
        new JEditorPane() {

            public void updateUI() {
                setUI(new BasicEditorPaneUI());
            }
        };
        new JFileChooser() {

            public void updateUI() {
                setUI(new BasicFileChooserUI(null));
            }
        };
        new JFormattedTextField() {

            public void updateUI() {
                setUI(new BasicFormattedTextFieldUI());
            }
        };
        new JInternalFrame() {

            public void updateUI() {
                setUI(new BasicInternalFrameUI(null));
            }
        };
        new JLabel() {

            public void updateUI() {
                setUI(new BasicLabelUI());
            }
        };
        new JList() {

            public void updateUI() {
                setUI(new BasicListUI());
            }
        };
        new JMenuBar() {

            public void updateUI() {
                setUI(new BasicMenuBarUI());
            }
        };
        new JMenuItem() {

            public void updateUI() {
                setUI(new BasicMenuItemUI());
            }
        };
        new JMenu() {

            public void updateUI() {
                setUI(new BasicMenuUI());
            }
        };
        new JOptionPane() {

            public void updateUI() {
                setUI(new BasicOptionPaneUI());
            }
        };
        new JPanel() {

            public void updateUI() {
                setUI(new BasicPanelUI());
            }
        };
        new JPasswordField() {

            public void updateUI() {
                setUI(new BasicPasswordFieldUI());
            }
        };
        new JPopupMenu() {

            public void updateUI() {
                setUI(new BasicPopupMenuUI());
            }
        };
        new JProgressBar() {

            public void updateUI() {
                setUI(new BasicProgressBarUI());
            }
        };
        new JRadioButton() {

            public void updateUI() {
                setUI(new BasicRadioButtonUI());
            }
        };
        new JRadioButtonMenuItem() {

            public void updateUI() {
                setUI(new BasicRadioButtonMenuItemUI());
            }
        };
        new JRootPane() {

            public void updateUI() {
                setUI(new BasicRootPaneUI());
            }
        };
        new JScrollBar() {

            public void updateUI() {
                setUI(new BasicScrollBarUI());
            }
        };
        new JScrollPane() {

            public void updateUI() {
                setUI(new BasicScrollPaneUI());
            }
        };
        new JSeparator() {

            public void updateUI() {
                setUI(new BasicSeparatorUI());
            }
        };
        new JSlider() {

            public void updateUI() {
                setUI(new BasicSliderUI(null));
            }
        };
        new JSpinner() {

            public void updateUI() {
                setUI(new BasicSpinnerUI());
            }
        };
        new JSplitPane() {

            public void updateUI() {
                setUI(new BasicSplitPaneUI());
            }
        };
        new JTabbedPane() {

            public void updateUI() {
                setUI(new BasicTabbedPaneUI());
            }
        };
        new JTable() {

            public void updateUI() {
                setUI(new BasicTableUI());
            }
        };
        new JTextArea() {

            public void updateUI() {
                setUI(new BasicTextAreaUI());
            }
        };
        new JTextField() {

            public void updateUI() {
                setUI(new BasicTextFieldUI());
            }
        };
        new JTextPane() {

            public void updateUI() {
                setUI(new BasicTextPaneUI());
            }
        };
        new JToggleButton() {

            public void updateUI() {
                setUI(new BasicToggleButtonUI());
            }
        };
        new JToolBar() {

            public void updateUI() {
                setUI(new BasicToolBarUI());
            }
        };
        new JToolTip() {

            public void updateUI() {
                setUI(new BasicToolTipUI());
            }
        };
        new JTree() {

            public void updateUI() {
                setUI(new BasicTreeUI());
            }
        };
        new JViewport() {

            public void updateUI() {
                setUI(new BasicViewportUI());
            }
        };
        System.out.println("DONE");
    }
}
