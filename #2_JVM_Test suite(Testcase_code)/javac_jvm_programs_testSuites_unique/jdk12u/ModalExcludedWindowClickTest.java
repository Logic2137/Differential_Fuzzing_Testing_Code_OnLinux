



import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;

public class ModalExcludedWindowClickTest extends Applet {
    Robot robot;
    Frame frame = new Frame("Frame");
    Window w = new Window(frame);
    Dialog d = new Dialog ((Dialog)null, "NullParentDialog", true);
    Button button = new Button("Button");
    boolean actionPerformed = false;

    public static void main (String args[]) {
        ModalExcludedWindowClickTest app = new ModalExcludedWindowClickTest();
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

        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    actionPerformed = true;
                    System.out.println(e.paramString());
                }
            });

        EventQueue.invokeLater(new Runnable() {
                public void run() {
                    frame.setSize(200, 200);
                    frame.setVisible(true);

                    w.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
                    w.add(button);
                    w.setSize(200, 200);
                    w.setLocation(230, 230);
                    w.setVisible(true);

                    d.setSize(200, 200);
                    d.setLocation(0, 230);
                    d.setVisible(true);

                }
            });

        waitTillShown(d);

        test();
    }

    void test() {
        clickOn(button);
        waitForIdle();
        if (!actionPerformed) {
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
