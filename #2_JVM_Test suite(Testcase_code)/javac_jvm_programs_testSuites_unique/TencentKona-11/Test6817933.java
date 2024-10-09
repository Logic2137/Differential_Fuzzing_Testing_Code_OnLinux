



import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import sun.awt.SunToolkit;
import sun.swing.WindowsPlacesBar;

public class Test6817933 {

    private static final String STYLE = "BODY {background:red}";
    private static final Color COLOR = Color.RED;
    private static JFileChooser chooser;

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return; 
        }
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                StyleSheet css = new StyleSheet();
                css.addRule(STYLE);

                HTMLEditorKit kit = new HTMLEditorKit();
                kit.setStyleSheet(css);

                JFrame frame = new JFrame(STYLE);
                frame.add(chooser = new JFileChooser());
                frame.setSize(640, 480);
                frame.setVisible(true);
            }
        });

        SunToolkit toolkit = (SunToolkit) Toolkit.getDefaultToolkit();
        toolkit.realSync(500);

        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                try {
                    JToggleButton button = get(JToggleButton.class,
                                           get(WindowsPlacesBar.class, chooser));

                    int width = button.getWidth();
                    int height = button.getHeight() / 3;
                    Point point = new Point(0, height * 2);
                    SwingUtilities.convertPointToScreen(point, button);
                    width += point.x;
                    height += point.y;

                    int count = 0;
                    Robot robot = new Robot();
                    for (int x = point.x; x < width; x++) {
                        for (int y = point.y; y < height; y++) {
                            count += COLOR.equals(robot.getPixelColor(x, y)) ? -2 : 1;
                        }
                    }
                    if (count < 0) {
                        throw new Exception("TEST ERROR: a lot of red pixels");
                    }
                }
                catch (Exception exception) {
                    throw new Error(exception);
                }
                finally {
                    SwingUtilities.getWindowAncestor(chooser).dispose();
                }
            }
        });
    }

    private static <T> T get(Class<? extends T> type, Container container) {
        Component component = container.getComponent(0);
        if (!type.isAssignableFrom(component.getClass())) {
            throw new IllegalStateException("expected " + type + "; expected " + component.getClass());
        }
        return (T) component;
    }
}
