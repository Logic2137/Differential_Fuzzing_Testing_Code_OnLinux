



import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Choice;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class ChoiceFromMaximizedFrame
{
    static public void main(String args[]) throws Exception
    {
        Frame f = new Frame("ChoiceTest");
        try {
            Panel p =new Panel(new BorderLayout());
            Choice choice = new Choice();
            choice.addItem("aaa");
            choice.addItem("bbb");
            p.add("North",choice);
            p.add("Center",new Canvas());
            f.add(p);

            GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice().getDefaultConfiguration();
            Rectangle bounds = gc.getBounds();
            Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
            int x = bounds.x + insets.left;
            int y = bounds.y + insets.top;
            int width = bounds.width - insets.left - insets.right;
            int height = bounds.height - insets.top - insets.bottom;
            Rectangle rect = new Rectangle(x, y, width, height);
            f.pack();
            f.setBounds(rect);
            f.setVisible(true);

            Robot robot = new Robot();
            robot.waitForIdle();
            robot.keyPress(KeyEvent.VK_SPACE);
            robot.keyRelease(KeyEvent.VK_SPACE);
            robot.delay(1000);
        } finally {
            f.dispose();
        }
    }
}
