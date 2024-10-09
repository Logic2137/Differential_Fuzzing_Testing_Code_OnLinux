



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CompEventOnHiddenComponent
{
    transient static boolean moved = false;
    transient static boolean resized = false;

    transient static boolean ancestor_moved = false;
    transient static boolean ancestor_resized = false;
    static String passed = "";

    private static void init()
    {
        Robot robot;
        try {
            robot = new Robot();
        }catch(Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unexpected failure");
        }

        EventQueue.invokeLater(new Runnable(){
                public void run(){
                    JFrame f = new JFrame("JFrame");
                    JButton b = new JButton("JButton");
                    f.add(b);
                    new JOptionPane().
                        createInternalFrame(b, "Test").
                        addComponentListener(new ComponentAdapter() {
                                public void componentMoved(ComponentEvent e) {
                                    moved = true;
                                    System.out.println(e);
                                }
                                public void componentResized(ComponentEvent e) {
                                    resized = true;
                                    System.out.println(e);
                                }
                            });
                }
            });

        robot.waitForIdle();

        if (moved || resized){
            passed = "Hidden component got COMPONENT_MOVED or COMPONENT_RESIZED event";
        } else {
            System.out.println("Stage 1 passed.");
        }

        EventQueue.invokeLater(new Runnable() {
                public void run() {
                    JFrame parentWindow = new JFrame("JFrame 1");
                    JButton component = new JButton("JButton 1");;
                    JButton smallButton = new JButton("Small Button");


                    smallButton.addHierarchyBoundsListener(new HierarchyBoundsAdapter() {
                            public void ancestorMoved(HierarchyEvent e) {
                                ancestor_moved = true;
                                System.out.println("SMALL COMPONENT >>>>>"+e);
                            }
                            public void ancestorResized(HierarchyEvent e) {
                                ancestor_resized = true;
                                System.out.println("SMALL COMPONENT >>>>>"+e);
                            }
                        });


                    parentWindow.add(component);
                    component.add(smallButton);

                    component.setSize(100, 100);
                    component.setLocation(100, 100);

                }
            });

        robot.waitForIdle();

        if (!ancestor_resized || !ancestor_moved){
            passed = "Hidden component didn't get ANCESTOR event";
        } else {
            System.out.println("Stage 2 passed.");
        }

        robot.waitForIdle();

        if (passed.equals("")){
            CompEventOnHiddenComponent.pass();
        } else {
            CompEventOnHiddenComponent.fail(passed);
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


