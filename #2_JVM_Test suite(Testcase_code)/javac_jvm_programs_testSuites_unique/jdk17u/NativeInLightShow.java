import java.awt.*;
import java.awt.event.*;

public class NativeInLightShow {

    static boolean buttonPressed = false;

    public static void main(String[] args) throws Exception {
        Frame f = new Frame("Test");
        Robot robot = null;
        robot = new Robot();
        robot.setAutoDelay(50);
        Container c = new Container();
        c.setLayout(new BorderLayout());
        Button b = new Button("I'm should be visible!");
        b.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.out.println("Test PASSED");
                buttonPressed = true;
            }
        });
        c.add(b);
        f.add(c);
        f.pack();
        c.setVisible(false);
        c.setVisible(true);
        robot.waitForIdle();
        f.setVisible(true);
        robot.waitForIdle();
        Point buttonLocation = b.getLocationOnScreen();
        robot.mouseMove(buttonLocation.x + 5, buttonLocation.y + 5);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
        robot.delay(100);
        if (!buttonPressed) {
            System.out.println("Test FAILED");
            throw new RuntimeException("Button was not pressed");
        }
    }
}
