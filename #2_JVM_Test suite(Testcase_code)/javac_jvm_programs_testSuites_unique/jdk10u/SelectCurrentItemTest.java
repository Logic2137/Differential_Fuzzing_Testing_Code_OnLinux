

















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

        String[] instructions =
        {
            "This is an AUTOMATIC test",
            "simply wait until it is done"
        };
        Sysout.createDialog( );
        Sysout.printInstructions( instructions );

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
        Sysout.println("ItemEvent received.  Test passes");
        passed = true;
    }

    public void windowOpened(WindowEvent e) {
        Sysout.println("windowActivated()");
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
        dialog.setLocation(0, 400);
        dialog.setVisible(true);
        println( "Any messages for the tester will display here." );
    }


    public static void printInstructions( String[] instructions )
    {
        dialog.printInstructions( instructions );
    }


    public static void println( String messageIn )
    {
        dialog.displayMessage( messageIn );
    }

}


class TestDialog extends Dialog
{

    TextArea instructionsText;
    TextArea messageText;
    int maxStringLength = 80;

    
    public TestDialog( Frame frame, String name )
    {
        super( frame, name );
        int scrollBoth = TextArea.SCROLLBARS_BOTH;
        instructionsText = new TextArea( "", 15, maxStringLength, scrollBoth );
        add( "North", instructionsText );

        messageText = new TextArea( "", 5, maxStringLength, scrollBoth );
        add("Center", messageText);

        pack();

        show();
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

    
    public void displayMessage( String messageIn )
    {
        messageText.append( messageIn + "\n" );
        System.out.println(messageIn);
    }

}
