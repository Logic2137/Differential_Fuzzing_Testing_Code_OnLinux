



import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

public class bug5012888 extends JFrame {
    JSpinner spinner1, spinner2;
    public bug5012888() {
      spinner1 = new JSpinner(new SpinnerNumberModel(0, -1000, 1000, 1));
      spinner2 = new JSpinner(new SpinnerNumberModel(1, -1000, 1000, 1));
      Container pane = getContentPane();
      pane.setLayout(new BorderLayout());
      pane.add(spinner1, BorderLayout.NORTH);
      pane.add(spinner2, BorderLayout.SOUTH);
    }
    public void doTest() throws Exception {
        Robot robot = new Robot();
        robot.waitForIdle();
        Point p = spinner2.getLocationOnScreen();
        Rectangle rect = spinner2.getBounds();
        robot.mouseMove(p.x+rect.width-5, p.y+5);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        Thread.sleep(1000);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        if ( ((Integer) spinner2.getValue()).intValue() == 1 ) {
            throw new Error("Spinner value should be more than 1");
        }
    }
    public static void main(String[] argv) throws Exception {
        bug5012888 b = new bug5012888();
        b.setBounds(0, 0, 100, 100);
        b.setVisible(true);
        b.doTest();
    }
}
