


















import java.applet.Applet;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.*;













public class NoFormatsCrashTest extends Applet {

    final Frame frame = new Frame();
    private volatile Process process;

    static final int FRAME_ACTIVATION_TIMEOUT = 2000;

    public static void main(String[] args) {
        NoFormatsCrashTest test = new NoFormatsCrashTest();
        test.run(args);
    }

    public void run(String[] args) {
        try {
            if (args.length != 4) {
                throw new RuntimeException("Incorrect command line arguments.");
            }

            int x = Integer.parseInt(args[0]);
            int y = Integer.parseInt(args[1]);
            int w = Integer.parseInt(args[2]);
            int h = Integer.parseInt(args[3]);

            Panel panel = new DragSourcePanel();

            frame.setTitle("Drag source frame");
            frame.setLocation(500, 200);
            frame.add(panel);
            frame.pack();
            frame.setVisible(true);

            Thread.sleep(FRAME_ACTIVATION_TIMEOUT);

            Point sourcePoint = panel.getLocationOnScreen();
            Dimension d = panel.getSize();
            sourcePoint.translate(d.width / 2, d.height / 2);

            Point targetPoint = new Point(x + w / 2, y + h / 2);

            Robot robot = new Robot();
            robot.mouseMove(sourcePoint.x, sourcePoint.y);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.mousePress(InputEvent.BUTTON1_MASK);
            for (; !sourcePoint.equals(targetPoint);
                 sourcePoint.translate(sign(targetPoint.x - sourcePoint.x),
                                       sign(targetPoint.y - sourcePoint.y))) {
                robot.mouseMove(sourcePoint.x, sourcePoint.y);
                Thread.sleep(50);
            }
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
            robot.keyRelease(KeyEvent.VK_CONTROL);

            Thread.sleep(FRAME_ACTIVATION_TIMEOUT);

            if (process.isAlive()) {
                process.destroy();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    } 

    public void init() {
        
        
        

        String[] instructions =
        {
            "This is an AUTOMATIC test",
            "simply wait until it is done"
        };
        Sysout.createDialog( );
        Sysout.printInstructions( instructions );

        frame.setTitle("Drop target frame");
        frame.setLocation(200, 200);

    } 

    public void start() {
        DropTargetPanel panel = new DropTargetPanel();
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);

        try {
            Thread.sleep(FRAME_ACTIVATION_TIMEOUT);

            Point p = frame.getLocationOnScreen();
            Dimension d = frame.getSize();

            String javaPath = System.getProperty("java.home", "");
            String command = javaPath + File.separator + "bin" +
                File.separator + "java -cp " + System.getProperty("test.classes", ".") +
                " NoFormatsCrashTest " +
                p.x + " " + p.y + " " + d.width + " " + d.height;

            process = Runtime.getRuntime().exec(command);
            ProcessResults pres = ProcessResults.doWaitFor(process);
            System.err.println("Child VM return code: " + pres.exitValue);

            if (pres.stderr != null && pres.stderr.length() > 0) {
                System.err.println("========= Child VM System.err ========");
                System.err.print(pres.stderr);
                System.err.println("======================================");
            }

            if (pres.stdout != null && pres.stdout.length() > 0) {
                System.err.println("========= Child VM System.out ========");
                System.err.print(pres.stdout);
                System.err.println("======================================");
            }

        } catch (Throwable e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        if (panel.isTestFailed()) {
            throw new RuntimeException();
        }
    } 

    public static int sign(int n) {
        return n < 0 ? -1 : n > 0 ? 1 : 0;
    }
} 

class TestTransferable implements Transferable {

    public static DataFlavor dataFlavor = null;
    static final Object data = new Object();

    static {
        DataFlavor df = null;
        try {
            df = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType +
                                "; class=java.lang.Object");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError(e);
        }
        dataFlavor = df;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { dataFlavor };
    }

    public boolean isDataFlavorSupported(DataFlavor df) {
        return dataFlavor.equals(df);
    }

    public Object getTransferData(DataFlavor df)
      throws UnsupportedFlavorException, IOException {
        if (!isDataFlavorSupported(df)) {
            throw new UnsupportedFlavorException(df);
        }
        return data;
    }
}

class DragSourcePanel extends Panel {
    public DragSourcePanel() {
        final Transferable t = new TestTransferable();
        final DragSourceListener dsl = new DragSourceAdapter() {
                public void dragDropEnd(DragSourceDropEvent dtde) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    
                    System.exit(0);
                }
            };
        final DragGestureListener dgl = new DragGestureListener() {
                public void dragGestureRecognized(DragGestureEvent dge) {
                    dge.startDrag(null, t, dsl);
                }
            };
        final DragSource ds = DragSource.getDefaultDragSource();
        final DragGestureRecognizer dgr =
            ds.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY,
                                                  dgl);
    }

    public Dimension getPreferredSize() {
        return new Dimension(100, 100);
    }
}

class DropTargetPanel extends Panel {
    private boolean testFailed = false;
    public DropTargetPanel() {
        final DropTargetListener dtl = new DropTargetAdapter() {
                public void dragOver(DropTargetDragEvent dtde) {
                    try {
                        dtde.getCurrentDataFlavorsAsList();
                    } catch (Exception e) {
                        testFailed = true;
                        e.printStackTrace();
                    }
                }
                public void drop(DropTargetDropEvent dtde) {
                    dtde.rejectDrop();
                }
            };
        final DropTarget dt = new DropTarget(this, dtl);
    }

    public boolean isTestFailed() {
        return testFailed;
    }

    public Dimension getPreferredSize() {
        return new Dimension(100, 100);
    }
}

class ProcessResults {
    public int exitValue;
    public String stdout;
    public String stderr;

    public ProcessResults() {
        exitValue = -1;
        stdout = "";
        stderr = "";
    }

    
    public static ProcessResults doWaitFor(Process p) {
        ProcessResults pres = new ProcessResults();

        InputStream in = null;
        InputStream err = null;

        try {
            in = p.getInputStream();
            err = p.getErrorStream();

            boolean finished = false;

            while (!finished) {
                try {
                    while (in.available() > 0) {
                        pres.stdout += (char)in.read();
                    }
                    while (err.available() > 0) {
                        pres.stderr += (char)err.read();
                    }
                    
                    
                    
                    
                    pres.exitValue = p.exitValue();
                    finished  = true;
                }
                catch (IllegalThreadStateException e) {
                    
                    
                    Thread.currentThread().sleep(500);
                }
            }
            if (in != null) in.close();
            if (err != null) err.close();
        }
        catch (Throwable e) {
            System.err.println("doWaitFor(): unexpected exception");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return pres;
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
      add("South", messageText);

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
