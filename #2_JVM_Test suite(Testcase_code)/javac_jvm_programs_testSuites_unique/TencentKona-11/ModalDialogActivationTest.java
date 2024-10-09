

import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JDialog;
import javax.swing.JFrame;



public class ModalDialogActivationTest {
    static final Object lock = new Object();
    static volatile boolean activated;
    static volatile boolean focused;

    public static void main(String[] args) throws InterruptedException {
        EventQueue.invokeLater(() -> runGUI());

        long time = System.currentTimeMillis();
        synchronized (lock) {
            while (!activated || !focused) {
                lock.wait(5000);
                if (System.currentTimeMillis() - time >= 5000) break;
            }
        }
        if (!activated || !focused) {
            throw new RuntimeException("Test FAILED: activated: " + activated
                                        + ", focused: " + focused);
        }
        System.out.println("Test PASSED");
    }

    static void runGUI() {
        JFrame f = new JFrame("frame");
        final JDialog d = new MyModalDialog(f, "dialog");
        d.addWindowListener(new WindowAdapter() {
            @Override
            public void windowActivated(WindowEvent e) {
                synchronized (lock) {
                    activated = true;
                    lock.notifyAll();
                }
            }
        });
        d.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                synchronized (lock) {
                    focused = true;
                    lock.notifyAll();
                }
            }
        });
        f.setVisible(true);
        d.setVisible(true);
    }

    static class MyModalDialog extends JDialog {
        public MyModalDialog(Frame owner, String title) {
            super(owner, title, true);
        }

        @Override
        public boolean getFocusableWindowState() {
            try {
                
                Thread.sleep(100);
            } catch (InterruptedException ignore) {
            }
            return super.getFocusableWindowState();
        }
    }
}
