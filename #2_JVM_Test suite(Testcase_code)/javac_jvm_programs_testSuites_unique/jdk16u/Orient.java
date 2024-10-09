








import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.print.*;




public class Orient implements Printable {

   private static void init()
    {
        

        String[] instructions =
        {
         "On-screen inspection is not possible for this printing-specific",
         "test therefore its only output is three printed pages.",
         "To be able to run this test it is required to have a default",
         "printer configured in your user environment.",
         "",
         "Visual inspection of the printed page is needed. A passing",
         "test will print three pages each containing a large oval ",
         "with the text describing the orientation: PORTRAIT, LANDSCAPE",
         "or REVERSE_LANDSCAPE, inside of it. The first page will ",
         "be emitted in portait orientation, the second page in landscape ",
         "orientation and the third page in reverse-landscape orientation. ",
         "On each page the oval will be wholly within the imageable area ",
         "of the page. In a failing test the oval on the third page ",
         "will be clipped against the imageable area.",
         "Axes will indicate the direction of increasing X and Y"
       };
      Sysout.createDialog( );
      Sysout.printInstructions( instructions );

        PrinterJob pjob = PrinterJob.getPrinterJob();

        Book book = new Book();

        
        PageFormat portrait = pjob.defaultPage();
        portrait.setOrientation(PageFormat.PORTRAIT);
        book.append(new Orient(), portrait);

        
        PageFormat landscape = pjob.defaultPage();
        landscape.setOrientation(PageFormat.LANDSCAPE);
        book.append(new Orient(), landscape);

        
        PageFormat reverseLandscape = pjob.defaultPage();
        reverseLandscape.setOrientation(PageFormat.REVERSE_LANDSCAPE);
        book.append(new Orient(), reverseLandscape);

        pjob.setPageable(book);
        try {
            pjob.print();
        } catch (PrinterException e) {
            e.printStackTrace();
        }

    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {

        Graphics2D g2d = (Graphics2D)g;
        g2d.translate(pf.getImageableX(), pf.getImageableY());
        drawGraphics(g2d, pf);
        return Printable.PAGE_EXISTS;
    }

    void drawGraphics(Graphics2D g, PageFormat pf) {
        double iw = pf.getImageableWidth();
        double ih = pf.getImageableHeight();

        g.setColor(Color.black);
        String orientation;
        switch (pf.getOrientation()) {
           case PageFormat.PORTRAIT  : orientation = "PORTRAIT";
                                       break;
           case PageFormat.LANDSCAPE : orientation = "LANDSCAPE";
                                       break;
           case PageFormat.REVERSE_LANDSCAPE :
                                       orientation = "REVERSE_LANDSCAPE";
                                       break;
           default                   : orientation = "INVALID";
        }
        g.drawString(orientation, 100, 300);
        g.draw(new Ellipse2D.Double(0, 0, iw, ih));
        g.drawString("(0,0)", 5,15);
        g.drawLine(0,0,300,0);
        g.drawString("X", 300,15);
        g.drawLine(0,0,0,300);
        g.drawString("Y",5,300);
    }


   
   private static boolean theTestPassed = false;
   private static boolean testGeneratedInterrupt = false;
   private static String failureMessage = "";

   private static Thread mainThread = null;

   private static int sleepTime = 300000;

   public static void main( String args[] ) throws InterruptedException
    {
      mainThread = Thread.currentThread();
      try
       {
         init();
       }
      catch( TestPassedException e )
       {
         
         
         return;
       }
      
      
      

      
      
      try
       {
         Thread.sleep( sleepTime );
         
         throw new RuntimeException( "Timed out after " + sleepTime/1000 + " seconds" );
       }
      catch (InterruptedException e)
       {
         if( ! testGeneratedInterrupt ) throw e;

         
         testGeneratedInterrupt = false;
         if ( theTestPassed == false )
          {
            throw new RuntimeException( failureMessage );
          }
       }

    }

   public static synchronized void setTimeoutTo( int seconds )
    {
      sleepTime = seconds * 1000;
    }

   public static synchronized void pass()
    {
      Sysout.println( "The test passed." );
      Sysout.println( "The test is over, hit  Ctl-C to stop Java VM" );
      
      if ( mainThread == Thread.currentThread() )
       {
         
         
         
         theTestPassed = true;
         throw new TestPassedException();
       }
      
      
      theTestPassed = true;
      testGeneratedInterrupt = true;
      mainThread.interrupt();
    }

   public static synchronized void fail()
    {
      
      fail( "it just plain failed! :-)" );
    }

   public static synchronized void fail( String whyFailed )
    {
      Sysout.println( "The test failed: " + whyFailed );
      Sysout.println( "The test is over, hit  Ctl-C to stop Java VM" );
      
      if ( mainThread == Thread.currentThread() )
       {
         
         throw new RuntimeException( whyFailed );
       }
      theTestPassed = false;
      testGeneratedInterrupt = true;
      failureMessage = whyFailed;
      mainThread.interrupt();
    }

 }




class TestPassedException extends RuntimeException
 {
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


class TestDialog extends Dialog implements ActionListener
 {

   TextArea instructionsText;
   TextArea messageText;
   int maxStringLength = 80;
   Panel  buttonP = new Panel();
   Button passB = new Button( "pass" );
   Button failB = new Button( "fail" );

   
   public TestDialog( Frame frame, String name )
    {
      super( frame, name );
      int scrollBoth = TextArea.SCROLLBARS_BOTH;
      instructionsText = new TextArea( "", 15, maxStringLength, scrollBoth );
      add( "North", instructionsText );

      messageText = new TextArea( "", 5, maxStringLength, scrollBoth );
      add("Center", messageText);

      passB = new Button( "pass" );
      passB.setActionCommand( "pass" );
      passB.addActionListener( this );
      buttonP.add( "East", passB );

      failB = new Button( "fail" );
      failB.setActionCommand( "fail" );
      failB.addActionListener( this );
      buttonP.add( "West", failB );

      add( "South", buttonP );
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

   
   
   
   public void actionPerformed( ActionEvent e )
    {
      if( e.getActionCommand() == "pass" )
       {
         Orient.pass();
       }
      else
       {
         Orient.fail();
       }
    }

 }
