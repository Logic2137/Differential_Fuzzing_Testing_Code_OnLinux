import java.awt.*;
import javax.swing.*;
import java.io.*;

public class bug4530474 {

    private static final Color TEST_COLOR = Color.BLUE;

    private static JEditorPane jep;

    public static void main(String[] args) throws Exception {
        final Robot robot = new Robot();
        robot.setAutoDelay(50);
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });
        robot.waitForIdle();
        robot.delay(500);
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                boolean passed = false;
                Point p = jep.getLocationOnScreen();
                Dimension d = jep.getSize();
                int x0 = p.x;
                int y = p.y + d.height / 3;
                StringBuilder builder = new StringBuilder("Test color: ");
                builder.append(TEST_COLOR.toString());
                builder.append(" resut colors: ");
                for (int x = x0; x < x0 + d.width; x++) {
                    Color color = robot.getPixelColor(x, y);
                    builder.append(color);
                    if (TEST_COLOR.equals(color)) {
                        passed = true;
                        break;
                    }
                }
                if (!passed) {
                    throw new RuntimeException("Test Fail. " + builder.toString());
                }
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame mainFrame = new JFrame("bug4530474");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jep = new JEditorPane();
        try {
            File file = new File(System.getProperty("test.src", "."), "test.html");
            jep.setPage(file.toURL());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mainFrame.getContentPane().add(jep);
        mainFrame.pack();
        mainFrame.setVisible(true);
    }
}
