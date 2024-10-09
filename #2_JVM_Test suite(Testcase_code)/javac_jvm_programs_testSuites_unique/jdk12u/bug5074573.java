



import java.util.*;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class bug5074573 {

    private static JTextComponent textComponent;
    final static String testString = "123 456 789";
    final static String resultString = "456 ";
    final static List<Class<? extends JTextComponent>> textClasses = Arrays.asList(
            JTextArea.class, JEditorPane.class, JTextPane.class,
            JTextField.class, JFormattedTextField.class, JPasswordField.class);

    public static void main(String[] args) throws Exception {
        for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
            UIManager.setLookAndFeel(info.getClassName());
            System.out.println(info);
            for (Class<? extends JTextComponent> clazz : textClasses) {
                boolean res = test(clazz);
                if (!res && clazz != JPasswordField.class) {
                    throw new RuntimeException("failed");
                }
            }
        }
    }

    static boolean test(final Class<? extends JTextComponent> textComponentClass) throws Exception {
        Robot robot = new Robot();
        robot.setAutoWaitForIdle(true);
        robot.setAutoDelay(50);


        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                initialize(textComponentClass);
            }
        });

        robot.waitForIdle();

        
        if (textComponent instanceof JTextField && "Aqua".equals(UIManager.getLookAndFeel().getID())) {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    Caret caret = textComponent.getCaret();
                    int dot = caret.getDot();
                    textComponent.select(dot, dot);
                }
            });

            robot.waitForIdle();
        }

        robot.keyPress(getCtrlKey());
        robot.keyPress(KeyEvent.VK_BACK_SPACE);
        robot.keyRelease(KeyEvent.VK_BACK_SPACE);
        robot.keyRelease(getCtrlKey());
        robot.waitForIdle();

        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                Caret caret = textComponent.getCaret();
                caret.setDot(0);
            }
        });
        robot.waitForIdle();

        robot.keyPress(getCtrlKey());
        robot.keyPress(KeyEvent.VK_DELETE);
        robot.keyRelease(KeyEvent.VK_DELETE);
        robot.keyRelease(getCtrlKey());
        robot.waitForIdle();

        return resultString.equals(getText());
    }

    private static String getText() throws Exception {
        final String[] result = new String[1];

        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                result[0] = textComponent.getText();
            }
        });

        return result[0];
    }

    
    public static int getCtrlKey() {

        if ("Aqua".equals(UIManager.getLookAndFeel().getID())) {
            return KeyEvent.VK_ALT;
        }

        return KeyEvent.VK_CONTROL;
    }

    private static void initialize(Class<? extends JTextComponent> textComponentClass) {
        try {
            JFrame frame = new JFrame();
            textComponent = textComponentClass.newInstance();
            textComponent.setText(testString);
            frame.add(textComponent);
            frame.pack();
            frame.setVisible(true);
            textComponent.requestFocus();
            Caret caret = textComponent.getCaret();
            caret.setDot(textComponent.getDocument().getLength());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
