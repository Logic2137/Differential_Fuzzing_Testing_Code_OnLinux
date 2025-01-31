import java.awt.Robot;
import java.awt.BorderLayout;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import javax.swing.JFrame;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class JSpinnerButtonFocusTest {

    static JFrame frame;

    static Robot robot;

    static JSpinner spinner1, spinner2;

    static DefaultEditor editor1, editor2;

    static volatile boolean isJTextFieldFocused;

    static volatile CountDownLatch latch1;

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo LF : UIManager.getInstalledLookAndFeels()) {
            latch1 = new CountDownLatch(1);
            try {
                UIManager.setLookAndFeel(LF.getClassName());
                robot = new Robot();
                robot.setAutoDelay(50);
                SwingUtilities.invokeAndWait(() -> {
                    frame = new JFrame();
                    spinner1 = new JSpinner();
                    spinner2 = new JSpinner();
                    frame.setLayout(new BorderLayout());
                    frame.getContentPane().add(spinner1, BorderLayout.NORTH);
                    frame.getContentPane().add(spinner2, BorderLayout.SOUTH);
                    editor1 = ((DefaultEditor) spinner1.getEditor());
                    editor1.setFocusable(false);
                    spinner1.setFocusable(false);
                    editor2 = (DefaultEditor) spinner2.getEditor();
                    editor2.setFocusable(false);
                    spinner2.setFocusable(false);
                    frame.setFocusTraversalPolicy(new ContainerOrderFocusTraversalPolicy());
                    frame.setFocusTraversalPolicyProvider(true);
                    frame.setAlwaysOnTop(true);
                    frame.pack();
                    frame.setVisible(true);
                });
                robot.waitForIdle();
                editor1.getTextField().addFocusListener(new FocusAdapter() {

                    @Override
                    public void focusGained(FocusEvent e) {
                        super.focusGained(e);
                        robot.keyPress(KeyEvent.VK_TAB);
                        robot.keyRelease(KeyEvent.VK_TAB);
                        latch1.countDown();
                    }
                });
                SwingUtilities.invokeAndWait(() -> {
                    editor1.getTextField().requestFocusInWindow();
                });
                if (!latch1.await(15, TimeUnit.MINUTES)) {
                    throw new RuntimeException(LF.getClassName() + ": Timeout waiting for editor1 to gain focus.");
                }
                robot.waitForIdle();
                SwingUtilities.invokeAndWait(() -> {
                    isJTextFieldFocused = editor2.getTextField().isFocusOwner();
                });
                if (!isJTextFieldFocused) {
                    throw new RuntimeException(LF.getClassName() + ": Spinner's Text Field doesn't have focus ");
                }
            } finally {
                if (frame != null) {
                    SwingUtilities.invokeAndWait(frame::dispose);
                }
            }
        }
    }
}
