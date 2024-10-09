





import java.awt.*;
import java.awt.event.*;


public class setComponentZOrder
{

    


    private static void init()
    {
        try {
            Container c = new Container();
            Button b = new Button("b");
            c.setComponentZOrder(b, 0);
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
            fail("The setComponentZOrder method used to insert a component caused the mixing code to throw the exception: " + e);
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
