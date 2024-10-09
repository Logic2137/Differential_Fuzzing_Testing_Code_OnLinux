import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class PressedButtonRightClickTest {

    private static Robot testRobot;

    private static JFrame myFrame;

    private static JButton myButton;

    public static void main(String[] args) throws Throwable {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                constructTestUI();
            }
        });
        try {
            testRobot = new Robot();
        } catch (AWTException ex) {
            throw new RuntimeException("Exception in Robot creation");
        }
        testRobot.waitForIdle();
        test();
        disposeTestUI();
    }

    private static void test() {
        Point loc = myFrame.getLocationOnScreen();
        testRobot.mouseMove((loc.x + 100), (loc.y + 100));
        testRobot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        myButton.setText("Left button pressed");
        testRobot.delay(1000);
        testRobot.mousePress(InputEvent.BUTTON3_DOWN_MASK);
        myButton.setText("Left button pressed + Right button pressed");
        testRobot.delay(1000);
        testRobot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
        myButton.setText("Right button released");
        testRobot.delay(1000);
        boolean pressed = myButton.getModel().isPressed();
        testRobot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        if (!pressed) {
            disposeTestUI();
            throw new RuntimeException("Test Failed!");
        }
    }

    private static void disposeTestUI() {
        myFrame.setVisible(false);
        myFrame.dispose();
    }

    public static void constructTestUI() {
        myFrame = new JFrame();
        myFrame.setLayout(new BorderLayout());
        myButton = new JButton("Whatever");
        myFrame.add(myButton, BorderLayout.CENTER);
        myFrame.setSize(400, 300);
        myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myFrame.setLocationRelativeTo(null);
        myFrame.setVisible(true);
    }
}
