



import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Robot;
import javax.swing.SwingUtilities;

public class AlphaColorTest extends Component {

    private Color color;

    public AlphaColorTest(Color c) {
       this.color = c;
    }

    public void paint(Graphics g) {
        g.setColor(color);
        g.fillRect(0, 0, getSize().width, getSize().height);
    }

    public Dimension getPreferredSize() {
        return getSize();
    }

    public Dimension getSize() {
        return new Dimension(200, 200);
    }

    public static void main(String args[]) throws Exception {
        SwingUtilities.invokeAndWait(() -> createAndShowGUI());
        Robot robot = new Robot();
        robot.delay(5000);
        robot.waitForIdle();
        Color c = robot.getPixelColor(frame.getX() + 100, frame.getY() + 100);
        int red = c.getRed();
        frame.dispose();
        
        if (red < 122 || red > 132) {
            throw new RuntimeException("Color is not as expected. Got " + c);
        }
     }

    static Frame frame;
    private static void createAndShowGUI() {
        frame = new Frame("Alpha Color Test") {
            @Override
            public void paint(Graphics g) {
                g.setColor(Color.black);
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paint(g);
            }
        };
        Color color = new Color(255, 255, 255, 127);
        frame.add("Center", new AlphaColorTest(color));
        frame.setUndecorated(true);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
