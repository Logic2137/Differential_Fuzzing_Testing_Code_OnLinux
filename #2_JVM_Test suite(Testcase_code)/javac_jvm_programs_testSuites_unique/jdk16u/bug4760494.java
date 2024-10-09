



import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

public class bug4760494 {

    static JFrame frame = null;

    public static PassedListener pass;
    public static TestStateListener tester;
    public static Robot robot;
    private static volatile boolean pressed = false;
    private static volatile boolean passed = false;
    private static volatile JPopupMenu popup = null;

    public static void main(String args[]) throws Throwable {
        pass = new PassedListener();
        tester = new TestStateListener();
        robot = new Robot();
        SwingUtilities.invokeAndWait(() -> createUI());
        while (!pressed) {
           try {
                Thread.sleep(1000);
           } catch (InterruptedException e) {
           }
        }
        int count = 0;
        while (!passed && count < 10) {
           try {
                count++;
                Thread.sleep(1000);
           } catch (InterruptedException e) {
           }
       }
       SwingUtilities.invokeAndWait(() -> frame.dispose());
       if (!passed) {
           throw new RuntimeException("Menu item not selected");
       }
    }

    static void createUI() {
        frame = new JFrame("Bug 4760494");
        frame.addWindowListener(tester);

        popup = new JPopupMenu();
        JMenuItem popupItem = popup.add(new JMenuItem("Test item"));
        popupItem.setMnemonic('T');
        popupItem.addActionListener(new PassedListener());

        frame.addMouseListener(new MouseAdapter() {
            public void mouseReleased( MouseEvent e ){
                popup.show(frame,e.getX(),e.getY());
            }
        });

        frame.setSize(200, 200);
        frame.setLocation(200, 200);
        frame.setVisible(true);
        frame.toFront();
    }

    public static class PassedListener implements ActionListener {
        public void actionPerformed(ActionEvent ev) {
              passed = true;
              System.out.println("passed!");
        }
    }

    public static class TestStateListener extends WindowAdapter {
        public void windowOpened(WindowEvent ev) {
            try {
                new Thread(new RobotThread()).start();
            } catch (Exception ex) {
                throw new RuntimeException("Thread Exception");
            }
        }
    }

    public static class RobotThread implements Runnable {
        public void run() {
            robot.setAutoDelay(500);
            robot.waitForIdle();
            
            robot.mouseMove(250, 250);
            
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            robot.waitForIdle();
            
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            while (!popup.isVisible()) {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                }
            }
            
            robot.keyPress(KeyEvent.VK_T);
            robot.keyRelease(KeyEvent.VK_T);
            robot.waitForIdle();
            pressed = true;
        }
     }
}
