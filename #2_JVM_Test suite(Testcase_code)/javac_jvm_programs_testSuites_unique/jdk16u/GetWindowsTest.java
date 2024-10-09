



import java.awt.*;
import java.awt.event.*;

import java.util.*;

public class GetWindowsTest
{
    private static Vector<Window> frames = new Vector<Window>();
    private static Vector<Window> windows = new Vector<Window>();
    private static Vector<Window> ownerless = new Vector<Window>();

    private static void init()
    {
        Frame f1 = new Frame("F1");
        f1.setBounds(100, 100, 100, 100);
        f1.setVisible(true);
        addToWindowsList(f1);

        Dialog d1 = new Dialog(f1, "D1", Dialog.ModalityType.MODELESS);
        d1.setBounds(120, 120, 100, 100);
        d1.setVisible(true);
        addToWindowsList(d1);

        Window w1 = new Window(d1);
        w1.setBounds(140, 140, 100, 100);
        w1.setVisible(true);
        addToWindowsList(w1);

        Frame f2 = new Frame("F2");
        f2.setBounds(300, 100, 100, 100);
        f2.setVisible(true);
        addToWindowsList(f2);

        Window w2 = new Window(f2);
        w2.setBounds(320, 120, 100, 100);
        w2.setVisible(true);
        addToWindowsList(w2);

        Dialog d2 = new Dialog(f2, "D2", Dialog.ModalityType.MODELESS);
        d2.setBounds(340, 140, 100, 100);
        d2.setVisible(true);
        addToWindowsList(d2);

        Dialog d3 = new Dialog((Frame)null, "D3", Dialog.ModalityType.MODELESS);
        d3.setBounds(500, 100, 100, 100);
        d3.setVisible(true);
        addToWindowsList(d3);

        Dialog d4 = new Dialog(d3, "D4", Dialog.ModalityType.MODELESS);
        d4.setBounds(520, 120, 100, 100);
        d4.setVisible(true);
        addToWindowsList(d4);

        Window w3 = new Window((Frame)null);
        w3.setBounds(700, 100, 100, 100);
        w3.setVisible(true);
        addToWindowsList(w3);

        Window w4 = new Window(w3);
        w4.setBounds(720, 120, 100, 100);
        w4.setVisible(true);
        addToWindowsList(w4);

        try {
            Robot robot = new Robot();
            robot.waitForIdle();
        }catch(Exception ex) {
            ex.printStackTrace();
            throw new Error("Unexpected failure");
        }

        Frame[] fl = Frame.getFrames();
        Vector<Window> framesToCheck = new Vector<Window>();
        for (Frame f : fl)
        {
            framesToCheck.add(f);
        }
        checkWindowsList(frames, framesToCheck, "Frame.getFrames()");

        Window[] wl = Window.getWindows();
        Vector<Window> windowsToCheck = new Vector<Window>();
        for (Window w : wl)
        {
            windowsToCheck.add(w);
        }
        checkWindowsList(windows, windowsToCheck, "Window.getWindows()");

        Window[] ol = Window.getOwnerlessWindows();
        Vector<Window> ownerlessToCheck = new Vector<Window>();
        for (Window o : ol)
        {
            ownerlessToCheck.add(o);
        }
        checkWindowsList(ownerless, ownerlessToCheck, "Window.getOwnerlessWindows()");

        GetWindowsTest.pass();
    }

    private static void addToWindowsList(Window w)
    {
        if (w instanceof Frame)
        {
            frames.add(w);
        }
        windows.add(w);
        if (w.getOwner() == null)
        {
            ownerless.add(w);
        }
    }

    private static void checkWindowsList(Vector<Window> wl1, Vector<Window> wl2, String methodName)
    {
        if ((wl1.size() != wl2.size()) ||
            !wl1.containsAll(wl2) ||
            !wl2.containsAll(wl1))
        {
            fail("Test FAILED: method " + methodName + " returns incorrect list of windows");
        }
    }



    private static boolean theTestPassed = false;
    private static boolean testGeneratedInterrupt = false;
    private static String failureMessage = "";

    private static Thread mainThread = null;

    private static int sleepTime = 300000;

    
    
    
    
    public static void main( String args[] ) throws InterruptedException
    {
        mainThread = Thread.currentThread();
        try
        {
            init();
        }
        catch( TestPassedException e )
        {
            
            
            return;
        }
        
        
        

        
        
        try
        {
            Thread.sleep( sleepTime );
            
            throw new RuntimeException( "Timed out after " + sleepTime/1000 + " seconds" );
        }
        catch (InterruptedException e)
        {
            
            
            if( ! testGeneratedInterrupt ) throw e;

            
            testGeneratedInterrupt = false;

            if ( theTestPassed == false )
            {
                throw new RuntimeException( failureMessage );
            }
        }
    }

    public static synchronized void setTimeoutTo( int seconds )
    {
        sleepTime = seconds * 1000;
    }

    public static synchronized void pass()
    {
        
        if ( mainThread == Thread.currentThread() )
        {
            
            
            
            theTestPassed = true;
            throw new TestPassedException();
        }
        theTestPassed = true;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
    }

    public static synchronized void fail()
    {
        
        fail( "it just plain failed! :-)" );
    }

    public static synchronized void fail( String whyFailed )
    {
        
        if ( mainThread == Thread.currentThread() )
        {
            
            throw new RuntimeException( whyFailed );
        }
        theTestPassed = false;
        testGeneratedInterrupt = true;
        failureMessage = whyFailed;
        mainThread.interrupt();
    }
}




class TestPassedException extends RuntimeException
{
}


