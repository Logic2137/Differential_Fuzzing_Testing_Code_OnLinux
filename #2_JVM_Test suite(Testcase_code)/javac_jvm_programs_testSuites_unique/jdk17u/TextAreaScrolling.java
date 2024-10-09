import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.TextArea;
import java.awt.event.InputEvent;

public class TextAreaScrolling {

    Frame mainFrame;

    TextArea textArea;

    Robot robot;

    TextAreaScrolling() {
        try {
            robot = new Robot();
        } catch (Exception ex) {
            throw new RuntimeException("Robot Creation Failed");
        }
        mainFrame = new Frame();
        mainFrame.setSize(200, 200);
        mainFrame.setLocation(200, 200);
        textArea = new TextArea();
        textArea.setText("1234 5678");
        textArea.setSelectionStart(3);
        textArea.setSelectionEnd(4);
        mainFrame.add(textArea);
        mainFrame.setVisible(true);
        textArea.requestFocusInWindow();
    }

    public void dispose() {
        if (mainFrame != null) {
            mainFrame.dispose();
        }
    }

    public void performTest() {
        robot.waitForIdle();
        robot.delay(1000);
        Point loc = textArea.getLocationOnScreen();
        Rectangle textAreaBounds = new Rectangle();
        textArea.getBounds(textAreaBounds);
        robot.mouseMove(loc.x + textAreaBounds.width / 2, loc.y + 5);
        robot.waitForIdle();
        robot.delay(500);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseMove(loc.x - 5, loc.y + 5);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
        robot.delay(500);
        robot.mouseMove(loc.x + 5, loc.y + 5);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
        robot.delay(500);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(500);
        robot.waitForIdle();
        if (textArea.getSelectedText().contentEquals("5678")) {
            dispose();
            throw new RuntimeException("TextArea over scrolled towards left. " + "Expected selected text: '1234 ' and for mac '1234' " + "Actual selected text: 5678");
        }
    }

    public static void main(String[] argv) throws RuntimeException {
        TextAreaScrolling test = new TextAreaScrolling();
        test.performTest();
        test.dispose();
    }
}
