



import java.applet.*;

import java.awt.*;
import java.awt.event.*;

public class HandleWindowDestroyTest extends Applet
{
    private volatile boolean handleEventCalled;

    public void start ()
    {
        setSize (200,200);
        setVisible(true);
        validate();

        Robot robot;
        try {
            robot = new Robot();
        }catch(Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unexpected failure");
        }

        Frame f = new Frame("Frame")
        {
            public boolean handleEvent(Event e)
            {
                if (e.id == Event.WINDOW_DESTROY)
                {
                    handleEventCalled = true;
                }
                return super.handleEvent(e);
            }
        };
        f.setBounds(100, 100, 100, 100);
        f.setVisible(true);
        robot.waitForIdle();

        handleEventCalled = false;
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new WindowEvent(f, Event.WINDOW_DESTROY));
        robot.waitForIdle();

        if (!handleEventCalled)
        {
            throw new RuntimeException("Test FAILED: handleEvent() is not called");
        }

        f.addWindowListener(new WindowAdapter()
        {
        });

        handleEventCalled = false;
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(new WindowEvent(f, Event.WINDOW_DESTROY));
        robot.waitForIdle();

        if (handleEventCalled)
        {
            throw new RuntimeException("Test FAILED: handleEvent() is called event with a listener added");
        }
    }
}
