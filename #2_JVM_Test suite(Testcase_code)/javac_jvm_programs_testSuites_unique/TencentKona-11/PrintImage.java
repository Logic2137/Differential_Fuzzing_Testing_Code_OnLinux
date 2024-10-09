


import java.awt.*;
import java.awt.print.*;
import java.awt.event.*;

public class PrintImage extends Frame implements ActionListener {

        private PrintImageCanvas                printImageCanvas;

        private MenuItem        print1Menu = new MenuItem("PrintTest1");
        private MenuItem        print2Menu = new MenuItem("PrintTest2");
        private MenuItem        exitMenu = new MenuItem("Exit");

        public static void main(String[] argv) {
        String[] instructions =
           { "You must have a printer available to perform this test,",
             "prefererably Canon LaserShot A309GII.",
             "Printing must be done in Win 98 Japanese 2nd Edition.",
             "",
             "Passing test : Output of text image for PrintTest1 and PrintTest2 should be same as that on the screen.",
           };

        Sysout.createDialog( );
         Sysout.printInstructions( instructions );

                new PrintImage();
        }

        public PrintImage() {
                super("PrintImage");
                initPrintImage();
        }

        public void initPrintImage() {

                printImageCanvas = new PrintImageCanvas(this);

                initMenu();

                addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent ev) {
                                dispose();
                        }
                        public void windowClosed(WindowEvent ev) {
                                System.exit(0);
                        }
                });

                setLayout(new BorderLayout());
                add(printImageCanvas, BorderLayout.CENTER);
                pack();

                setSize(500,500);
                setVisible(true);
        }

        private void initMenu() {
                MenuBar         mb = new MenuBar();
                Menu            me = new Menu("File");
                me.add(print1Menu);
                me.add(print2Menu);
                me.add("-");
                me.add(exitMenu);
                mb.add(me);
                this.setMenuBar(mb);

                print1Menu.addActionListener(this);
                print2Menu.addActionListener(this);
                exitMenu.addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
                Object target = e.getSource();
                if( target.equals(print1Menu) ) {
                        printMain1();
                }
                else if( target.equals(print2Menu) ) {
                        printMain2();
                }
                else if( target.equals(exitMenu) ) {
                        dispose();
                }
        }

        private void printMain1(){

                PrinterJob printerJob = PrinterJob.getPrinterJob();
                PageFormat pageFormat = printerJob.defaultPage();

                printerJob.setPrintable((Printable)printImageCanvas, pageFormat);

                if(printerJob.printDialog()){
                        try {
                                printerJob.print();
                        }
                        catch(PrinterException p){
                        }
                }
                else
                        printerJob.cancel();
        }

        private void printMain2(){

                PrinterJob printerJob = PrinterJob.getPrinterJob();
                PageFormat pageFormat = printerJob.pageDialog(printerJob.defaultPage());

                printerJob.setPrintable((Printable)printImageCanvas, pageFormat);

                if(printerJob.printDialog()){
                        try {
                                printerJob.print();
                        }
                        catch(PrinterException p){
                        }
                }
                else
                        printerJob.cancel();
        }

}

class PrintImageCanvas extends Canvas implements Printable {

        private PrintImage pdsFrame;

        public PrintImageCanvas(PrintImage pds) {
                pdsFrame = pds;
        }

        public void paint(Graphics g) {
                Font drawFont = new Font("MS Mincho",Font.ITALIC,50);
                g.setFont(drawFont);
                g.drawString("PrintSample!",100,150);
        }

        public int print(Graphics g, PageFormat pf, int pi)
                throws PrinterException {

                if(pi>=1)
                        return NO_SUCH_PAGE;
                else{
                        Graphics2D g2 = (Graphics2D)g;
                        g.setColor(new Color(0,0,0,200));

                        Font drawFont = new Font("MS Mincho",Font.ITALIC,50);
                        g.setFont(drawFont);
                        g.drawString("PrintSample!",100,150);
                        return PAGE_EXISTS;
                }
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
