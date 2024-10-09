



import java.awt.Frame;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.TextArea;
import java.awt.event.InputEvent;

public class OverScrollTest {
    Frame mainFrame;
    TextArea textArea;
    Robot robot;

    OverScrollTest() {
        try {
            robot = new Robot();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }

        mainFrame = new Frame();
        mainFrame.setSize(400, 200);
        mainFrame.setLocation(200, 200);
        mainFrame.setLayout(new FlowLayout());

        textArea = new TextArea(2, 10);
        textArea.setSize(300, 100);
        textArea.setText("123456 789123");
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
        Point loc = textArea.getLocationOnScreen();
        Rectangle textAreaBounds = new Rectangle();
        textArea.getBounds(textAreaBounds);

        
        robot.mouseMove(loc.x + textAreaBounds.width / 2, loc.y + 5);

        
        robot.mousePress(InputEvent.BUTTON1_MASK);
        for (int i = 0; i < textAreaBounds.width; i += 15) {
            robot.mouseMove(i + loc.x + textAreaBounds.width / 2, loc.y + 5);
            robot.delay(10);
        }
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();

        
        robot.mouseMove(loc.x + 5, loc.y + 5);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(100);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();

        dispose();
        if (!textArea.getSelectedText().contains("123456")) {
            throw new RuntimeException ("TextArea over scrolled towards right. "
                + "Selected text should contain: '123456' "
                + "Actual selected test: '" + textArea.getSelectedText() + "'");
        }
    }

    public static void main(String argv[]) throws RuntimeException {
        OverScrollTest test = new OverScrollTest();
        test.performTest();
    }
}
