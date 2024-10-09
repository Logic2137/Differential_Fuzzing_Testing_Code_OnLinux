import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.plaf.basic.BasicLookAndFeel;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

public class bug4885629 {

    private static final Color darkShadow = new Color(100, 120, 200);

    private static final Color darkHighlight = new Color(200, 120, 50);

    private static final Color lightHighlight = darkHighlight.brighter();

    private static final Color BGCOLOR = Color.blue;

    private static JSplitPane sp;

    public static void main(String[] args) throws Exception {
        UIManager.setLookAndFeel(new BasicLookAndFeel() {

            public boolean isSupportedLookAndFeel() {
                return true;
            }

            public boolean isNativeLookAndFeel() {
                return false;
            }

            public String getDescription() {
                return "Foo";
            }

            public String getID() {
                return "FooID";
            }

            public String getName() {
                return "FooName";
            }
        });
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                JFrame frame = new JFrame();
                JComponent a = new JPanel();
                a.setBackground(Color.white);
                a.setMinimumSize(new Dimension(10, 10));
                JComponent b = new JPanel();
                b.setBackground(Color.white);
                b.setMinimumSize(new Dimension(10, 10));
                sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, a, b);
                sp.setPreferredSize(new Dimension(20, 20));
                sp.setBackground(BGCOLOR);
                Border bo = new BasicBorders.SplitPaneBorder(lightHighlight, Color.red);
                Border ibo = new EmptyBorder(0, 0, 0, 0);
                sp.setBorder(bo);
                sp.setMinimumSize(new Dimension(200, 200));
                ((BasicSplitPaneUI) sp.getUI()).getDivider().setBorder(ibo);
                frame.getContentPane().setLayout(new FlowLayout());
                frame.getContentPane().setBackground(darkShadow);
                frame.getContentPane().add(sp);
                frame.setSize(200, 200);
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setVisible(true);
            }
        });
        final Robot robot = new Robot();
        robot.waitForIdle();
        robot.delay(1000);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                Rectangle rect = ((BasicSplitPaneUI) sp.getUI()).getDivider().getBounds();
                Point p = rect.getLocation();
                SwingUtilities.convertPointToScreen(p, sp);
                for (int i = 0; i < rect.width; i++) {
                    if (!BGCOLOR.equals(robot.getPixelColor(p.x + i, p.y + rect.height - 1))) {
                        throw new Error("The divider's area has incorrect color.");
                    }
                }
            }
        });
    }
}
