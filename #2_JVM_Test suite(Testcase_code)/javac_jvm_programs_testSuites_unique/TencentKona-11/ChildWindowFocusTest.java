



import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.lang.reflect.*;

public class ChildWindowFocusTest extends Applet {
    Robot robot;
    Frame frame = new Frame("Owner");
    Button button0 = new Button("button-0");
    TextField text0 = new TextField("text-0");
    TextField text1 = new TextField("text-1");
    Window win1 = new TestWindow(frame, text0, 110);
    Window win2 = new TestWindow(win1, text1, 220);
    Frame outerFrame = new Frame("Outer");
    Button button1 = new Button("button-1");
    int shift;

    public void init() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Error: unable to create robot", e);
        }
        
        
        
        this.setLayout (new BorderLayout ());
        shift = 100;
    }

    public void start() {

        frame.setBounds(0, 50, 400, 100);
        frame.setLayout(new FlowLayout());
        frame.add(button0);

        outerFrame.setBounds(0, 390, 400, 100);
        outerFrame.setLayout(new FlowLayout());
        outerFrame.add(button1);

        adjustAndShow(new Component[] {frame, win1, win2, outerFrame});
        robot.waitForIdle();

        test();
    }

    void adjustAndShow(Component[] comps) {
        for (Component comp: comps) {
            comp.setLocation(shift, (int)comp.getLocation().getY());
            comp.setVisible(true);
            robot.waitForIdle();
        }
    }

    void test() {
        clickOnCheckFocusOwner(button0);
        clickOnCheckFocusOwner(text1);
        clickOnCheckFocusOwner(button1);
        clickOn(frame);
        checkFocusOwner(text1);
        clickOnCheckFocusOwner(text0);
        clickOnCheckFocusOwner(button1);
        clickOn(frame);
        checkFocusOwner(text0);

        System.out.println("Test passed.");
    }

    void clickOnCheckFocusOwner(Component c) {
        clickOn(c);
        if (!checkFocusOwner(c)) {
            throw new RuntimeException("Test failed: couldn't focus <" + c + "> by mouse click!");
        }
    }

    boolean checkFocusOwner(Component comp) {
        return (comp == KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner());
    }

    void clickOn(Component c) {
        Point p = c.getLocationOnScreen();
        Dimension d = c.getSize();

        System.out.println("Clicking " + c);

        if (c instanceof Frame) {
            robot.mouseMove(p.x + (int)(d.getWidth()/2), p.y + ((Frame)c).getInsets().top/2);
        } else {
            robot.mouseMove(p.x + (int)(d.getWidth()/2), p.y + (int)(d.getHeight()/2));
        }
        robot.delay(50);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(50);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
    }

}

class TestWindow extends Window {
    TestWindow(Window owner, Component comp, int x) {
        super(owner);
        setBackground(Color.blue);
        setLayout(new FlowLayout());
        add(comp);
        comp.setBackground(Color.yellow);
        setBounds(0, x, 100, 100);
    }
}
