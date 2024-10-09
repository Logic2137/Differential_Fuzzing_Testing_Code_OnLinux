



import java.awt.*;

public class NonEDT_GUI_Deadlock {
    boolean bOK = false;
    Thread badThread = null;

    public void start ()
    {
        final Frame theFrame = new Frame("Window test");
        theFrame.setSize(240, 200);

        Thread thKiller = new Thread() {
           public void run() {
              try {
                 Thread.sleep( 9000 );
              }catch( Exception ex ) {
              }
              if( !bOK ) {
                 
                 
                 Runtime.getRuntime().halt(0);
              }else{
                 
              }
           }
        };
        thKiller.setName("Killer thread");
        thKiller.start();
        Window w = new TestWindow(theFrame);
        theFrame.toBack();
        theFrame.setVisible(true);

        theFrame.setLayout(new FlowLayout(FlowLayout.CENTER));
        EventQueue.invokeLater(new Runnable() {
           public void run() {
               bOK = true;
           }
        });



    }
    class TestWindow extends Window implements Runnable {

        TestWindow(Frame f) {
            super(f);

            
            setLocation(0, 75);

            show();
            toFront();

            badThread = new Thread(this);
            badThread.setName("Bad Thread");
            badThread.start();

        }

        public void paint(Graphics g) {
            g.drawString("Deadlock or no deadlock?",20,80);
        }

        public void run() {

            long ts = System.currentTimeMillis();

            while (true) {
                if ((System.currentTimeMillis()-ts)>3000) {
                    this.setVisible( false );
                    dispose();
                    break;
                }

                toFront();
                try {
                    Thread.sleep(80);
                } catch (Exception e) {
                }
            }
        }
    }



    public static void main(String args[]) {
       NonEDT_GUI_Deadlock imt = new NonEDT_GUI_Deadlock();
       imt.start();
    }


}
