



import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;

public class TestNoScreenMenuBar
{
    static TestNoScreenMenuBar theTest;
    private Robot robot;
    private boolean isApplicationOpened;
    private boolean isActionPerformed;

    public TestNoScreenMenuBar(String[] args)
    {
        try {
            robot = new Robot();
            robot.setAutoDelay(50);
        } catch (AWTException ex) {
            throw new RuntimeException(ex);
        }

        if (!(args.length > 0 && args[0].equals("baseline"))) {
            
            openOtherApplication();
            robot.delay(500);
        }

        
        Desktop desktop = Desktop.getDesktop();
        desktop.setDefaultMenuBar(createMenuBar());

        robot.delay(500);
        desktop.requestForeground(true);
        robot.delay(500);
    }

    JMenuBar createMenuBar()
    {
        JMenuBar mb = new JMenuBar();
        
        JMenu menu = new JMenu("TestTestTestTestTestTestTestTestTestTest");
        mb.add(menu);
        JMenuItem item = new JMenuItem("TestTestTestTestTestTestTestTestTestTest");
        item.addActionListener(ev -> {
            isActionPerformed = true;
        });
        menu.add(item);
        return mb;
    }

    void dispose()
    {
        closeOtherApplication();
        Desktop.getDesktop().setDefaultMenuBar(null);
    }

    private void performMenuItemTest()
    {
        
        
        
        

        int menuBarX = 250;
        int menuBarY = 11;
        int menuItemX = menuBarX;
        int menuItemY = 34;

        robot.mouseMove(menuBarX, menuBarY);
        robot.delay(100);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(100);
        robot.mouseMove(menuItemX, menuItemY);
        robot.delay(100);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();

        waitForAction();
    }

    private synchronized void waitForAction()
    {
        try {
            for (int i = 0; i < 10; i++) {
                if (isActionPerformed) {
                    return;
                }
                wait(100);
            }
        } catch (InterruptedException ex) {
        }
        throw new RuntimeException("Test failed: menu item action was not performed");
    }

    private void openOtherApplication() {
        String[] cmd = { "/usr/bin/open", "/Applications/System Preferences.app" };
        execute(cmd);
        isApplicationOpened = true;
    }

    private void closeOtherApplication() {
        if (isApplicationOpened) {
            String[] cmd = { "/usr/bin/osascript", "-e", "tell application \"System Preferences\" to close window 1" };
            execute(cmd);
        }
    }

    private void execute(String[] cmd) {
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            p.waitFor();
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException("Unable to execute command");
        }
    }

    private static void runSwing(Runnable r)
    {
        try {
            SwingUtilities.invokeAndWait(r);
        } catch (InterruptedException e) {
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args)
    {
        if (!System.getProperty("os.name").contains("OS X")) {
            System.out.println("This test is for MacOS only. Automatically passed on other platforms.");
            return;
        }

        System.setProperty("apple.laf.useScreenMenuBar", "true");
        try {
            runSwing(() -> theTest = new TestNoScreenMenuBar(args));
            theTest.performMenuItemTest();
        } finally {
            if (theTest != null) {
                runSwing(() -> theTest.dispose());
            }
        }
    }
}
