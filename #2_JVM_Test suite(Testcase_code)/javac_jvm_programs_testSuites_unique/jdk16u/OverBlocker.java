





import java.awt.*;
import java.awt.event.*;

public class OverBlocker
{

    private static void init()
    {
        

        String[] instructions =
        {
            " the test will be run 4 times, to start next test just close all ",
            " windows of previous; the instructions are the same for all tests: ",
            " 1) there is a frame with 'show modal' button, ",
            " 2) press the button to show a dialog, ",
            " 3) activate any non-Java application, move the app over the dialog, ",
            " 4) click on the frame by mouse, ",
            " 5) make sure that the dialog comes up from the application and ",
            "    now the dialog overlaps the app as well as the frame, ",
            "    if it's true, then the test passed, otherwise, it failed. ",
            " Press 'pass' button only after all of the 4 tests are completed, ",
            " the number of the currently executed test is displayed on the ",
            " output window. "
        };
        Sysout.createDialog( );
        Sysout.printInstructions( instructions );

        test(false, true);
        test(true, true);
        test(true, false);
        test(false, false);

    }

    private static final Object obj = new Object();
    private static int counter = 0;

    
    private static void test(final boolean ownerless, final boolean usual) {

        Sysout.print(" * test #" + (++counter) + " is running ... ");

        final Frame frame = new Frame();
        Button button = new Button("show modal");
        button.addActionListener(new ActionListener() {
              public void actionPerformed(ActionEvent ae) {
                    Dialog dialog = null;
                    Frame parent = ownerless ? null : frame;
                    if (usual) {
                        dialog = new Dialog(parent, "Sample", true);
                    } else {
                        dialog = new FileDialog(parent, "Sample", FileDialog.LOAD);
                    }
                    dialog.addWindowListener(new WindowAdapter(){
                            public void windowClosing(WindowEvent e){
                                e.getWindow().dispose();
                            }
                       });
                    dialog.setBounds(200, 200, 200, 200);
                    dialog.setVisible(true);
                }
            });
        frame.add(button);
        frame.setBounds(400, 400, 200, 200);
        frame.addWindowListener(new WindowAdapter(){
              public void windowClosing(WindowEvent e){
                    e.getWindow().dispose();
                    synchronized(obj) {
                        obj.notify();
                    }
                }
             });
        frame.setVisible(true);

        synchronized(obj) {
            try{
                obj.wait();
            } catch(Exception e) {
                throw new RuntimeException(e);
            }
        }

        Sysout.println(" completed. ");

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
        Sysout.println( "The test passed." );
        Sysout.println( "The test is over, hit  Ctl-C to stop Java VM" );
        
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
        Sysout.println( "The test failed: " + whyFailed );
        Sysout.println( "The test is over, hit  Ctl-C to stop Java VM" );
        
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




















class Sysout
{
    private static TestDialog dialog;

    public static void createDialogWithInstructions( String[] instructions )
    {
        dialog = new TestDialog( new Frame(), "Instructions" );
        dialog.printInstructions( instructions );
        dialog.setVisible(true);
        println( "Any messages for the tester will display here." );
    }

    public static void createDialog( )
    {
        dialog = new TestDialog( new Frame(), "Instructions" );
        String[] defInstr = { "Instructions will appear here. ", "" } ;
        dialog.printInstructions( defInstr );
        dialog.setVisible(true);
        println( "Any messages for the tester will display here." );
    }


    public static void printInstructions( String[] instructions )
    {
        dialog.printInstructions( instructions );
    }


    public static void println( String messageIn )
    {
        dialog.displayMessage( messageIn, true );
    }

    public static void print( String messageIn )
    {
        dialog.displayMessage( messageIn, false );
    }

}


class TestDialog extends Dialog implements ActionListener
{

    TextArea instructionsText;
    TextArea messageText;
    int maxStringLength = 80;
    Panel  buttonP = new Panel();
    Button passB = new Button( "pass" );
    Button failB = new Button( "fail" );

    
    public TestDialog( Frame frame, String name )
    {
        super( frame, name );
        int scrollBoth = TextArea.SCROLLBARS_BOTH;
        instructionsText = new TextArea( "", 15, maxStringLength, scrollBoth );
        add( "North", instructionsText );

        messageText = new TextArea( "", 5, maxStringLength, scrollBoth );
        add("Center", messageText);

        passB = new Button( "pass" );
        passB.setActionCommand( "pass" );
        passB.addActionListener( this );
        buttonP.add( "East", passB );

        failB = new Button( "fail" );
        failB.setActionCommand( "fail" );
        failB.addActionListener( this );
        buttonP.add( "West", failB );

        add( "South", buttonP );
        pack();

        setVisible(true);
    }

    
    public void printInstructions( String[] instructions )
    {
        
        instructionsText.setText( "" );

        

        String printStr, remainingStr;
        for( int i=0; i < instructions.length; i++ )
        {
            
            remainingStr = instructions[ i ];
            while( remainingStr.length() > 0 )
            {
                
                if( remainingStr.length() >= maxStringLength )
                {
                    
                    int posOfSpace = remainingStr.
                        lastIndexOf( ' ', maxStringLength - 1 );

                    if( posOfSpace <= 0 ) posOfSpace = maxStringLength - 1;

                    printStr = remainingStr.substring( 0, posOfSpace + 1 );
                    remainingStr = remainingStr.substring( posOfSpace + 1 );
                }
                
                else
                {
                    printStr = remainingStr;
                    remainingStr = "";
                }

                instructionsText.append( printStr + "\n" );

            }

        }

    }

    
    public void displayMessage( String messageIn, boolean nextLine )
    {
        messageText.append( messageIn + (nextLine? "\n" : "") );
        System.out.println(messageIn);
    }

    
    
    
    public void actionPerformed( ActionEvent e )
    {
        if( e.getActionCommand() == "pass" )
        {
            OverBlocker.pass();
        }
        else
        {
            OverBlocker.fail();
        }
    }

}
