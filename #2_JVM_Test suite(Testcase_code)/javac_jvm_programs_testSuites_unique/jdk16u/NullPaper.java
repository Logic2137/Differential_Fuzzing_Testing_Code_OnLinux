








import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.print.*;




public class NullPaper {

   private static void init()
    {
    boolean settingNullWorked = false;

    try {
        
        new PageFormat().setPaper(null);
        settingNullWorked = true;

    
    } catch (NullPointerException e) {
        pass();

    
    } catch (Exception e) {
        fail("Instead of the expected NullPointerException, '" + e + "' was thrown.");
    }

    if (settingNullWorked) {
        fail("The expected NullPointerException was not thrown");
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
