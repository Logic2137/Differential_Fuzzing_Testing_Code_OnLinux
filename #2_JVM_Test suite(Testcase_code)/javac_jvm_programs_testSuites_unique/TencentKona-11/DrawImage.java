



import java.util.*;
import java.text.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.awt.print.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.image.renderable.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;
import javax.swing.event.*;

public class DrawImage
{
    protected static final double _hwBorder = 72 / 4;       
    protected static final double _border = 72 / 4;         
    protected static final int _objectBorder = 15;
    protected static final int _verticalGap = 20;
    protected static final int _textIndent = 150;

    protected BufferedImage _image;

    protected PageFormat  _pageFormat;

    public DrawImage(BufferedImage image) {
        _image = image;
        PrinterJob pj = PrinterJob.getPrinterJob();
        _pageFormat = pj.defaultPage();

 }


    protected int printImage(Graphics g, PageFormat pf, BufferedImage image) {
        Graphics2D g2D = (Graphics2D)g;
        g2D.transform(new AffineTransform(_pageFormat.getMatrix()));

        int paperW = (int)pf.getImageableWidth(), paperH =
            (int)pf.getImageableHeight();

        int x = (int)pf.getImageableX(), y = (int)pf.getImageableY();
        g2D.setClip(x, y, paperW, paperH);

        
        if (image != null ) {
            int imageH = image.getHeight(), imageW = image.getWidth();
            
            float scaleFactor = ((float)((paperW - 25) - _objectBorder -
                                         _objectBorder) / (float)(imageW));
            int scaledW = (int)(imageW * scaleFactor),
                scaledH = (int)(imageH *scaleFactor);
            BufferedImageOp scaleOp = new RescaleOp(scaleFactor, 0, null);
            g2D.drawImage(image, scaleOp, x + _objectBorder, y + _objectBorder);
            y += _objectBorder + scaledH + _objectBorder;
            return Printable.PAGE_EXISTS;
        }
        else {
            return Printable.NO_SUCH_PAGE;
        }
    }

    public void print() {
        try {
            final PrinterJob pj = PrinterJob.getPrinterJob();
            pj.setJobName("Print Image");
            pj.setPrintable(new Printable() {
                public int print(Graphics g, PageFormat pf, int pageIndex) {
                    int result = NO_SUCH_PAGE;
                    if (pageIndex == 0) {
                        result = printImage(g, _pageFormat, _image);
                    }
                    return result;
                }
            });
            if (pj.printDialog()) {
                try { pj.print(); }
                catch (PrinterException e) {
                    System.out.println(e);
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    public static void main(String[] args) {
                                String[] instructions =
           {
            "You must have a printer available to perform this test.",
            "The test passes if you get a printout of a gray rectangle",
                                                "with white text without any exception."
          };

         Sysout.createDialog( );
         Sysout.printInstructions( instructions );

        BufferedImage image = prepareFrontImage();
        DrawImage pt = new DrawImage(image);
        pt.print();
        
    }



    public static BufferedImage prepareFrontImage() {
        
        BufferedImage result = new BufferedImage(400, 200,
                                                 BufferedImage.TYPE_BYTE_GRAY);

        Graphics2D g2D = (Graphics2D)result.getGraphics();
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                             RenderingHints.VALUE_ANTIALIAS_OFF);
        int w = result.getWidth(), h = result.getHeight();

        g2D.setColor(Color.gray);
        g2D.fill(new Rectangle(0, 0, w, h));

        g2D.setColor(Color.white);

        AffineTransform original = g2D.getTransform();
        AffineTransform originXform = AffineTransform.getTranslateInstance(w /
5, h / 5);
        g2D.transform(originXform);


        g2D.drawString("Front Side", 20, h / 2);

        return result;
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
