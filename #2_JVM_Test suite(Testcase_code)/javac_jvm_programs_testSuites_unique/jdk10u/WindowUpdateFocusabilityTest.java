



import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.lang.reflect.*;

public class WindowUpdateFocusabilityTest extends Applet {
    Robot robot;
    boolean focusGained = false;
    final Object monitor = new Object();
    FocusListener listener = new FocusAdapter () {
            public void focusGained(FocusEvent e) {
                Sysout.println(e.toString());
                synchronized (monitor) {
                    focusGained = true;
                    monitor.notifyAll();
                }
            }
        };

    public static void main(String[] args) {
        WindowUpdateFocusabilityTest app = new WindowUpdateFocusabilityTest();
        app.init();
        app.start();
    }

    public void init() {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            throw new RuntimeException("Error: couldn't create robot");
        }
        
        
        
        this.setLayout (new BorderLayout ());
        Sysout.createDialogWithInstructions(new String[]
            {"This is an automatic test. Simply wait until it's done."});
    }

    public void start() {
        if ("sun.awt.motif.MToolkit".equals(Toolkit.getDefaultToolkit().getClass().getName())) {
            Sysout.println("No testing on Motif.");
            return;
        }

        test(new Frame("Frame owner"));
        Frame dialog_owner = new Frame("dialog's owner");
        test(new Dialog(dialog_owner));
        test(new Dialog(dialog_owner, Dialog.ModalityType.DOCUMENT_MODAL));
        test(new Dialog(dialog_owner, Dialog.ModalityType.APPLICATION_MODAL));
        test(new Dialog(dialog_owner, Dialog.ModalityType.TOOLKIT_MODAL));
        test(new Dialog((Window) null, Dialog.ModalityType.MODELESS));
        test(new Dialog((Window) null, Dialog.ModalityType.DOCUMENT_MODAL));
        test(new Dialog((Window) null, Dialog.ModalityType.APPLICATION_MODAL));
        test(new Dialog((Window) null, Dialog.ModalityType.TOOLKIT_MODAL));
        dialog_owner.dispose();
    }

    private void test(final Window owner)
    {
        Window window0 = new Window(owner); 
        Window window1 = new Window(window0);
        Window window2 = new Window(window1);
        Button button1 = new Button("button1");
        Button button2 = new Button("button2");
        button1.addFocusListener(listener);
        button2.addFocusListener(listener);

        owner.setBounds(800, 0, 100, 100);
        window1.setBounds(800, 300, 100, 100);
        window2.setBounds(800, 150, 100, 100);

        window1.add(button1);
        window2.add(button2);

        window2.setVisible(true);
        window1.setVisible(true);
        EventQueue.invokeLater(new Runnable() {
                public void run() {
                    owner.setVisible(true);
                }
            });

        try {
            EventQueue.invokeAndWait(new Runnable() {
                    public void run() {
                        
                    }
                });
        } catch (InterruptedException ie) {
            throw new RuntimeException(ie);
        } catch (InvocationTargetException ite) {
            throw new RuntimeException(ite);
        }

        robot.delay(1000);

        clickOn(button1);

        if (!isFocusGained()) {
            throw new RuntimeException("Test failed: window1 is not focusable!");
        }

        focusGained = false;
        clickOn(button2);

        if (!isFocusGained()) {
            throw new RuntimeException("Test failed: window2 is not focusable!");
        }

        Sysout.println("Test for " + owner.getName() + " passed.");
        owner.dispose();
    }

    void clickOn(Component c) {
        Point p = c.getLocationOnScreen();
        Dimension d = c.getSize();

        Sysout.println("Clicking " + c);

        robot.mouseMove(p.x + (int)(d.getWidth()/2), p.y + (int)(d.getHeight()/2));

        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        waitForIdle();
    }

    void waitForIdle() {
        try {
            robot.waitForIdle();
            robot.delay(50);
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
    }

    boolean isFocusGained() {
        synchronized (monitor) {
            if (!focusGained) {
                try {
                    monitor.wait(3000);
                } catch (InterruptedException e) {
                    Sysout.println("Interrupted unexpectedly!");
                    throw new RuntimeException(e);
                }
            }
        }
        return focusGained;
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
