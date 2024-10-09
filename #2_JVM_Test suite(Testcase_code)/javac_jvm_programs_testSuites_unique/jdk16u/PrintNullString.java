




import java.awt.*;
import java.awt.event.*;
import java.awt.print.*;
import java.text.*;

public class PrintNullString extends Frame implements ActionListener {

 private TextCanvas c;

 public static void main(String args[]) {

  String[] instructions =
        {
         "You must have a printer available to perform this test",
         "This test should print a page which contains the same",
         "text messages as in the test window on the screen",
         "The messages should contain only 'OK' and 'expected' messages",
         "There should be no FAILURE messages.",
         "You should also monitor the command line to see if any exceptions",
         "were thrown",
         "If the page fails to print, but there were no exceptions",
         "then the problem is likely elsewhere (ie your printer)"
       };
      Sysout.createDialog( );
      Sysout.printInstructions( instructions );

    PrintNullString f = new PrintNullString();
    f.show();
 }

 public PrintNullString() {
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

        
        try {
             g.drawString(nullStr, 20, 40);
             g.drawString("FAILURE: No NPE for null String, int", 20, 40);
        } catch (NullPointerException e) {
          g.drawString("caught expected NPE for null String, int", 20, 40);
        }

        
             g.drawString(emptyStr, 20, 60);
             g.drawString("OK for empty String, int", 20, 60);
        


        
        try {
             g.drawString(nullStr, 20.0f, 80.0f);
             g.drawString("FAILURE: No NPE for null String, float", 20, 80);
        } catch (NullPointerException e) {
          g.drawString("caught expected NPE for null String, float", 20, 80);
        } 
        
             g.drawString(emptyStr, 20.0f, 100.0f);
             g.drawString("OK for empty String, float", 20.0f, 100.f);
        

        
        try {
             g.drawString(nullIterator, 20, 120);
             g.drawString("FAILURE: No NPE for null iterator, float", 20, 120);
        } catch (NullPointerException e) {
          g.drawString("caught expected NPE for null iterator, int", 20, 120);
        } 
        try {
             g.drawString(emptyIterator, 20, 140);
             g.drawString("FAILURE: No IAE for empty iterator, int",
                           20, 140);
        } catch (IllegalArgumentException e) {
          g.drawString("caught expected IAE for empty iterator, int",
                        20, 140);
        } 


        
        try {
             g.drawString(nullIterator, 20.0f, 160.0f);
             g.drawString("FAILURE: No NPE for null iterator, float", 20, 160);
        } catch (NullPointerException e) {
          g.drawString("caught expected NPE for null iterator, float", 20, 160);
        } 

        try {
             g.drawString(emptyIterator, 20, 180);
             g.drawString("FAILURE: No IAE for empty iterator, float",
                           20, 180);
        } catch (IllegalArgumentException e) {
          g.drawString("caught expected IAE for empty iterator, float",
                        20, 180);
        } 
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
