



import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class LargeWindowPaintTest extends JPanel {

    static volatile JFrame frame = null;
    static volatile LargeWindowPaintTest comp = null;
    static Color color = Color.red;

    public static void main(String[] args) throws Exception {

        SwingUtilities.invokeAndWait(() -> {
            frame = new JFrame("Large Window Paint Test");
            frame.add(comp = new LargeWindowPaintTest());
            frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        });

        Thread.sleep(2000);
        Robot robot = new Robot();
        robot.setAutoDelay(500);
        robot.waitForIdle();
        Rectangle r = comp.getBounds();
        System.out.println("Component bounds = " + r);
        Color c = robot.getPixelColor((int)r.getWidth()-100, (int)r.getHeight()-100);

        SwingUtilities.invokeAndWait(() -> frame.dispose());

        if (!c.equals(color)) {
            throw new RuntimeException("Color was " + c + " expected " + color);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillRect(0, 0, getSize().width, getSize().height);
    };
}
