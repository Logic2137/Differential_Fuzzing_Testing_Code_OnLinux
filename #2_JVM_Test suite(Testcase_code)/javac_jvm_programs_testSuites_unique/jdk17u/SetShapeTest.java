import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.Robot;
import javax.swing.SwingUtilities;

public class SetShapeTest {

    private static Window window;

    private static Frame background;

    public static void main(String[] args) throws Exception {
        createUI();
        Robot robot = new Robot();
        robot.waitForIdle();
        Rectangle rect = window.getBounds();
        rect.x += rect.width - 10;
        rect.y += rect.height - 10;
        robot.delay(1000);
        Color c = robot.getPixelColor(rect.x, rect.y);
        try {
            if (!c.equals(Color.RED)) {
                throw new RuntimeException("Test Failed");
            }
        } finally {
            SwingUtilities.invokeAndWait(new Runnable() {

                @Override
                public void run() {
                    background.dispose();
                    window.dispose();
                }
            });
        }
    }

    private static void createUI() throws Exception {
        SwingUtilities.invokeAndWait(new Runnable() {

            @Override
            public void run() {
                background = new Frame();
                background.setUndecorated(true);
                background.setBackground(Color.blue);
                background.setSize(300, 300);
                background.setLocation(100, 100);
                background.setVisible(true);
                window = new Window(background);
                window.setBackground(Color.red);
                window.add(new Panel(), BorderLayout.CENTER);
                window.setLocation(200, 200);
                window.setSize(100, 100);
                Area a = new Area();
                a.add(new Area(new Rectangle2D.Double(0, 0, 100, 100)));
                window.setShape(a);
                window.setVisible(true);
                window.toFront();
            }
        });
    }
}
