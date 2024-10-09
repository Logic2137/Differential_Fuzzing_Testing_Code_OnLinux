import java.awt.*;

public class HeadlessFrame {

    public static void main(String[] args) {
        Frame f;
        boolean exceptions = false;
        try {
            f = new Frame();
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
        exceptions = false;
        try {
            f = new Frame("Frame me peculiar");
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
    }
}
