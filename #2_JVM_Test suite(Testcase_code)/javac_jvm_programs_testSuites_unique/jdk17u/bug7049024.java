import javax.swing.*;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;

public class bug7049024 {

    public static Clipboard clipboard = null;

    public static JTextField textField = null;

    public static JButton button = null;

    public static JFrame frame = null;

    public static DefaultCaret caret = null;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame = new JFrame("Test");
                textField = new JTextField("test selection for textfield");
                button = new JButton("To compete the focus");
                frame.setLayout(new FlowLayout());
                frame.getContentPane().add(textField);
                frame.getContentPane().add(button);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
        robot.waitForIdle();
        clipboard = textField.getToolkit().getSystemSelection();
        if (null == clipboard) {
            return;
        }
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                textField.requestFocusInWindow();
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                caret = (DefaultCaret) textField.getCaret();
                caret.setDot(2);
                caret.moveDot(4);
            }
        });
        robot.waitForIdle();
        String oldSelection = (String) clipboard.getData(DataFlavor.stringFlavor);
        System.out.println("oldSelection is " + oldSelection);
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                button.requestFocusInWindow();
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                caret.setDot(4);
                caret.moveDot(6);
            }
        });
        robot.waitForIdle();
        String newSelection = (String) clipboard.getData(DataFlavor.stringFlavor);
        System.out.println("newSelection is " + newSelection);
        boolean passed = newSelection.equals(oldSelection);
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame.dispose();
            }
        });
        if (!passed) {
            throw new RuntimeException("The test for bug 7049024 failed");
        }
    }
}
