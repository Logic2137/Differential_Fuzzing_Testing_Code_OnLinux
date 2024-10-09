



import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class ButtonGroupFocusTest {

    private static JRadioButton button1;
    private static JRadioButton button2;
    private static JRadioButton button3;
    private static JRadioButton button4;
    private static JRadioButton button5;
    private static Robot robot;
    private static JFrame frame;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.setAutoDelay(100);

        SwingUtilities.invokeAndWait(() -> {
            frame = new JFrame();
            Container contentPane = frame.getContentPane();
            contentPane.setLayout(new FlowLayout());
            button1 = new JRadioButton("Button 1");
            contentPane.add(button1);
            button2 = new JRadioButton("Button 2");
            contentPane.add(button2);
            button3 = new JRadioButton("Button 3");
            contentPane.add(button3);
            button4 = new JRadioButton("Button 4");
            contentPane.add(button4);
            button5 = new JRadioButton("Button 5");
            contentPane.add(button5);
            ButtonGroup group = new ButtonGroup();
            group.add(button1);
            group.add(button2);
            group.add(button3);

            group = new ButtonGroup();
            group.add(button4);
            group.add(button5);

            button2.setSelected(true);

            frame.pack();
            frame.setVisible(true);
        });

        robot.waitForIdle();
        robot.delay(200);

        SwingUtilities.invokeAndWait(() -> {
            if( !button2.hasFocus() ) {
                frame.dispose();
                throw new RuntimeException(
                        "Button 2 should get focus after activation");
            }
        });

        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);

        robot.waitForIdle();
        robot.delay(200);

        SwingUtilities.invokeAndWait(() -> {
            if( !button4.hasFocus() ) {
                frame.dispose();
                throw new RuntimeException(
                        "Button 4 should get focus");
            }
            button3.setSelected(true);
        });

        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);

        robot.waitForIdle();
        robot.delay(200);

        SwingUtilities.invokeAndWait(() -> {
            if( !button3.hasFocus() ) {
                frame.dispose();
                throw new RuntimeException(
                        "selected Button 3 should get focus");
            }
        });

        SwingUtilities.invokeLater(frame::dispose);
    }
}
