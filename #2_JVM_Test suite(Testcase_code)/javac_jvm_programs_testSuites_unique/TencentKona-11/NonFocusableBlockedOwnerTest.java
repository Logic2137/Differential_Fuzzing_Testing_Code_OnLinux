



import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;

public class NonFocusableBlockedOwnerTest extends Applet {
    Robot robot;
    Frame frame = new Frame("Modal Blocked Frame");
    Dialog dialog = new Dialog(frame, "Modal Dialog", true);
    Window excluded = new Window(frame);
    Button button = new Button("button");

    public static void main(String[] args) {
        NonFocusableBlockedOwnerTest app = new NonFocusableBlockedOwnerTest();
        app.init();
        app.start();
    }

    public void init() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Error: unable to create robot", e);
        }
        
        
        
        this.setLayout (new BorderLayout ());
    }

    public void start() {

        if ("sun.awt.motif.MToolkit".equals(Toolkit.getDefaultToolkit().getClass().getName())) {
            System.out.println("No testing on MToolkit.");
            return;
        }

        try {
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    frame.setSize(300, 200);
                    frame.setVisible(true);

                    excluded.setSize(300, 200);
                    excluded.setLocation(0, 400);
                    excluded.setModalExclusionType(Dialog.ModalExclusionType.TOOLKIT_EXCLUDE);
                    excluded.setLayout(new FlowLayout());
                    excluded.add(button);
                    excluded.setVisible(true);

                    dialog.setSize(200, 100);
                    dialog.setLocation(0, 250);
                    dialog.setVisible(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        waitTillShown(dialog);
        clickOn(button);
        if (frame == KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow()) {
            throw new RuntimeException("Test failed!");
        }
        if (excluded == KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusedWindow()) {
            throw new RuntimeException("Test failed!");
        }
        if (button == KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()) {
            throw new RuntimeException("Test failed!");
        }
        System.out.println("Test passed.");
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
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        waitForIdle();
    }

    void waitTillShown(Component c) {
        while (true) {
            try {
                Thread.sleep(100);
                c.getLocationOnScreen();
                break;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IllegalComponentStateException e) {}
        }
    }
    void waitForIdle() {
        try {
            robot.waitForIdle();
            EventQueue.invokeAndWait( new Runnable() {
                    public void run() {} 
                });
        } catch(InterruptedException ie) {
            System.out.println("waitForIdle, non-fatal exception caught:");
            ie.printStackTrace();
        } catch(InvocationTargetException ite) {
            System.out.println("waitForIdle, non-fatal exception caught:");
            ite.printStackTrace();
        }

        
        robot.delay(200);
    }
}
