



import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;

public class FocusSubRequestTest extends Applet {
    Frame frame = new Frame("Test Frame");
    Button button = new Button("button");
    boolean passed = false;
    Robot robot;

    public void init() {
        frame.add(button);
        button.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    System.out.println("FocusSubRequestTest: focusGained for: " + e.getSource());
                    ((Component)e.getSource()).requestFocus();
                }
            });

        button.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    System.out.println("FocusSubRequestTest: keyPressed for: " + e.getSource());
                    passed = true;
                }
            });

        try {
            robot = new Robot();
        } catch(Exception e) {
            throw new RuntimeException("Error: unable to create robot", e);
        }
    }

    public void start() {
        frame.pack();
        frame.setLocation(getLocation().x + getSize().width + 20, 0);
        frame.setVisible(true);

        waitTillShown(button);
        frame.toFront();

        robot.delay(100);
        robot.keyPress(KeyEvent.VK_K);
        robot.delay(100);
        robot.keyRelease(KeyEvent.VK_K);

        robot.waitForIdle();

        if(passed) {
            System.out.println("Test passed.");
        } else {
            throw new RuntimeException("Test failed.");
        }
    }

    private void waitTillShown(Component component) {
        while (true) {
            try {
                Thread.sleep(100);
                component.getLocationOnScreen();
                break;
            } catch(InterruptedException ie) {
                throw new RuntimeException(ie);
            } catch(IllegalComponentStateException icse) {}
        }
    }
}
