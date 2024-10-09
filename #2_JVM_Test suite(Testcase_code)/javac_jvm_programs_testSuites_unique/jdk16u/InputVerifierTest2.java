





import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
import java.awt.TextArea;

import java.awt.event.InputEvent;

import javax.swing.InputVerifier;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JWindow;

public class InputVerifierTest2
{

    private static void init()
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JTextField tf = new JTextField(10);
        frame.getContentPane().add(tf);

        final JWindow w = new JWindow(frame);
        JButton btn1 = new JButton("window");
        btn1.setName("bnt1");
        w.getContentPane().add(btn1);
        w.pack();
        w.setVisible(true);

        frame.setSize(200, 200);
        frame.setVisible(true);


        Robot r = null;
        try {
            r = new Robot();
        } catch (AWTException e) {
            InputVerifierTest2.fail(e);
        }


        try {
            r.waitForIdle();
            mouseClickOnComp(r, tf);
            r.waitForIdle();

            if (!tf.isFocusOwner()) {
                throw new RuntimeException("t1 is not a focus owner");
            }

            tf.setInputVerifier(new InputVerifier() {
                    public boolean verify(JComponent input) {
                        System.err.println("verify on " + input);
                        throw new RuntimeException("InputVerifier should not be called");
                    }
                });
            btn1.requestFocus();
        } catch (Exception e) {
            InputVerifierTest2.fail(e);
        }

        InputVerifierTest2.pass();

    }


    static void mouseClickOnComp(Robot r, Component comp) {
        Point loc = comp.getLocationOnScreen();
        loc.x += comp.getWidth() / 2;
        loc.y += comp.getHeight() / 2;
        r.mouseMove(loc.x, loc.y);
        r.delay(10);
        r.mousePress(InputEvent.BUTTON1_MASK);
        r.delay(10);
        r.mouseRelease(InputEvent.BUTTON1_MASK);
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

    public static synchronized void fail( Exception whyFailed )
    {
        System.out.println( "The test failed: " + whyFailed );
        System.out.println( "The test is over, hit  Ctl-C to stop Java VM" );
        
        if ( mainThread == Thread.currentThread() )
        {
            
            throw new RuntimeException( whyFailed );
        }
        theTestPassed = false;
        testGeneratedInterrupt = true;
        failureMessage = whyFailed.toString();
        mainThread.interrupt();
    }

}




class TestPassedException extends RuntimeException
{
}
