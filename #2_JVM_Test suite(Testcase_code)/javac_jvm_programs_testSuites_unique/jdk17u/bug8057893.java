import java.awt.EventQueue;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class bug8057893 {

    private static volatile boolean isComboBoxEdited = false;

    private static JFrame frame;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(100);
        EventQueue.invokeAndWait(() -> {
            frame = new JFrame();
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            JComboBox<String> comboBox = new JComboBox<>(new String[] { "one", "two" });
            comboBox.setEditable(true);
            comboBox.addActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent e) {
                    if ("comboBoxEdited".equals(e.getActionCommand())) {
                        isComboBoxEdited = true;
                    }
                }
            });
            frame.add(comboBox);
            frame.pack();
            frame.setVisible(true);
            frame.setLocationRelativeTo(null);
            comboBox.requestFocusInWindow();
        });
        robot.waitForIdle();
        robot.delay(1000);
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.waitForIdle();
        if (frame != null)
            EventQueue.invokeAndWait(() -> frame.dispose());
        if (!isComboBoxEdited) {
            throw new RuntimeException("ComboBoxEdited event is not fired!");
        }
    }
}
