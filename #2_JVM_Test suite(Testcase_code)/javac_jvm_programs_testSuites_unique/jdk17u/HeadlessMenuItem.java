import java.awt.*;
import java.awt.event.KeyEvent;

public class HeadlessMenuItem {

    public static void main(String[] args) {
        MenuItem mi;
        boolean exceptions = false;
        try {
            mi = new MenuItem();
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
        exceptions = false;
        try {
            mi = new MenuItem("Choose me");
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
        exceptions = false;
        try {
            MenuShortcut ms = new MenuShortcut(KeyEvent.VK_A);
            mi = new MenuItem("Choose me", ms);
        } catch (HeadlessException e) {
            exceptions = true;
        }
        if (!exceptions)
            throw new RuntimeException("HeadlessException did not occur when expected");
    }
}
