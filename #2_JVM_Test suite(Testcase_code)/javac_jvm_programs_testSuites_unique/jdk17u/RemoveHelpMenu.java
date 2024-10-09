import java.awt.Frame;
import java.awt.Menu;
import java.awt.MenuBar;

public final class RemoveHelpMenu {

    public static void main(final String[] args) {
        final Frame frame = new Frame("RemoveHelpMenu Test");
        try {
            frame.pack();
            test1(getMenuBar(frame));
            test2(getMenuBar(frame));
            test3(getMenuBar(frame));
            test4(getMenuBar(frame));
        } finally {
            frame.dispose();
        }
        test1(getMenuBar(frame));
        test2(getMenuBar(frame));
        test3(getMenuBar(frame));
        test4(getMenuBar(frame));
    }

    private static MenuBar getMenuBar(final Frame frame) {
        final MenuBar menuBar = new MenuBar();
        frame.setMenuBar(menuBar);
        return menuBar;
    }

    private static void checkHelpMenu(final Menu menu, final boolean expected) {
        final boolean actual = menu.toString().contains("isHelpMenu=true");
        if (actual != expected) {
            throw new RuntimeException("Incorrect menu type");
        }
    }

    private static void checkMenuCount(final MenuBar bar, final int expected) {
        final int actual = bar.getMenuCount();
        if (actual != expected) {
            throw new RuntimeException("Incorrect menus count");
        }
    }

    private static void checkCurrentMenu(final MenuBar bar, final Menu menu) {
        if (bar.getHelpMenu() != menu) {
            throw new RuntimeException("Wrong HelpMenu");
        }
    }

    private static void test1(final MenuBar menuBar) {
        checkCurrentMenu(menuBar, null);
        checkMenuCount(menuBar, 0);
    }

    private static void test2(final MenuBar menuBar) {
        final Menu helpMenu = new Menu("Help Menu");
        menuBar.setHelpMenu(helpMenu);
        checkCurrentMenu(menuBar, helpMenu);
        checkMenuCount(menuBar, 1);
        checkHelpMenu(helpMenu, true);
        menuBar.remove(helpMenu);
        checkCurrentMenu(menuBar, null);
        checkMenuCount(menuBar, 0);
        checkHelpMenu(helpMenu, false);
    }

    private static void test3(final MenuBar menuBar) {
        final Menu helpMenu1 = new Menu("Help Menu1");
        final Menu helpMenu2 = new Menu("Help Menu2");
        menuBar.setHelpMenu(helpMenu1);
        checkCurrentMenu(menuBar, helpMenu1);
        checkMenuCount(menuBar, 1);
        checkHelpMenu(helpMenu1, true);
        checkHelpMenu(helpMenu2, false);
        menuBar.setHelpMenu(helpMenu2);
        checkCurrentMenu(menuBar, helpMenu2);
        checkMenuCount(menuBar, 1);
        checkHelpMenu(helpMenu1, false);
        checkHelpMenu(helpMenu2, true);
        menuBar.remove(helpMenu2);
        checkCurrentMenu(menuBar, null);
        checkMenuCount(menuBar, 0);
        checkHelpMenu(helpMenu1, false);
        checkHelpMenu(helpMenu2, false);
    }

    private static void test4(final MenuBar menuBar) {
        final Menu helpMenu = new Menu("Help Menu");
        menuBar.setHelpMenu(helpMenu);
        checkCurrentMenu(menuBar, helpMenu);
        checkMenuCount(menuBar, 1);
        checkHelpMenu(helpMenu, true);
        menuBar.setHelpMenu(null);
        checkCurrentMenu(menuBar, null);
        checkMenuCount(menuBar, 0);
        checkHelpMenu(helpMenu, false);
    }
}
