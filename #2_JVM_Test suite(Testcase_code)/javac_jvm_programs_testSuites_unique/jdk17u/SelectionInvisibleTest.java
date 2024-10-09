import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Robot;
import java.awt.TextField;
import java.awt.event.InputEvent;

public class SelectionInvisibleTest {

    private static final String TEXT = "One Two Three Four Five Six Seven Eight Nine ";

    private static final String LAST_WORD = "Ten";

    public static void main(String[] args) throws Exception {
        Frame frame = new Frame();
        frame.setSize(300, 200);
        TextField textField = new TextField(TEXT + LAST_WORD, 30);
        Panel panel = new Panel(new FlowLayout());
        panel.add(textField);
        frame.add(panel);
        frame.setVisible(true);
        Robot robot = new Robot();
        robot.setAutoDelay(50);
        robot.waitForIdle();
        Point point = textField.getLocationOnScreen();
        int x = point.x + textField.getWidth() / 2;
        int y = point.y + textField.getHeight() / 2;
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
        robot.mousePress(InputEvent.BUTTON1_MASK);
        int N = 10;
        int dx = textField.getWidth() / N;
        for (int i = 0; i < N; i++) {
            x += dx;
            robot.mouseMove(x, y);
        }
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
        if (!textField.getSelectedText().endsWith(LAST_WORD)) {
            throw new RuntimeException("Last word is not selected!");
        }
    }
}
