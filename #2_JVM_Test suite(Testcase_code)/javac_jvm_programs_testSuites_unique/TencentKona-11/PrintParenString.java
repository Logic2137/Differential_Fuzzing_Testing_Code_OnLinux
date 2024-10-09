




import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.text.*;

public class PrintParenString extends Frame implements ActionListener {

 private TextCanvas c;

 public static void main(String args[]) {

  String[] instructions =
        {
         "You must have a printer available to perform this test",
         "This test should print a page which contains the same",
         "text message as in the test window on the screen",
         "You should also monitor the command line to see if any exceptions",
         "were thrown",
         "If an exception is thrown, or the page doesn't print properly",
         "then the test fails",
       };
      Sysout.createDialog( );
      Sysout.printInstructions( instructions );

    PrintParenString f = new PrintParenString();
    f.show();
 }

 public PrintParenString() {
    super("JDK 1.2 drawString Printing");

    c = new TextCanvas();
    add("Center", c);

    Button printButton = new Button("Print");
    printButton.addActionListener(this);
    add("South", printButton);

    addWindowListener(new WindowAdapter() {
       public void windowClosing(WindowEvent e) {
             System.exit(0);
            }
    });

    pack();
 }

 public void actionPerformed(ActionEvent e) {

   PrinterJob pj = PrinterJob.getPrinterJob();

   if (pj != null && pj.printDialog()) {

       pj.setPrintable(c);
       try {
            pj.print();
      } catch (PrinterException pe) {
      } finally {
         System.err.println("PRINT RETURNED");
      }
   }
 }

 class TextCanvas extends Panel implements Printable {

    String nullStr = null;
    String emptyStr = new String();
    AttributedString nullAttStr = null;
    AttributedString emptyAttStr = new AttributedString(emptyStr);
    AttributedCharacterIterator nullIterator = null;
    AttributedCharacterIterator emptyIterator = emptyAttStr.getIterator();

    public int print(Graphics g, PageFormat pgFmt, int pgIndex) {

      if (pgIndex > 0)
         return Printable.NO_SUCH_PAGE;

      Graphics2D g2d = (Graphics2D)g;
      g2d.translate(pgFmt.getImageableX(), pgFmt.getImageableY());

      paint(g);

      return Printable.PAGE_EXISTS;
    }

    public void paint(Graphics g1) {
        Graphics2D g = (Graphics2D)g1;

          String str = "String containing unclosed parenthesis (.";
          g.drawString(str, 20, 40);

    }

     public Dimension getPreferredSize() {
        return new Dimension(450, 250);
    }
 }

}

class Sysout
 {
   private static TestDialog dialog;

   public static void createDialogWithInstructions( String[] instructions )
    {
      dialog = new TestDialog( new Frame(), "Instructions" );
      dialog.printInstructions( instructions );
      dialog.show();
      println( "Any messages for the tester will display here." );
    }

   public static void createDialog( )
    {
      dialog = new TestDialog( new Frame(), "Instructions" );
      String[] defInstr = { "Instructions will appear here. ", "" } ;
      dialog.printInstructions( defInstr );
      dialog.show();
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


class TestDialog extends Dialog {

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
    }

 }
