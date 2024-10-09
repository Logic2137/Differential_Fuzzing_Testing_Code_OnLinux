






import java.awt.*;
import java.awt.event.*;


public class RealSyncOnEDT
{
    static Exception exceptionCaught = null;

    private static void init()
    {
        
        try {
            EventQueue.invokeAndWait(new Runnable() {
                public void run() {
                    Frame frame = new Frame("Test frame");

                    frame.setSize(100, 100);
                    frame.setVisible(true);

                    try {
                        ((sun.awt.SunToolkit)java.awt.Toolkit.getDefaultToolkit()).realSync();
                    } catch (Exception e) {
                        exceptionCaught = e;
                    }
                }
            });
        } catch (InterruptedException e) {
            e.printStackTrace();
            fail("Unexpected exception caught: " + e);
            return;
        } catch (java.lang.reflect.InvocationTargetException e) {
            e.printStackTrace();
            fail("Unexpected exception caught: " + e);
            return;
        }

        
        if (exceptionCaught == null) {
            fail("No exception was thrown by the realSync() method.");
            return;
        } else if (!exceptionCaught.getClass().getName().equals("sun.awt.SunToolkit$IllegalThreadException")) {
            exceptionCaught.printStackTrace();
            fail("Unexpected exception caught while invoking the realSync(): " + exceptionCaught);
            return;
        }
        pass();

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
        System.out.println( "The test passed." );
        System.out.println( "The test is over, hit  Ctl-C to stop Java VM" );
        
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
        System.out.println( "The test failed: " + whyFailed );
        System.out.println( "The test is over, hit  Ctl-C to stop Java VM" );
        
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
