import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.Robot;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestDispose {

    public static Frame frame = null;

    public static TextArea textArea = null;

    public static volatile Process worker = null;

    public void testDispose() throws InvocationTargetException, InterruptedException {
        Robot robot;
        try {
            robot = new Robot();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unexpected failure");
        }
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame = new JFrame("Test");
                textArea = new TextArea("editable textArea");
                textArea.setEditable(true);
                frame.setLayout(new FlowLayout());
                frame.add(textArea);
                frame.pack();
                frame.setVisible(true);
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                frame.dispose();
            }
        });
        robot.waitForIdle();
    }

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            Runtime.getRuntime().addShutdownHook(new Thread() {

                public void run() {
                    worker.destroy();
                }
            });
            System.out.println(System.getProperty("java.home") + "/bin/java TestDispose workprocess");
            worker = Runtime.getRuntime().exec(System.getProperty("java.home") + "/bin/java TestDispose workprocess");
            worker.waitFor();
            return;
        }
        TestDispose app = new TestDispose();
        app.testDispose();
    }
}
