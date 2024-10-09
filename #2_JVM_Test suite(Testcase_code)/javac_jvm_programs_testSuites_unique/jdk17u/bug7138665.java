import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class bug7138665 {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                JOptionPane pane = new JOptionPane("Enter value", JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION, null, null, null);
                pane.setWantsInput(true);
                JDialog dialog = pane.createDialog(null, "My Dialog");
                dialog.setVisible(true);
                Object result = pane.getValue();
                if (result == null || ((Integer) result).intValue() != JOptionPane.OK_OPTION) {
                    throw new RuntimeException("Invalid result: " + result);
                }
                System.out.println("Test bug7138665 passed");
            }
        });
        Robot robot = new Robot();
        robot.waitForIdle();
        robot.setAutoDelay(100);
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        robot.waitForIdle();
    }
}
