

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.JEditorPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.JTextComponent;

public class TestNimbusBGColor {

    static JFrame frame;
    static volatile Point pt;
    static volatile Rectangle bounds;
    static Robot robot;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        testTextPane();
        testEditorPane();
    }

    private interface ComponentCreator<T extends JTextComponent> {
        T createComponent();
    }

    private static void testTextPane() throws Exception {
        testComponent(JTextPane::new);
    }

    private static void testEditorPane() throws Exception {
        testComponent(() -> {
            JEditorPane ep = new JEditorPane();
            ep.setContentType("text/plain");
            return ep;
        });
    }

    private static void testComponent(ComponentCreator<? extends JTextComponent> creator)
            throws Exception {
        try {
            SwingUtilities.invokeAndWait(() -> {
                try {
                    UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
                } catch (Exception checkedExceptionsPleaseDie) {
                    throw new RuntimeException(checkedExceptionsPleaseDie);
                }
                JTextComponent tc = creator.createComponent();
                tc.setEditable(false);
                tc.setForeground(Color.GREEN);
                tc.setBackground(Color.RED);
                tc.setText("This text should be green on red");

                frame = new JFrame(tc.getClass().getName());
                frame.setDefaultCloseOperation(frame.DISPOSE_ON_CLOSE);
                frame.add(tc);
                frame.setSize(new Dimension(480, 360));
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            });
            robot.waitForIdle();
            robot.delay(1000);
            SwingUtilities.invokeAndWait(() -> {
                pt = frame.getLocationOnScreen();
                bounds = frame.getBounds();
            });
            if (!(robot.getPixelColor(pt.x + bounds.width/2,
                                      pt.y + bounds.height/2)
                                .equals(Color.RED))) {
                throw new RuntimeException("bg Color not same as the color being set");
            }
        } finally {
            SwingUtilities.invokeAndWait(() -> {
                if (frame != null) {
                    frame.dispose();
                }
            });
        }
    }
}
