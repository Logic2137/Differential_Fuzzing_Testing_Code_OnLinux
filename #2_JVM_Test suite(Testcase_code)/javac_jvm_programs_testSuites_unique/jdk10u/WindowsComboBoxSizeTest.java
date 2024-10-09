



import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.Robot;

public class WindowsComboBoxSizeTest {
    private static JTextField textField;
    private static JComboBox<String> comboBox;
    private static JComboBox<String> comboBoxEd;
    private static JFrame frame;

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        SwingUtilities.invokeAndWait(() -> {
            frame = new JFrame();
            frame.getContentPane().setLayout( new FlowLayout() );

            textField = new JTextField("item 1");
            frame.getContentPane().add(textField);

            comboBox = new JComboBox<>(new String[]
                                                {"item 1", "item 2", "item 3"});
            frame.getContentPane().add(comboBox);

            comboBoxEd = new JComboBox<>(new String[]
                                                {"item 1", "item 2", "item 3"});
            comboBoxEd.setEditable( true );
            frame.getContentPane().add(comboBoxEd);

            frame.pack();
            frame.setVisible( true );
        });
        Robot robot = new Robot();
        robot.waitForIdle();

        try {
            test();
        } finally {
            SwingUtilities.invokeLater(frame::dispose);
        }
    }

    private static void test() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            int expected = textField.getSize().height;
            if (comboBox.getSize().height != expected ) {
                throw new RuntimeException(
                        "Wrong non-editable JComboBox height " +
                                              comboBox.getSize().height);
            }
            if (comboBoxEd.getSize().height != expected ) {
                throw new RuntimeException(
                        "Wrong editable JComboBox height " +
                                            comboBoxEd.getSize().height);
            }
        });
    }
}

