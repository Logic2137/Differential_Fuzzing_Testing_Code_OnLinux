

import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.UIManager;



public class HiDPIRobotMouseClick {

    private static volatile int mouseX;
    private static volatile int mouseY;

    public static void main(String[] args) throws Exception {

        try {
            UIManager.setLookAndFeel(
                    "com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception e) {
            return;
        }

        Frame frame = new Frame();
        frame.setBounds(30, 20, 400, 300);
        frame.setUndecorated(true);

        frame.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                mouseX = e.getXOnScreen();
                mouseY = e.getYOnScreen();
            }
        });

        frame.setVisible(true);

        Robot robot = new Robot();
        robot.waitForIdle();
        Thread.sleep(200);

        Rectangle rect = frame.getBounds();
        rect.setLocation(frame.getLocationOnScreen());

        int x = (int) rect.getCenterX();
        int y = (int) rect.getCenterY();

        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();

        if (x != mouseX || y != mouseY) {
            throw new RuntimeException("Wrong mouse click point!");
        }
    }
}
