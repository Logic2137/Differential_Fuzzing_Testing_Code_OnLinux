import javax.swing.*;
import javax.swing.plaf.SliderUI;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.InputEvent;
import java.lang.reflect.Field;

public class bug6848475 {

    private static JFrame frame;

    private static JSlider slider;

    private static Robot robot;

    private static int thumbRectX;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        robot.setAutoDelay(100);
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                frame = new JFrame();
                DefaultBoundedRangeModel sliderModel = new DefaultBoundedRangeModel() {

                    public void setValue(int n) {
                    }
                };
                slider = new JSlider(sliderModel);
                frame.getContentPane().add(slider);
                frame.pack();
                frame.setVisible(true);
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                Point p = slider.getLocationOnScreen();
                robot.mouseMove(p.x, p.y);
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                thumbRectX = getThumbRectField().x;
                Point p = slider.getLocationOnScreen();
                robot.mouseMove(p.x, p.y);
                robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseMove(p.x + 20, p.y);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            }
        });
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(new Runnable() {

            public void run() {
                Rectangle newThumbRect = getThumbRectField();
                if (newThumbRect.x != thumbRectX) {
                    throw new RuntimeException("Test failed: the thumb was moved");
                }
                frame.dispose();
            }
        });
    }

    private static Rectangle getThumbRectField() {
        try {
            SliderUI ui = slider.getUI();
            Field field = BasicSliderUI.class.getDeclaredField("thumbRect");
            field.setAccessible(true);
            return (Rectangle) field.get(ui);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
