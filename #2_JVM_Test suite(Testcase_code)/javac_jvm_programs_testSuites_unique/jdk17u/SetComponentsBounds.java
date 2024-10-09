import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Label;
import java.awt.List;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Window;

public final class SetComponentsBounds {

    private static final int X = 111;

    private static final int Y = 222;

    private static final int WIDTH = 321;

    private static final int HEIGHT = 123;

    public static void main(String[] args) throws Exception {
        var ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        for (GraphicsDevice gd : ge.getScreenDevices()) {
            test(gd.getDefaultConfiguration(), true);
            test(gd.getDefaultConfiguration(), false);
        }
    }

    private static void test(GraphicsConfiguration gc, boolean visible) throws Exception {
        Rectangle screen = gc.getBounds();
        Window frame = new Frame();
        try {
            frame.setLayout(null);
            frame.setBounds(screen.x + 100, screen.y + 100, 500, 500);
            frame.add(new Button());
            frame.add(new Canvas());
            frame.add(new Checkbox());
            frame.add(new Choice());
            frame.add(new Label());
            frame.add(new List());
            frame.add(new Scrollbar());
            frame.add(new ScrollPane());
            frame.add(new TextArea());
            frame.add(new TextField());
            for (Component comp : frame.getComponents()) {
                comp.setBounds(X, Y, WIDTH, HEIGHT);
            }
            if (visible) {
                frame.setVisible(true);
            } else {
                frame.pack();
            }
            Robot robot = new Robot();
            robot.waitForIdle();
            checkGC(gc, frame);
            for (Component comp : frame.getComponents()) {
                Rectangle bounds = comp.getBounds();
                if (bounds.x != X || bounds.y != Y || bounds.width != WIDTH) {
                    System.err.println("Screen bounds:" + screen);
                    System.err.println("Component:" + comp);
                    throw new RuntimeException("Wrong bounds:" + bounds);
                }
                if (bounds.height > HEIGHT) {
                    throw new RuntimeException("Wrong height:" + bounds.height);
                }
                checkGC(gc, comp);
            }
        } finally {
            frame.dispose();
        }
    }

    private static void checkGC(GraphicsConfiguration gc, Component comp) {
        GraphicsConfiguration compGC = comp.getGraphicsConfiguration();
        if (compGC != gc) {
            System.err.println("Expected GC:" + gc);
            System.err.println("Actual GC:" + compGC);
            throw new RuntimeException();
        }
    }
}
