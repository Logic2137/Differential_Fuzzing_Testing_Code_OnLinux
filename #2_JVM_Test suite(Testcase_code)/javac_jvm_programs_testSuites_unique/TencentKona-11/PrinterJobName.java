



import java.awt.*;
import java.awt.print.*;

public class PrinterJobName implements Printable {


  static String theName = "Testing the Jobname setting";

  public static void main(String[] args) {

       String[] instructions =
        {
         "You must have a printer available to perform this test",
         "This test prints a page with a banner/job name of",
          theName
       };

      Sysout.createDialog( );
      Sysout.printInstructions( instructions );

      PrinterJob job = PrinterJob.getPrinterJob();
      job.setJobName(theName);
      job.setPrintable(new PrinterJobName());
      try {
          job.print();
          System.out.println("PRINTING DONE.");
      }
      catch (Exception exc) {
          System.out.println("Printer Exception");
      }
  }


    public int print(Graphics g, PageFormat pgFmt, int pgIndex) {
      if (pgIndex > 0 ) {
          return Printable.NO_SUCH_PAGE;
      }

      double iw = pgFmt.getImageableWidth();
      double ih = pgFmt.getImageableHeight();
      Graphics2D g2d = (Graphics2D)g;
      g2d.translate(pgFmt.getImageableX(), pgFmt.getImageableY());
      g2d.drawString("Name is: "+theName,20,20 );
      return Printable.PAGE_EXISTS;
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
