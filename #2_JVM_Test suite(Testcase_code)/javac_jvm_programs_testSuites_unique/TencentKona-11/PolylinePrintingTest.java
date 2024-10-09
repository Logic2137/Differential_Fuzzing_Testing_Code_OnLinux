



import java.awt.Dialog;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class PolylinePrintingTest implements Printable {

    public int print(Graphics graphics, PageFormat pageFormat,
                     int pageIndex) throws PrinterException {

        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }

        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setStroke(new BasicStroke(25,
                                      BasicStroke.CAP_ROUND,
                                      BasicStroke.JOIN_MITER,
                                      10.0F, null, 1.0F));

        int[] x2Points = {100, 250, 400};
        int[] y2Points = {100, 400, 100};
        drawPolylineGOOD(g2d, x2Points, y2Points);
        drawPolylineBAD(g2d, x2Points, y2Points);

        return PAGE_EXISTS;
    }

    private void drawPolylineGOOD(Graphics2D g2d,
                                  int[] x2Points, int[] y2Points) {

        Path2D polyline =
            new Path2D.Float(Path2D.WIND_EVEN_ODD, x2Points.length);

        polyline.moveTo(x2Points[0], y2Points[0]);

        for (int index = 1; index < x2Points.length; index++) {
                polyline.lineTo(x2Points[index], y2Points[index]);
        }
        g2d.draw(polyline);
    }

    private void drawPolylineBAD(Graphics2D g, int[] xp, int[] yp) {
        int offset = 200;
        g.translate(0, offset);
        g.drawPolyline(xp, yp, xp.length);
    }

    public PolylinePrintingTest() throws PrinterException {
        PrinterJob job = PrinterJob.getPrinterJob();
        PageFormat pf = job.defaultPage();
        Paper p = pf.getPaper();
        p.setImageableArea(0,0,p.getWidth(), p.getHeight());
        pf.setPaper(p);
        job.setPrintable(this, pf);
        if (job.printDialog()) {
            job.print();
        }
    }

    public static void main(String[] args) throws PrinterException {
        String[] instructions = {
             "You must have a printer available to perform this test.",
             "OK the print dialog, and collect the printed page.",
             "Passing test : Output should show two identical chevrons.",
             "Failing test : The line joins will appear different."
           };
        Sysout.createDialog();
        Sysout.printInstructions(instructions);
        new PolylinePrintingTest();
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

