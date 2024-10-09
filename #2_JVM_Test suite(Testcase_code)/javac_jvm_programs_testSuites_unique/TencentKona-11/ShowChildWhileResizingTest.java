



import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

public class ShowChildWhileResizingTest {

    private static Window dialog;
    private static Timer timer;
    private static Point point;

    public static void main(String[] args) throws Exception {
        dialog = new Frame();
        dialog.add(new JPanel());
        dialog.setVisible(true);
        dialog.setBounds(100, 100, 200, 200);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                final Window dependentWindow = new JWindow(dialog);
                JPanel panel = new JPanel();
                panel.add(new JButton("button"));
                dependentWindow.add(panel);
                dependentWindow.setVisible(true);
                dependentWindow.setBounds(0, 0, 50, 50);
                timer = new Timer(100, new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dependentWindow
                                .setVisible(!dependentWindow.isVisible());
                    }
                });
                timer.start();
            }

        });

        Robot robot = new Robot();
        robot.setAutoDelay(5);
        robot.delay(300);
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                point = dialog.getLocationOnScreen();
            }
        });
        robot.mouseMove(point.x + 200 - dialog.getInsets().right/2,
                point.y + 200 - dialog.getInsets().bottom/2);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        for(int i = 0; i < 100; i++) {
            robot.mouseMove(point.x + 200 + i, point.y + 200 + i);
        }
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        timer.stop();
        dialog.dispose();
        System.out.println("ok");
    }
}
