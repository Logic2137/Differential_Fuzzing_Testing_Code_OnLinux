



















import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import java.io.*;













public class NonEDT_GUI_Deadlock extends Applet
{
    
    boolean bOK = false;
    Thread badThread = null;

    public void init()
    {
        
        
        


        String[] instructions =
        {
            "This is an AUTOMATIC test",
            "simply wait until it is done"
        };
        Sysout.createDialog( );
        Sysout.printInstructions( instructions );

    }

    public void start ()
    {
        

        setSize (200,300);
        setVisible(true);
        validate();

        final Frame theFrame = new Frame("Window test");
        theFrame.setSize(240, 200);

        Thread thKiller = new Thread() {
           public void run() {
              try {
                 Thread.sleep( 9000 );
              }catch( Exception ex ) {
              }
              if( !bOK ) {
                 
                 
                 Runtime.getRuntime().halt(0);
              }else{
                 
              }
           }
        };
        thKiller.setName("Killer thread");
        thKiller.start();
        Window w = new TestWindow(theFrame);
        theFrame.toBack();
        theFrame.setVisible(true);

        theFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
        EventQueue.invokeLater(new Runnable() {
           public void run() {
               bOK = true;
           }
        });



    }
    class TestWindow extends Window implements Runnable {

        TestWindow(Frame f) {
            super(f);

            
            setLocation(0, 75);

            show();
            toFront();

            badThread = new Thread(this);
            badThread.setName("Bad Thread");
            badThread.start();

        }

        public void paint(Graphics g) {
            g.drawString("Deadlock or no deadlock?",20,80);
        }

        public void run() {

            long ts = System.currentTimeMillis();

            while (true) {
                if ((System.currentTimeMillis()-ts)>3000) {
                    this.setVisible( false );
                    dispose();
                    break;
                }

                toFront();
                try {
                    Thread.sleep(80);
                } catch (Exception e) {
                }
            }
        }
    }



    public static void main(String args[]) {
       NonEDT_GUI_Deadlock imt = new NonEDT_GUI_Deadlock();
       imt.init();
       imt.start();
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
