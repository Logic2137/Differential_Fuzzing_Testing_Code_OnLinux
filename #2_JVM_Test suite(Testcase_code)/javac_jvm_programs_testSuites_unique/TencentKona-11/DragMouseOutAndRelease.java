


import java.awt.*;
import java.awt.event.*;

public class DragMouseOutAndRelease
{
    static Frame frame = new Frame("Test Frame");
    static Choice choice1 = new Choice();
    static Robot robot;
    static Point pt;
    static volatile boolean mousePressed = false;
    static volatile boolean mouseReleased = false;

    private static void init()
    {
        frame.setLayout (new FlowLayout ());
        for (int i = 1; i<10;i++){
            choice1.add("item "+i);
        }
        frame.add(choice1);

        choice1.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    mousePressed = true;
                    System.out.println(me);
                }
                public void mouseReleased(MouseEvent me) {
                    mouseReleased = true;
                    System.out.println(me);
                }
            });

        frame.pack();
        frame.setVisible(true);
        frame.validate();

        try {
            robot = new Robot();
            robot.setAutoDelay(50);
            robot.waitForIdle();
            testMouseDrag();
        } catch (Throwable e) {
            new RuntimeException("Test failed. Exception thrown: "+e);
        }
        DragMouseOutAndRelease.pass();
    }

    public static void testMouseDrag(){
        mousePressed = false;
        mouseReleased = false;

        pt = choice1.getLocationOnScreen();
        robot.mouseMove(pt.x + choice1.getWidth()/2, pt.y + choice1.getHeight()/2);
        robot.waitForIdle();
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();


        
        robot.mouseMove(pt.x + choice1.getWidth()/2, pt.y - choice1.getHeight());
        robot.waitForIdle();
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();

        if (!mousePressed || !mouseReleased)
        {
            System.out.println("ERROR: "+ mousePressed+","+mouseReleased);
            
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            robot.waitForIdle();
            DragMouseOutAndRelease.fail("Test failed. Choice should generate PRESSED, RELEASED events outside if pressed on Choice ");
        } else{
            
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
            robot.waitForIdle();
            System.out.println("Choice did generated PRESSED and RELEASED after Drag outside the Choice ");
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
