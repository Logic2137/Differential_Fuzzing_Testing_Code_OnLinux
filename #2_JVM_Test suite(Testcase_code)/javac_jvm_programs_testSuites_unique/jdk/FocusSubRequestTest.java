



import java.awt.*;
import java.awt.event.*;

public class FocusSubRequestTest {
    Frame frame = new Frame("Test Frame");
    Button button = new Button("button");
    boolean passed = false;
    Robot robot;

    public static void main(final String[] args) {
        FocusSubRequestTest app = new FocusSubRequestTest();
        app.init();
        app.start();
    }

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
            robot.setAutoDelay(100);
        } catch(Exception e) {
            throw new RuntimeException("Error: unable to create robot", e);
        }
    }

    public void start() {
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        waitTillShown(button);
        frame.toFront();

        robot.delay(100);
        robot.keyPress(KeyEvent.VK_K);
        robot.keyRelease(KeyEvent.VK_K);

        robot.waitForIdle();

        if(passed) {
            System.out.println("Test passed.");
        } else {
            throw new RuntimeException("Test failed.");
        }
    }

    private void waitTillShown(Component component) {
        Point p = null;
        while (p == null) {
            try {
                p = component.getLocationOnScreen();
            } catch (IllegalStateException e) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException ie) {
                }
            }
        }
    }
}
