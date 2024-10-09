



import java.applet.Applet;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.*;
import java.io.IOException;

public class ManualHTMLDataFlavorTest extends Applet {

    class DropPane extends Panel implements DropTargetListener {

        DropPane() {
            requestFocus();
            setBackground(Color.red);
            setDropTarget(new DropTarget(this, DnDConstants.ACTION_COPY, this));
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200,200);
        }

        @Override
        public void dragEnter(DropTargetDragEvent dtde) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        }

        @Override
        public void dragOver(DropTargetDragEvent dtde) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        }

        @Override
        public void dropActionChanged(DropTargetDragEvent dtde) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
        }

        @Override
        public void dragExit(DropTargetEvent dte) {}

        @Override
        public void drop(DropTargetDropEvent dtde) {
            if (!dtde.isDataFlavorSupported(DataFlavor.allHtmlFlavor)) {
                Sysout.println("DataFlavor.allHtmlFlavor is not present in the system clipboard");
                dtde.rejectDrop();
                return;
            } else if (!dtde.isDataFlavorSupported(DataFlavor.fragmentHtmlFlavor)) {
                Sysout.println("DataFlavor.fragmentHtmlFlavor is not present in the system clipboard");
                dtde.rejectDrop();
                return;
            } else if (!dtde.isDataFlavorSupported(DataFlavor.selectionHtmlFlavor)) {
                Sysout.println("DataFlavor.selectionHtmlFlavor is not present in the system clipboard");
                dtde.rejectDrop();
                return;
            }

            dtde.acceptDrop(DnDConstants.ACTION_COPY);

            Transferable t = dtde.getTransferable();
            try {
                Sysout.println("ALL:");
                Sysout.println(t.getTransferData(DataFlavor.allHtmlFlavor).toString());
                Sysout.println("FRAGMENT:");
                Sysout.println(t.getTransferData(DataFlavor.fragmentHtmlFlavor).toString());
                Sysout.println("SELECTION:");
                Sysout.println(t.getTransferData(DataFlavor.selectionHtmlFlavor).toString());
            } catch (UnsupportedFlavorException | IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void init() {

        String[] instructions =
            {
                "1) The test contains a drop-aware panel with a red background",
                "2) Open some page in a browser, select some text",
                "   Drag and drop it on the red panel",
                "   IMPORTANT NOTE: the page should be stored locally.",
                "   otherwise for instance iexplore can prohibit drag and drop from",
                "   the browser to other applications because of",
                "   the protected mode restrictions.",
                "   On Mac OS X do NOT use Safari, it does not provide the needed DataFlavor",
                "3) Check the data in the output area of this dialog",
                "5) The output should not contain information that any of",
                "   flavors is not present in the system clipboard",
                "6) The output should contain data in three different formats",
                "   provided by the system clipboard",
                "    - Data after the \"ALL:\" marker should include the data",
                "      from the the \"SELECTION:\" marker",
                "    - Data after the \"FRAGMENT\" marker should include the data",
                "      from the \"SELECTION:\" marker and may be some closing",
                "      tags could be added to the mark-up",
                "    - Data after the \"SELECTION:\" marker should correspond",
                "      to the data selected in the browser",
                "7) If the above requirements are met, the test is passed"
            };

        add(new DropPane());
        Sysout.createDialogWithInstructions( instructions );

        new ManualHTMLDataFlavorTest();
    }

    public void start ()
    {
        setSize (200,200);
        setVisible(true);
        validate();

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
