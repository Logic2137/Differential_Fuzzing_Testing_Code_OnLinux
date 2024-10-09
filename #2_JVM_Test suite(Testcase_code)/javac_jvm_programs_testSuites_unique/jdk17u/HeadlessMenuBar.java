import java.awt.*;

public class HeadlessMenuBar {

    public static void main(String[] args) {
        boolean exceptions = false;
        try {
            MenuBar m = new MenuBar();
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
    }
}
