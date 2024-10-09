



import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.lang.reflect.Method;


public class DefaultMenuBarTest {
    static KeyStroke ks = KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.META_MASK);

    static volatile int listenerCallCounter = 0;
    public static void main(String[] args) throws Exception {
        if (!System.getProperty("os.name").contains("OS X")) {
            System.out.println("This test is for MacOS only. Automatically passed on other platforms.");
            return;
        }

        System.setProperty("apple.laf.useScreenMenuBar", "true");
        SwingUtilities.invokeAndWait(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });

        Robot robot = new Robot();
        robot.setAutoDelay(100);

        robot.keyPress(KeyEvent.VK_META);
        robot.keyPress(ks.getKeyCode());
        robot.keyRelease(ks.getKeyCode());
        robot.keyRelease(KeyEvent.VK_META);

        robot.waitForIdle();

        if (listenerCallCounter != 1) {
            throw new Exception("Test failed: ActionListener either wasn't called or was called more than once");
        }
    }

    private static void createAndShowGUI() {
        JMenu menu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("Open");

        newItem.setAccelerator(ks);
        newItem.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent e) {
                    listenerCallCounter++;
                }
            }
        );
        menu.add(newItem);

        JMenuBar defaultMenu = new JMenuBar();
        defaultMenu.add(menu);

        
        try {
            Class appClass = Class.forName("com.apple.eawt.Application");
            if (appClass != null) {
                Method method = appClass.getMethod("getApplication");
                if (method != null) {
                    Object app = method.invoke(null, new Object[]{});
                    if (app != null) {
                        method = appClass.getMethod("setDefaultMenuBar", new Class[]{JMenuBar.class});
                        if (method != null) {
                            method.invoke(app, new Object[]{defaultMenu});
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
