

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Robot;
import java.awt.event.KeyEvent;


public final class MacOSGoToFolderCrash {

    public static void main(final String[] args) throws Exception {
        EventQueue.invokeLater(() -> {
            FileDialog fd = new FileDialog((Frame) null);
            fd.setVisible(true);
        });
        Robot robot = new Robot();
        robot.setAutoDelay(400);
        robot.waitForIdle();
        
        robot.keyPress(KeyEvent.VK_META);
        robot.keyPress(KeyEvent.VK_SHIFT);
        robot.keyPress(KeyEvent.VK_G);
        robot.keyRelease(KeyEvent.VK_G);
        robot.keyRelease(KeyEvent.VK_SHIFT);
        robot.keyRelease(KeyEvent.VK_META);
        
        robot.keyPress(KeyEvent.VK_DOWN);
        robot.keyRelease(KeyEvent.VK_DOWN);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        
        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
    }
}
