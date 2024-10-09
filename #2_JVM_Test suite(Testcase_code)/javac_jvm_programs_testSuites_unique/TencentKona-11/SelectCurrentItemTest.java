

















import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;












public class SelectCurrentItemTest extends Applet implements ItemListener,
 WindowListener, Runnable
{
    
    Frame frame;
    Choice theChoice;
    Robot robot;

    Object lock = new Object();
    boolean passed = false;

    public void init()
    {
        
        
        

        this.setLayout (new BorderLayout ());

        frame = new Frame("SelectCurrentItemTest");
        theChoice = new Choice();
        for (int i = 0; i < 10; i++) {
            theChoice.add(new String("Choice Item " + i));
        }
        theChoice.addItemListener(this);
        frame.add(theChoice);
        frame.addWindowListener(this);

        try {
            robot = new Robot();
            robot.setAutoDelay(500);
        }
        catch (AWTException e) {
            throw new RuntimeException("Unable to create Robot.  Test fails.");
        }

    }

    public void start ()
    {
        
        setSize (200,200);
        setVisible(true);
        validate();

        
        
        
        

        frame.setLocation(1,20);
        robot.mouseMove(10, 30);
        frame.pack();
        frame.setVisible(true);
        synchronized(lock) {
        try {
        lock.wait(120000);
        }
        catch(InterruptedException e) {}
        }
        robot.waitForIdle();
        if (!passed) {
            throw new RuntimeException("TEST FAILED!");
        }

        


    }

    public void run() {
        try {Thread.sleep(1000);} catch (InterruptedException e){}
        
        Point loc = theChoice.getLocationOnScreen();
        
        Dimension size = theChoice.getSize();
        robot.mouseMove(loc.x + size.width - 10, loc.y + size.height / 2);

        robot.setAutoDelay(250);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);

        robot.setAutoDelay(1000);
        robot.mouseMove(loc.x + size.width / 2, loc.y + size.height + size.height / 2);
        robot.setAutoDelay(250);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
        synchronized(lock) {
            lock.notify();
        }
    }

    public void itemStateChanged(ItemEvent e) {
        System.out.println("ItemEvent received.  Test passes");
        passed = true;
    }

    public void windowOpened(WindowEvent e) {
        System.out.println("windowActivated()");
        Thread testThread = new Thread(this);
        testThread.start();
    }
    public void windowActivated(WindowEvent e) {
    }
    public void windowDeactivated(WindowEvent e) {}
    public void windowClosed(WindowEvent e) {}
    public void windowClosing(WindowEvent e) {}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}

}
