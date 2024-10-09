



import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.reflect.InvocationTargetException;

public class ModalDialogInitialFocusTest extends Applet {
    Robot robot;

    Dialog dialog = new Dialog((Window)null, "Test Dialog", Dialog.ModalityType.TOOLKIT_MODAL);
    Button button = new Button("button");

    volatile static boolean passed = true;

    public static void main(String[] args) {
        ModalDialogInitialFocusTest app = new ModalDialogInitialFocusTest();
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

        dialog.setLayout(new FlowLayout());
        dialog.add(button);
        dialog.setBounds(800, 0, 100, 100);

        dialog.addFocusListener(new FocusAdapter() {
                
                public void focusGained(FocusEvent e) {
                    passed = false;
                }
            });

        test();
    }

    void test() {
        new Thread(new Runnable() {
                public void run() {
                  dialog.setVisible(true);
                }
            }).start();

        waitTillShown(dialog);

        robot.waitForIdle();

        dialog.dispose();

        if (passed) {
            System.out.println("Test passed.");
        } else {
            throw new RuntimeException("Test failed: dialog requests extra focus on show!");
        }
    }

    void waitTillShown(Component c) {
        while (true) {
            try {
                Thread.sleep(100);
                c.getLocationOnScreen();
                break;
            } catch (InterruptedException ie) {
                ie.printStackTrace();
                break;
            } catch (IllegalComponentStateException e) {
            }
        }
    }
}
