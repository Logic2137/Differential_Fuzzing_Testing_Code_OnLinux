





import java.applet.Applet;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;

public class PrinterDialogsModalityTest extends Applet
{
    

    public void init()
    {
        
        
        
        this.setLayout (new BorderLayout ());

        String[] instructions =
        {
            "This is a Windows only test, for other platforms consider it passed",
            "After test start you will see frame titled \"test Frame\"",
            "with two buttons - \"Page Dialog\" and \"Print Dialog\"",
            "1. make the frame active by clicking on title",
            "2. press \"Page Dialog\" button, page dailog should popup",
            "3. make sure page dialog is modal (if not test is failed)",
            "4. close the dialog (either cancel it or press ok)",
            "5. make sure the frame is still active (if not test is failed)",
            "6. press \"Print Dialog\" button, print dialog should popup",
            "7. repeat steps 3.-5.",
            "",
            "If you are able to execute all steps successfully then test is passed, else failed."
        };
        Sysout.createDialogWithInstructions( instructions );

    }

    public void start ()
    {
        
        setSize (200,200);
        setVisible(true);
        validate();

        Button page = new Button("Page Dialog");
        page.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    PrinterJob prnJob = PrinterJob.getPrinterJob();
                    prnJob.pageDialog(new PageFormat());
                }
            });
        Button print = new Button("Print Dialog");
        print.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    PrinterJob prnJob = PrinterJob.getPrinterJob();
                    prnJob.printDialog();
                }
            });
        Frame frame = new Frame("Test Frame");
        frame.setLayout(new FlowLayout());
        frame.add(page);
        frame.add(print);
        frame.setLocation(200, 200);
        frame.pack();
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
