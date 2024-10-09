

import java.awt.*;
import java.awt.print.*;
import java.awt.GraphicsEnvironment;

public class PrintFontStyle {
    public static void main(String[] args) {

        String[] instructions =
        {
            "You must have a printer available to perform this test and should use Win 98.",
            "This bug is system dependent and is not always reproducible.",
            " ",
            "A passing test will have all text printed with correct font style.",
        };

        Sysout.createDialog( );
        Sysout.printInstructions( instructions );

        PrinterJob pj=PrinterJob.getPrinterJob();
        pj.setPrintable(new FontPrintable());
        if (pj.printDialog())
            {
                try { pj.print(); }
                catch (PrinterException e) {
                    System.out.println(e);
                }
            }
    }
}

class FontPrintable
    implements Printable {

    public int print(Graphics g, PageFormat pf, int pageIndex) {
        if (pageIndex != 0) return NO_SUCH_PAGE;
        Graphics2D g2= (Graphics2D)g;

        g2.setPaint(Color.black);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontList = ge.getAvailableFontFamilyNames();
        g2.setFont (new Font ("Arial", Font.PLAIN, 20));
        g2.drawString("Arial - Plain", 144, 120);
        g2.setFont (new Font ("Arial", Font.BOLD, 20));
        g2.drawString("Arial - Bold", 144, 145);
        g2.setFont (new Font ("Arial", Font.ITALIC, 20));
        g2.drawString("Arial - Italic", 144, 170);
        g2.setFont (new Font ("Times New Roman", Font.PLAIN, 20));
        g2.drawString("Times New Roman - Plain", 144, 195);
        g2.setFont (new Font ("Times New Roman", Font.BOLD, 20));
        g2.drawString("Times New Roman - Bold", 144, 220);
        g2.setFont (new Font ("Times New Roman", Font.ITALIC, 20));
        g2.drawString("Times New Roman - Italic", 144, 245);

        return PAGE_EXISTS;
    }
}

class Sysout {
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
