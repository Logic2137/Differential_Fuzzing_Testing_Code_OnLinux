import java.awt.Frame;
import java.awt.Robot;
import java.awt.TextField;
import java.awt.event.KeyEvent;

public class DeadKeySystemAssertionDialog {

    public static void main(String[] args) throws Exception {
        Frame frame = new Frame();
        frame.setSize(300, 200);
        TextField textField = new TextField();
        frame.add(textField);
        Robot robot = new Robot();
        robot.setAutoDelay(50);
        frame.setVisible(true);
        robot.waitForIdle();
        textField.requestFocus();
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_A);
        robot.keyRelease(KeyEvent.VK_A);
        robot.waitForIdle();
        frame.setVisible(false);
        frame.dispose();
    }
}
