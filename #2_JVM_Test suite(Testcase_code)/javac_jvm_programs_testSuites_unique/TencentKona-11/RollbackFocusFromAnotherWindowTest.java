



import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class RollbackFocusFromAnotherWindowTest extends JFrame
                                                          implements KeyListener
{
    private static RollbackFocusFromAnotherWindowTest tfs;
    private static Robot robot;

    public static void main(String[] args) throws Exception {
        robot = new Robot();

        SwingUtilities.invokeAndWait(() -> {
            tfs = new RollbackFocusFromAnotherWindowTest();
            tfs.setVisible(true);
        });

        robot.waitForIdle();
        robot.delay(200);

        try {
            for (int i = 0; i < 10; i++) {
                robot.keyPress(KeyEvent.VK_A);
                robot.delay(10);
                robot.keyRelease(KeyEvent.VK_A);
                robot.waitForIdle();
                robot.delay(200);
                SwingUtilities.invokeAndWait(() -> {
                    String name = tfs.getFocusOwner().getName();
                    System.out.println(name);
                    if (!"Comp0".equals(name)) {
                        throw new RuntimeException(
                                "Focus is not restored correctly");
                    }
                });
            }
            System.out.println("ok");
        } finally {
            SwingUtilities.invokeLater(() -> tfs.dispose());
        }
    }

    public RollbackFocusFromAnotherWindowTest()
    {
        setUndecorated(true);
        Container c = getContentPane();
        c.setLayout(new FlowLayout());
        for (int i = 0; i < 10; i++)
        {
            JTextField tf = new JTextField("" + i, 10);
            tf.setName("Comp" + i);
            c.add(tf);
            tf.addKeyListener(this);
        }
        pack();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        Frame frame = new Frame();
        frame.setVisible(true);
        try {
            Thread.sleep(2);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        frame.dispose();
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
