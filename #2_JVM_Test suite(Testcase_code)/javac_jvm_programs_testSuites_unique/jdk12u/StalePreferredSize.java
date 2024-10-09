

import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolTip;
import javax.swing.JTree;
import javax.swing.SpinnerListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;

import static javax.swing.UIManager.getInstalledLookAndFeels;


public final class StalePreferredSize {

    
    static final String TEXT[] = new String[]{
            "<span>A few words to get started before the "
                    + "bug</span><span>overlapping text</span>",
            "A quick brown fox jumps over the lazy dog",
            "El veloz murciélago hindú comía feliz cardillo y kiwi. La cigüeña "
                    + "tocaba el saxofón detrás del palenque de paja",
            "Voix ambiguë d’un cœur qui au zéphyr préfère les jattes de kiwis",
            "다람쥐 헌 쳇바퀴에 타고파",
            "Съешь ещё этих мягких французских булок да выпей же чаю"};

    static JFrame frame;
    static Component component;
    static int typeFont = 0; 

    public static void main(final String[] args) throws Exception {
        for (final UIManager.LookAndFeelInfo laf : getInstalledLookAndFeels()) {
            EventQueue.invokeAndWait(() -> setLookAndFeel(laf));
            for (typeFont = 0; typeFont < 3; typeFont++) {
                System.err.println("typeFont = " + typeFont);
                for (final boolean html : new boolean[]{true, false}) {
                    for (String text : TEXT) {
                        if (html) {
                            text = "<html>" + text + "</html>";
                        }
                        test(text);
                    }
                }
            }
        }
    }

    private static void test(String text) throws Exception {
        System.err.println("text = " + text);
        
        final List<Callable<Component>> comps = List.of(
                () -> new JLabel(text),
                () -> new JButton(text),
                () -> new JMenuItem(text),
                () -> new JMenu(text),
                () -> new JList<>(new String[]{text}),
                () -> new JComboBox<>(new String[]{text}),
                () -> new JTextField(text),
                () -> new JTextArea(text),
                () -> new JCheckBox(text),
                () -> new JFormattedTextField(text),
                () -> new JRadioButton(text),
                () -> new JTree(new DefaultMutableTreeNode(text)),
                () -> new JSpinner(new SpinnerListModel(new String[]{text})),
                () -> {
                    JToolTip tip = new JToolTip();
                    tip.setTipText(text);
                    return tip;
                    },
                () -> {
                    JEditorPane pane = new JEditorPane();
                    pane.setText(text);
                    return pane;
                    },
                () -> {
                    JTable table = new JTable(1, 1);
                    table.getModel().setValueAt(text, 0, 0);
                    return table;
                    }
        );

        for (final Callable<Component> creator : comps) {
            checkComponent(creator);
        }
    }

    static void checkComponent(Callable<Component> creator) throws Exception {
        EventQueue.invokeAndWait(() -> {

            try {
                component = creator.call();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            Font font = component.getFont();
            if (typeFont == 1) {
                component.setFont(new Font(font.deriveFont(Font.BOLD).getAttributes()));
            }
            if (typeFont == 2) {
                component.setFont(new Font(font.deriveFont(Font.ITALIC).getAttributes()));
            }

            frame = new JFrame();
            frame.setLayout(new FlowLayout());
            frame.add(new JScrollPane(component));
            frame.setSize(300, 100);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });

        EventQueue.invokeAndWait(() -> {

            
            
            Dimension before = component.getPreferredSize();
            SwingUtilities.updateComponentTreeUI(frame);
            Dimension after = component.getPreferredSize();

            
            
            component.setFont(component.getFont().deriveFont(35f));
            Dimension last = component.getPreferredSize();

            frame.dispose();

            if (!Objects.equals(before, after)) {
                System.err.println("Component: " + component);
                System.err.println("Before: " + before);
                System.err.println("After: " + after);
                throw new RuntimeException("Wrong PreferredSize");
            }
            






        });
    }

    private static void setLookAndFeel(final UIManager.LookAndFeelInfo laf) {
        try {
            UIManager.setLookAndFeel(laf.getClassName());
            System.err.println("LookAndFeel: " + laf.getClassName());
        } catch (final UnsupportedLookAndFeelException ignored) {
            System.err.println(
                    "Unsupported LookAndFeel: " + laf.getClassName());
        } catch (ClassNotFoundException | InstantiationException |
                IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
