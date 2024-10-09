


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.lang.reflect.InvocationTargetException;

public class ConsumeForModalDialogTest extends Applet {
    Robot robot;
    JFrame frame = new JFrame("Test Frame");
    JDialog dialog = new JDialog((Window)null, "Test Dialog", Dialog.ModalityType.DOCUMENT_MODAL);
    JTextField text = new JTextField();
    static boolean passed = true;

    public static void main(String[] args) {
        ConsumeForModalDialogTest app = new ConsumeForModalDialogTest();
        app.init();
        app.start();
    }

    public void init() {
        try {
            robot = new Robot();
            robot.setAutoDelay(50);
        } catch (AWTException e) {
            throw new RuntimeException("Error: unable to create robot", e);
        }
        
        
        
        this.setLayout (new BorderLayout ());
        Sysout.createDialogWithInstructions(new String[]
            {"This is automatic test. Simply wait until it is done."
            });
    }

    public void start() {

        text.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    Sysout.println(e.toString());
                    passed = false;
                }
            });

        JMenuItem testItem = new JMenuItem();
        testItem.setMnemonic('s');
        testItem.setText("Test");

        testItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    dialog.setVisible(true);
            }
        });

        JMenu menu = new JMenu();
        menu.setMnemonic('f');
        menu.setText("File");
        menu.add(testItem);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(menu);

        dialog.setSize(100, 100);
        dialog.add(text);

        frame.setJMenuBar(menuBar);
        frame.setSize(100, 100);
        frame.setVisible(true);

        robot.waitForIdle();

        if (!frame.isFocusOwner()) {
            Point loc = frame.getLocationOnScreen();
            Dimension size = frame.getSize();
            robot.mouseMove(loc.x + size.width/2, loc.y + size.height/2);
            robot.delay(10);
            robot.mousePress(MouseEvent.BUTTON1_MASK);
            robot.delay(10);
            robot.mouseRelease(MouseEvent.BUTTON1_MASK);

            robot.waitForIdle();

            int iter = 10;
            while (!frame.isFocusOwner() && iter-- > 0) {
                robot.delay(200);
            }
            if (iter <= 0) {
                Sysout.println("Test: the frame couldn't be focused!");
                return;
            }
        }

        robot.keyPress(KeyEvent.VK_ALT);
        robot.keyPress(KeyEvent.VK_F);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_F);
        robot.keyRelease(KeyEvent.VK_ALT);

        robot.waitForIdle();

        robot.keyPress(KeyEvent.VK_S);
        robot.delay(10);
        robot.keyRelease(KeyEvent.VK_S);

        robot.delay(1000);

        if (passed) {
            Sysout.println("Test passed.");
        } else {
            throw new RuntimeException("Test failed! Enexpected KeyTyped came into the JTextField.");
        }
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
