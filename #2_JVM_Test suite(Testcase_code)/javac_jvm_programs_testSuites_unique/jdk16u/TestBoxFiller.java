


import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.KeyboardFocusManager;
import java.beans.PropertyChangeEvent;
import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class TestBoxFiller
{
    private static JFrame frame;
    private static void showFocusOwner(PropertyChangeEvent e)
    {
        Object c = e.getNewValue();
        if (c instanceof Box.Filler) {
            throw new RuntimeException("Box.Filler having focus");
        }
    }

    public static void main(String[] args) throws Exception
    {
        try {
            Robot robot = new Robot();
            robot.setAutoDelay(100);
            SwingUtilities.invokeAndWait(() -> {
                frame = new JFrame();
                KeyboardFocusManager m = KeyboardFocusManager.getCurrentKeyboardFocusManager();
                m.addPropertyChangeListener("focusOwner", TestBoxFiller::showFocusOwner);

                Box box = Box.createHorizontalBox();
                JTextField tf1 = new JTextField("Test");
                tf1.setColumns(40);
                JTextField tf2 = new JTextField("Test");
                tf2.setColumns(40);
                box.add(tf1);
                box.add(Box.createHorizontalStrut(20));
                box.add(tf2);
                frame.setContentPane(box);
                frame.setFocusTraversalPolicy(new ContainerOrderFocusTraversalPolicy());
                frame.pack();
                frame.setVisible(true);
                frame.setLocationRelativeTo(null);
                tf1.requestFocusInWindow();
            });
            robot.waitForIdle();
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
        } finally {
            if (frame != null) {
                SwingUtilities.invokeAndWait(frame::dispose);
            }
        }
    }
}
