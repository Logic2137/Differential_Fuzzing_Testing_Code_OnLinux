import java.awt.FlowLayout;
import java.awt.TextArea;
import java.awt.Robot;
import java.lang.reflect.Field;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultCaret;
import sun.awt.AWTAccessor;
import sun.awt.AWTAccessor.ComponentAccessor;

public class bug7129742 {

    public static DefaultCaret caret = null;

    public static JFrame frame = null;

    public static boolean fastreturn = false;

    public static void main(String[] args) throws Exception {
        Robot robot = new Robot();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame = new JFrame("Test");
                TextArea textArea = new TextArea("Non-editable textArea");
                textArea.setEditable(false);
                frame.setLayout(new FlowLayout());
                frame.add(textArea);
                frame.pack();
                frame.setVisible(true);
                try {
                    ComponentAccessor acc = AWTAccessor.getComponentAccessor();
                    Class XTextAreaPeerClzz = acc.getPeer(textArea).getClass();
                    System.out.println(XTextAreaPeerClzz.getName());
                    if (!XTextAreaPeerClzz.getName().equals("sun.awt.X11.XTextAreaPeer")) {
                        fastreturn = true;
                        return;
                    }
                    Field jtextField = XTextAreaPeerClzz.getDeclaredField("jtext");
                    jtextField.setAccessible(true);
                    JTextArea jtext = (JTextArea) jtextField.get(acc.getPeer(textArea));
                    caret = (DefaultCaret) jtext.getCaret();
                    textArea.requestFocusInWindow();
                } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                    frame.dispose();
                    throw new RuntimeException("This testcase is not valid any more!");
                }
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                try {
                    if (fastreturn) {
                        return;
                    }
                    boolean passed = caret.isActive();
                    System.out.println("is caret visible : " + passed);
                    if (!passed) {
                        throw new RuntimeException("The test for bug 71297422 failed");
                    }
                } finally {
                    frame.dispose();
                }
            }
        });
    }
}
