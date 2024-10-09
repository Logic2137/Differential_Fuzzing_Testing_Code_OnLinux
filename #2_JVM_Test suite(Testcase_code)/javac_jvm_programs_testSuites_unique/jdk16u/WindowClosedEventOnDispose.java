




import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;


public class WindowClosedEventOnDispose {

    private static int N_LOOPS = 5;
    private static int N_DIALOGS = 2;

    public static void main(String args[]) throws Exception {
        tesWithFrame();
        testWithoutFrame();
        testHidenChildDispose();
        testHidenWindowDispose();
    }

    
    public static void tesWithFrame() throws Exception {
        doTest(true);
    }

    
    public static void testWithoutFrame() throws Exception  {
        System.out.println("Run without owner Frame");
        doTest(false);
    }

    
    public static void testHidenChildDispose() throws Exception {
        JFrame f = new JFrame();
        JDialog dlg = new JDialog(f);
        Listener l = new Listener();
        dlg.addWindowListener(l);
        f.dispose();
        waitEvents();

        assertEquals(0, l.getCount());
    }

    
    public static void testVisibleChildParentDispose() throws Exception {
        JFrame f = new JFrame();
        JDialog dlg = new JDialog(f);
        Listener l = new Listener();
        dlg.addWindowListener(l);
        dlg.setVisible(true);
        f.dispose();
        waitEvents();

        assertEquals(1, l.getCount());
    }

    
    public static void testHidenWindowDispose() throws Exception {
        JFrame f = new JFrame();
        Listener l = new Listener();
        f.addWindowListener(l);
        f.dispose();
        waitEvents();

        assertEquals(0, l.getCount());
    }

    
    private static void doTest(final boolean useFrame) throws Exception {
        final Listener l  = new Listener();
        final JFrame f = new JFrame();

        for (int i = 0; i < N_LOOPS; i++) {

            SwingUtilities.invokeLater(new Runnable() {

                public void run() {
                    JDialog[] dialogs = new JDialog[N_DIALOGS];
                    for (int i = 0; i < N_DIALOGS; i++) {
                        if (useFrame) {
                            dialogs[i]= new JDialog(f);
                        }
                        else {
                            dialogs[i] = new JDialog();
                        }

                        dialogs[i].addWindowListener(l);
                        dialogs[i].setVisible(true);
                    }

                    
                    for (JDialog d : dialogs)
                        d.dispose();

                    f.dispose();
                }
            });
        }

        waitEvents();

        assertEquals(N_DIALOGS * N_LOOPS, l.getCount());
    }

    private static void waitEvents() throws InterruptedException {
        
        while (Toolkit.getDefaultToolkit().getSystemEventQueue().peekEvent() != null)
            Thread.sleep(100);
    }

    
    private static void assertEquals(int expected, int real) throws Exception {
        if (expected != real) {
            throw new Exception("Expected events: " + expected + " Received Events: " + real);
        }
    }

}


class Listener extends WindowAdapter {

    private volatile int count = 0;

    public void windowClosed(WindowEvent e) {
        count++;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
