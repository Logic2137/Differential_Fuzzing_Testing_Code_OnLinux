


import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class bug6578666 {

    private static JTree tree;

    private static void createGui() {
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        tree = new JTree();
        frame.add(tree);

        tree.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                tree.updateUI();
            }
        });

        frame.setSize(200, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(10);

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                bug6578666.createGui();
            }
        });

        robot.waitForIdle();

        tree.requestFocus();
        robot.waitForIdle();

        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);
        robot.waitForIdle();
        robot.keyPress(KeyEvent.VK_SPACE);
        robot.keyRelease(KeyEvent.VK_SPACE);

    }
}
