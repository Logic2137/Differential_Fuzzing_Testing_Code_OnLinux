



import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class CtrlSpace extends Frame implements KeyListener {

    static volatile boolean testPassed = false;
    static volatile Robot robot;

    public static void main(String[] args) throws Exception {

        robot = new Robot();
        robot.setAutoWaitForIdle(true);
        robot.setAutoDelay(100);

        Frame frame = createAndShowGUI(robot);

        test(robot);
        robot.waitForIdle();
        Thread.sleep(2000);
        frame.setVisible(false);
        frame.dispose();
        if (!testPassed) {
            throw new RuntimeException("No KeyTyped event");
        }
    }


   static Frame createAndShowGUI(Robot robot) {
        CtrlSpace win = new CtrlSpace();
        win.setSize(300, 300);
        Panel panel = new Panel(new BorderLayout());
        TextField textField = new TextField("abcdefghijk");
        textField.addKeyListener(win);
        panel.add(textField, BorderLayout.CENTER);
        win.add(panel);
        win.setVisible(true);
        robot.waitForIdle();
        textField.requestFocusInWindow();
        robot.waitForIdle();
        return win;
    }

    public static void test(Robot robot) {
        robot.keyPress(KeyEvent.VK_CONTROL);
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_CONTROL);
        robot.delay(200);
    }

    public void keyPressed(KeyEvent evt) {
        System.out.println("Pressed " + evt);
    }

    public void keyReleased(KeyEvent evt) {
        System.out.println("Released " + evt);
    }

    public void keyTyped(KeyEvent evt) {
        System.out.println("Typed " + evt);
        testPassed = true;
    }

}
