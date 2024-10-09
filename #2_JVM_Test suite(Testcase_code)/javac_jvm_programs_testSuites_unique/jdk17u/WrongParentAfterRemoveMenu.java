import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.PopupMenu;
import java.awt.Window;

public final class WrongParentAfterRemoveMenu {

    public static void main(final String[] args) {
        testMenuBar();
        testComponent();
        testFrame();
    }

    private static void testFrame() {
        Frame frame = new Frame();
        try {
            frame.pack();
            PopupMenu popupMenu = new PopupMenu();
            frame.add(popupMenu);
            checkParent(popupMenu, frame);
            frame.remove(popupMenu);
            checkParent(popupMenu, null);
        } finally {
            frame.dispose();
        }
        frame = new Frame();
        PopupMenu popupMenu = new PopupMenu();
        frame.add(popupMenu);
        checkParent(popupMenu, frame);
        frame.remove(popupMenu);
        checkParent(popupMenu, null);
    }

    private static void testComponent() {
        Window w = new Window(null);
        try {
            w.pack();
            PopupMenu popupMenu = new PopupMenu();
            w.add(popupMenu);
            checkParent(popupMenu, w);
            w.remove(popupMenu);
            checkParent(popupMenu, null);
        } finally {
            w.dispose();
        }
        w = new Window(null);
        PopupMenu popupMenu = new PopupMenu();
        w.add(popupMenu);
        checkParent(popupMenu, w);
        w.remove(popupMenu);
        checkParent(popupMenu, null);
    }

    private static void testMenuBar() {
        MenuBar mb = new MenuBar();
        try {
            mb.addNotify();
            Menu m1 = new Menu();
            Menu m2 = new Menu();
            m1.add(m2);
            mb.add(m1);
            checkParent(m1, mb);
            checkParent(m2, m1);
            m1.remove(m2);
            checkParent(m2, null);
            mb.remove(m1);
            checkParent(m1, null);
        } finally {
            mb.removeNotify();
        }
        mb = new MenuBar();
        Menu m1 = new Menu();
        Menu m2 = new Menu();
        m1.add(m2);
        mb.add(m1);
        checkParent(m1, mb);
        checkParent(m2, m1);
        m1.remove(m2);
        checkParent(m2, null);
        mb.remove(m1);
        checkParent(m1, null);
    }

    private static void checkParent(final Menu menu, final Object parent) {
        if (menu.getParent() != parent) {
            System.err.println("Expected: " + parent);
            System.err.println("Actual: " + menu.getParent());
            throw new RuntimeException("Wrong parent");
        }
    }
}
