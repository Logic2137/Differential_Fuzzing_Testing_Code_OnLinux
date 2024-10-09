



import java.awt.*;
import java.awt.print.*;
import java.awt.geom.*;

public class CustomPaper implements Pageable, Printable{

  private static double PIXELS_PER_INCH = 72.0;

  private PrinterJob printerJob;
  private PageFormat pageFormat;

  CustomPaper(){
    printerJob = PrinterJob.getPrinterJob();
    createPageFormat();
  }

  private void createPageFormat(){
    pageFormat = new PageFormat();
    Paper p = new Paper();
    double width   = 12.0*PIXELS_PER_INCH;
    double height  = 14.0*PIXELS_PER_INCH;
    double ix      = PIXELS_PER_INCH;
    double iy      = PIXELS_PER_INCH;
    double iwidth  = width  - 2.0*PIXELS_PER_INCH;
    double iheight = height - 2.0*PIXELS_PER_INCH;
    p.setSize(width, height);
    p.setImageableArea(ix, iy, iwidth, iheight);
    pageFormat.setPaper(p);
  }

  public Printable getPrintable(int index){
    return this;
  }

  public PageFormat getPageFormat(int index){
    return pageFormat;
  }

  public int getNumberOfPages(){
    return 1;
  }

  public void print(){
    if(printerJob.printDialog())
        {
      try{
        printerJob.setPageable(this);
        printerJob.print();
      }catch(Exception e){e.printStackTrace();}
    }

  }

  public int print(Graphics g, PageFormat pf, int pageIndex){
    if(pageIndex == 0){
        Graphics2D g2 = (Graphics2D)g;
        Rectangle2D r = new Rectangle2D.Double(pf.getImageableX(),
                                               pf.getImageableY(),
                                               pf.getImageableWidth(),
                                               pf.getImageableHeight());
      g2.setStroke(new BasicStroke(3.0f));
      g2.draw(r);
      return PAGE_EXISTS;
    }else{
      return NO_SUCH_PAGE;
    }
  }

  public static void main(String[] args){

        String[] instructions =
        {
            "You must have a printer that supports custom paper size of ",
            "at least 12 x 14 inches to perform this test. It requires",
            "user interaction and you must have a 12 x 14 inch paper available.",
            " ",
            "To test bug ID 4385157, click OK on print dialog box to print.",
            " ",
            "To test bug ID 4355514, select the printer in the Print Setup dialog and add a ",
            "custom paper size under Printer properties' Paper selection menu. ",
            "Set the dimension  to width=12 inches and height=14 inches.",
            "Select this custom paper size before proceeding to print.",
            " ",
            "Visual inspection of the one-page printout is needed. A passing",
            "test will print a rectangle of the imageable area which is approximately",
            "10 x 12 inches.",
        };
        Sysout.createDialog( );
        Sysout.printInstructions( instructions );

        CustomPaper pt = new CustomPaper();
        pt.print();
        
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
