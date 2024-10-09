import java.awt.MenuShortcut;
import java.awt.event.KeyEvent;

public class HeadlessMenuShortcut {

    public static void main(String[] args) {
        MenuShortcut ms;
        ms = new MenuShortcut(KeyEvent.VK_A);
        ms = new MenuShortcut(KeyEvent.VK_A, true);
        ms = new MenuShortcut(KeyEvent.VK_A, false);
    }
}
