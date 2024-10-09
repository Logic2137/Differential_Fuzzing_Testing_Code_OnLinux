



import java.awt.*;

public class MultiWindowAppTest {

    public static void main(String[] args) throws Exception {
        Window win1 = new Frame();
        Window win2 = new Dialog((Frame) null);

        int delay = 300;

        win1.setBounds(100, 100, 200, 200);
        win1.setBackground(Color.RED);
        win1.setVisible(true);

        Robot robot = new Robot();
        robot.delay(delay);
        robot.waitForIdle();

        win2.setBounds(win1.getBounds());
        win2.setVisible(true);

        robot.delay(delay);
        robot.waitForIdle();

        win1.toFront();
        robot.delay(delay);
        robot.waitForIdle();

        Point point = win1.getLocationOnScreen();
        Color color = robot.getPixelColor(point.x + 100, point.y + 100);

        if(!color.equals(Color.RED)) {
            win1.dispose();
            win2.dispose();
            throw new RuntimeException("Window was not sent to front.");
        }

        win1.toBack();
        robot.delay(delay);
        robot.waitForIdle();

        color = robot.getPixelColor(point.x + 100, point.y + 100);

        win1.dispose();
        win2.dispose();

        if(color.equals(Color.RED)) {
            throw new RuntimeException("Window was not sent to back.");
        }

        System.out.println("ok");
    }
}
