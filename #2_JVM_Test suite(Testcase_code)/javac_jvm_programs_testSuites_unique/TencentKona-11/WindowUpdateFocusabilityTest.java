



import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.lang.reflect.*;

public class WindowUpdateFocusabilityTest extends Applet {
    Robot robot;
    boolean focusGained = false;
    final Object monitor = new Object();
    FocusListener listener = new FocusAdapter () {
            public void focusGained(FocusEvent e) {
                System.out.println(e.toString());
                synchronized (monitor) {
                    focusGained = true;
                    monitor.notifyAll();
                }
            }
        };

    public static void main(String[] args) {
        WindowUpdateFocusabilityTest app = new WindowUpdateFocusabilityTest();
        app.init();
        app.start();
    }

    public void init() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Error: couldn't create robot");
        }
        
        
        
        this.setLayout (new BorderLayout ());
    }

    public void start() {
        if ("sun.awt.motif.MToolkit".equals(Toolkit.getDefaultToolkit().getClass().getName())) {
            System.out.println("No testing on Motif.");
            return;
        }

        test(new Frame("Frame owner"));
        Frame dialog_owner = new Frame("dialog's owner");
        test(new Dialog(dialog_owner));
        test(new Dialog(dialog_owner, Dialog.ModalityType.DOCUMENT_MODAL));
        test(new Dialog(dialog_owner, Dialog.ModalityType.APPLICATION_MODAL));
        test(new Dialog(dialog_owner, Dialog.ModalityType.TOOLKIT_MODAL));
        test(new Dialog((Window) null, Dialog.ModalityType.MODELESS));
        test(new Dialog((Window) null, Dialog.ModalityType.DOCUMENT_MODAL));
        test(new Dialog((Window) null, Dialog.ModalityType.APPLICATION_MODAL));
        test(new Dialog((Window) null, Dialog.ModalityType.TOOLKIT_MODAL));
        dialog_owner.dispose();
    }

    private void test(final Window owner)
    {
        Window window0 = new Window(owner); 
        Window window1 = new Window(window0);
        Window window2 = new Window(window1);
        Button button1 = new Button("button1");
        Button button2 = new Button("button2");
        button1.addFocusListener(listener);
        button2.addFocusListener(listener);

        owner.setBounds(800, 0, 100, 100);
        window1.setBounds(800, 300, 100, 100);
        window2.setBounds(800, 150, 100, 100);

        window1.add(button1);
        window2.add(button2);

        window2.setVisible(true);
        window1.setVisible(true);
        EventQueue.invokeLater(new Runnable() {
                public void run() {
                    owner.setVisible(true);
                }
            });

        try {
            EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        
                    }
                });
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        } catch (InvocationTargetException ite) {
            throw new RuntimeException(ite);
        }

        robot.delay(1000);

        clickOn(button1);

        if (!isFocusGained()) {
            throw new RuntimeException("Test failed: window1 is not focusable!");
        }

        focusGained = false;
        clickOn(button2);

        if (!isFocusGained()) {
            throw new RuntimeException("Test failed: window2 is not focusable!");
        }

        System.out.println("Test for " + owner.getName() + " passed.");
        owner.dispose();
    }

    void clickOn(Component c) {
        Point p = c.getLocationOnScreen();
        Dimension d = c.getSize();

        System.out.println("Clicking " + c);

        robot.mouseMove(p.x + (int)(d.getWidth()/2), p.y + (int)(d.getHeight()/2));

        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        waitForIdle();
    }

    void waitForIdle() {
        try {
            robot.waitForIdle();
            robot.delay(50);
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
    }

    boolean isFocusGained() {
        synchronized (monitor) {
            if (!focusGained) {
                try {
                    monitor.wait(3000);
                } catch (InterruptedException e) {
                    System.out.println("Interrupted unexpectedly!");
                    throw new RuntimeException(e);
                }
            }
        }
        return focusGained;
    }
}
