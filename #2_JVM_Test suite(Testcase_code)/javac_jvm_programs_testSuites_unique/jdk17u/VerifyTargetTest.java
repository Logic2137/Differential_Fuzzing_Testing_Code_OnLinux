import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class VerifyTargetTest extends InputVerifier implements FocusListener {

    static boolean success;

    private static JFrame frame;

    private static JTextField field2;

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> setup());
        try {
            Robot robot = new Robot();
            robot.waitForIdle();
            robot.delay(200);
            KeyboardFocusManager.getCurrentKeyboardFocusManager().focusNextComponent();
            robot.waitForIdle();
            robot.delay(200);
            if (!success) {
                throw new RuntimeException("Failed");
            } else {
                System.out.println("ok");
            }
        } finally {
            SwingUtilities.invokeLater(() -> frame.dispose());
        }
    }

    static void setup() {
        frame = new JFrame();
        JTextField field1 = new JTextField("Input 1");
        VerifyTargetTest test = new VerifyTargetTest();
        field1.setInputVerifier(test);
        field1.addFocusListener(test);
        frame.getContentPane().add(field1, BorderLayout.NORTH);
        field2 = new JTextField("Input 2");
        frame.getContentPane().add(field2, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public boolean verify(JComponent input) {
        return true;
    }

    @Override
    public boolean verifyTarget(JComponent input) {
        success = input == field2;
        return false;
    }

    @Override
    public void focusGained(FocusEvent e) {
    }

    @Override
    public void focusLost(FocusEvent e) {
        success = false;
    }
}
