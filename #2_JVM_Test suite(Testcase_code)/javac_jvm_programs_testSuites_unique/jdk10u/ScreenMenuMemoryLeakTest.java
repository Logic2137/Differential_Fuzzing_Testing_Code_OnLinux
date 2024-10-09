



import java.awt.EventQueue;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.WindowConstants;

public class ScreenMenuMemoryLeakTest {

    private static byte[] sBytes;

    private static WeakReference<JMenuItem> sMenuItem;
    private static JFrame sFrame;
    private static JMenu sMenu;

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                System.setProperty("apple.laf.useScreenMenuBar", "true");
                showUI();
            }
        });

        EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                removeMenuItemFromMenu();
            }
        });
        System.gc();
        System.runFinalization();
        Thread.sleep(1000);
        JMenuItem menuItem = sMenuItem.get();
        EventQueue.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                sFrame.dispose();
            }
        });
        if (menuItem != null) {
            throw new RuntimeException("The menu item should have been GC-ed");
        }
    }

    private static void showUI() {
        sFrame = new JFrame();
        sFrame.add(new JLabel("Some dummy content"));

        JMenuBar menuBar = new JMenuBar();

        sMenu = new JMenu("Menu");
        JMenuItem item = new JMenuItem("Item");
        sMenu.add(item);

        sMenuItem = new WeakReference<>(item);

        menuBar.add(sMenu);

        sFrame.setJMenuBar(menuBar);

        sFrame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        sFrame.pack();
        sFrame.setVisible(true);
    }

    private static void removeMenuItemFromMenu() {
        JMenuItem menuItem = sMenuItem.get();
        Objects.requireNonNull(menuItem, "The menu item should still be available at this point");
        sMenu.remove(menuItem);
    }
}
