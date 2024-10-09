

import java.awt.Button;
import java.awt.Choice;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class AccessibleChoiceTest {
    
    Frame frame = new Frame("window owner");
    Window win = new Window(frame);
    Choice choice = new Choice();
    Button def = new Button("default owner");
    CountDownLatch go = new CountDownLatch(1);

    public static void main(final String[] args) {
        AccessibleChoiceTest app = new AccessibleChoiceTest();
        app.test();
    }

    private void test() {
        try {
            init();
            start();
        } finally {
            if (frame != null) frame.dispose();
            if (win != null) win.dispose();
        }
    }

    public void init() {
        win.setLayout (new FlowLayout ());
        win.add(def);
        def.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    go.countDown();
                }
            });
        choice.add("One");
        choice.add("Two");
        win.add(choice);
    }

    public void start () {
        frame.setVisible(true);
        win.pack();
        win.setLocation(100, 200);
        win.setVisible(true);

        Robot robot = null;
        try {
            robot = new Robot();
        } catch (Exception ex) {
            throw new RuntimeException("Can't create robot");
        }
        robot.delay(2000);
        robot.setAutoDelay(150);
        robot.setAutoWaitForIdle(true);

        
        Point loc = def.getLocationOnScreen();
        robot.mouseMove(loc.x+2, loc.y+2);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);

        try {
            go.await(1, TimeUnit.SECONDS);
        } catch (InterruptedException ie) {
            throw new RuntimeException("Interrupted !!!");
        }

        if (!def.isFocusOwner()) {
            throw new RuntimeException("Button doesn't have focus");
        }

        
        robot.keyPress(KeyEvent.VK_TAB);
        robot.keyRelease(KeyEvent.VK_TAB);

        robot.delay(500);

        
        
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);

        String osName = System.getProperty("os.name").toLowerCase();
        if (osName.startsWith("mac")) {
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
        }

        robot.delay(1000);

        
        if (choice.getSelectedItem() != choice.getItem(1)) {
            throw new RuntimeException("Choice can't be controlled by keyboard");
        }
    }
}
