



import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class FileDialogOpenDirTest extends Applet {

    public static void main(String[] args) {
        Applet a = new FileDialogOpenDirTest();
        a.init();
        a.start();
    }

    public void init()
    {
        System.setProperty("sun.awt.disableGtkFileDialogs","true");
        
        
        
        this.setLayout (new BorderLayout ());

        String curdir = System.getProperty("user.dir");

        String[] instructions1 =
        {
            "After test started you will see 'Test Frame' with a button inside.",
            "Click the button to open FileDialog.",
            "Verify that the directory opened is current directory, that is:",
            curdir,
            "If so press PASSED, otherwise FAILED."
        };

        String[] instructions2 =
        {
            "The test is not applicable for current platform. Press PASSED."
        };

        Sysout.createDialogWithInstructions(Toolkit.getDefaultToolkit().getClass().getName().
                                            equals("sun.awt.X11.XToolkit") ?
                                            instructions1 : instructions2);
    }

    public void start() {
        Frame frame = new Frame("Test Frame");
        Button open = new Button("Open File Dialog");

        open.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    new FileDialog(new Frame()).show();
                }
            });

        frame.setLayout(new FlowLayout());
        frame.add(open);

        int x = 0;
        int y = 0;
        Component dlg = null;

        if ((dlg = Sysout.getDialog()) != null) {
            x = dlg.getBounds().x + dlg.getBounds().width;
            y = dlg.getBounds().y;
        }
        frame.setBounds(x, y, 150, 70);
        frame.setVisible(true);
    }
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
        dialog.displayMessage( messageIn );
    }

    public static Component getDialog() {
        return dialog;
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
