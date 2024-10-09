



import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.*;

public class ModalExcludedWindowClickTest extends Applet {
    Robot robot;
    Frame frame = new Frame("Frame");
    Window w = new Window(frame);
    Dialog d = new Dialog ((Dialog)null, "NullParentDialog", true);
    Button button = new Button("Button");
    boolean actionPerformed = false;

    public static void main (String args[]) {
        ModalExcludedWindowClickTest app = new ModalExcludedWindowClickTest();
        app.init();
        app.start();
    }

    public void init() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Error: unable to create robot", e);
        }
        
        
        
        this.setLayout (new BorderLayout ());
        Sysout.createDialogWithInstructions(new String[]
            {"This is an AUTOMATIC test", "simply wait until it is done"});
    }

    public void start() {

        if ("sun.awt.motif.MToolkit".equals(Toolkit.getDefaultToolkit().getClass().getName())) {
            Sysout.println("No testing on MToolkit.");
            return;
        }

        button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    actionPerformed = true;
                    Sysout.println(e.paramString());
                }
            });

        EventQueue.invokeLater(new Runnable() {
                public void run() {
                    frame.setSize(200, 200);
                    frame.setVisible(true);

                    w.setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
                    w.add(button);
                    w.setSize(200, 200);
                    w.setLocation(230, 230);
                    w.setVisible(true);

                    d.setSize(200, 200);
                    d.setLocation(0, 230);
                    d.setVisible(true);

                }
            });

        waitTillShown(d);

        test();
    }

    void test() {
        clickOn(button);
        waitForIdle();
        if (!actionPerformed) {
            throw new RuntimeException("Test failed!");
        }
        Sysout.println("Test passed.");
    }

    void clickOn(Component c) {
        Point p = c.getLocationOnScreen();
        Dimension d = c.getSize();

        Sysout.println("Clicking " + c);

        if (c instanceof Frame) {
            robot.mouseMove(p.x + (int)(d.getWidth()/2), p.y + ((Frame)c).getInsets().top/2);
        } else {
            robot.mouseMove(p.x + (int)(d.getWidth()/2), p.y + (int)(d.getHeight()/2));
        }
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        waitForIdle();
    }
    void waitTillShown(Component c) {
        while (true) {
            try {
                Thread.sleep(100);
                c.getLocationOnScreen();
                break;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (IllegalComponentStateException e) {}
        }
    }
    void waitForIdle() {
        try {
            robot.waitForIdle();
            EventQueue.invokeAndWait( new Runnable() {
                    public void run() {} 
                });
        } catch(InterruptedException ie) {
            Sysout.println("waitForIdle, non-fatal exception caught:");
            ie.printStackTrace();
        } catch(InvocationTargetException ite) {
            Sysout.println("waitForIdle, non-fatal exception caught:");
            ite.printStackTrace();
        }

        
        robot.delay(200);
    }
}





class Sysout
{
    static TestDialog dialog;

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

    
    public void displayMessage( String messageIn )
    {
        messageText.append( messageIn + "\n" );
        System.out.println(messageIn);
    }

}
