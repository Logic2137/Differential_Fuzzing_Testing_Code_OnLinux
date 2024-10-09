import java.awt.AWTException;
import java.awt.Button;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import javax.swing.JButton;

public final class CreateImage {

    public static void main(final String[] args) throws Exception {
        EventQueue.invokeAndWait(CreateImage::test);
    }

    private static void test() {
        final JButton jbutton1 = new JButton();
        final JButton jbutton2 = new JButton();
        if (GraphicsEnvironment.isHeadless()) {
            checkCreateImage(jbutton1, true);
            checkCreateImage(jbutton2, true);
            return;
        }
        final Frame frame = new Frame();
        final Button button1 = new Button();
        final Button button2 = new Button();
        try {
            checkCreateImage(frame, true);
            checkCreateImage(button1, true);
            checkCreateImage(button2, true);
            checkCreateImage(jbutton1, true);
            checkCreateImage(jbutton2, true);
            frame.add(button1);
            frame.add(jbutton1);
            checkCreateImage(button1, true);
            checkCreateImage(jbutton1, true);
            frame.pack();
            checkCreateImage(frame, false);
            checkCreateImage(button1, false);
            checkCreateImage(jbutton1, false);
            frame.add(button2);
            frame.add(jbutton2);
            checkCreateImage(button2, false);
            checkCreateImage(jbutton2, false);
        } finally {
            frame.dispose();
        }
        checkCreateImage(frame, true);
        checkCreateImage(button1, true);
        checkCreateImage(button2, true);
        checkCreateImage(jbutton1, true);
        checkCreateImage(jbutton2, true);
    }

    private static void checkCreateImage(final Component comp, final boolean isNull) {
        if ((comp.createImage(10, 10) != null) == isNull) {
            throw new RuntimeException("Image is wrong");
        }
        if ((comp.createVolatileImage(10, 10) != null) == isNull) {
            throw new RuntimeException("Image is wrong");
        }
        try {
            if ((comp.createVolatileImage(10, 10, null) != null) == isNull) {
                throw new RuntimeException("Image is wrong");
            }
        } catch (final AWTException ignored) {
        }
    }
}
