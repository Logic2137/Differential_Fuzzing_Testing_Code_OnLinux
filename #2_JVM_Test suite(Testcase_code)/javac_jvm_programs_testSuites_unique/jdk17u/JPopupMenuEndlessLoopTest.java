import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;

public class JPopupMenuEndlessLoopTest {

    public static void main(String[] args) throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JPopupMenu popup = new JPopupMenu("Popup Menu");
            JMenu menu = new JMenu("Menu");
            menu.add(new JMenuItem("Menu Item"));
            popup.add(menu);
            menu.doClick();
            MenuElement[] elems = MenuSelectionManager.defaultManager().getSelectedPath();
            if (elems == null || elems.length == 0) {
                throw new RuntimeException("Empty Selection");
            }
            if (elems[0] != popup || elems[1] != menu) {
                throw new RuntimeException("Necessary menus are not selected!");
            }
        });
    }
}
