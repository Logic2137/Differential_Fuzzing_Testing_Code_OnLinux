import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Objects;
import javax.swing.*;

public class TestMainKeyWindow {

    static TestMainKeyWindow theTest;

    KeyStroke commandT = KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.META_DOWN_MASK);

    int nextX = 130;

    private final MyFrame frame1;

    private final MyFrame frame2;

    private final Object COLOR_PANEL = "Color Panel";

    private final Object NATIVE_WINDOW = "Native Window";

    private Rectangle colorPanelBounds = new Rectangle(130, 300, 225, 400);

    private Rectangle nativeWindowBounds = new Rectangle(130, 200, 200, 100);

    private Robot robot;

    private int actionCounter;

    private Object actionTarget;

    private int failureCount;

    private boolean isApplicationOpened;

    public TestMainKeyWindow() {
        System.loadLibrary("testMainKeyWindow");
        JMenuBar defaultMenuBar = createMenuBar("Application", true);
        Desktop.getDesktop().setDefaultMenuBar(defaultMenuBar);
        setup();
        frame1 = new MyFrame("Frame 1");
        frame2 = new MyFrame("Frame 2");
        frame1.setVisible(true);
        frame2.setVisible(true);
        try {
            robot = new Robot();
            robot.setAutoDelay(50);
        } catch (AWTException ex) {
            throw new RuntimeException(ex);
        }
    }

    class MyFrame extends JFrame {

        public MyFrame(String title) throws HeadlessException {
            super(title);
            JMenuBar mainMenuBar = createMenuBar(title, true);
            setJMenuBar(mainMenuBar);
            setBounds(nextX, 60, 200, 90);
            nextX += 250;
            JComponent contentPane = new JPanel();
            setContentPane(contentPane);
            contentPane.setLayout(new FlowLayout());
            contentPane.add(new JCheckBox("foo", true));
            InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
            inputMap.put(commandT, "test");
            ActionMap actionMap = contentPane.getActionMap();
            actionMap.put("test", new MyAction(title + " Key"));
        }
    }

    private void runTest() {
        failureCount = 0;
        robot.waitForIdle();
        performTest(frame1, false);
        performTest(frame1, true);
        performTest(frame2, false);
        performTest(frame2, true);
        performTest(NATIVE_WINDOW, false);
        performTest(NATIVE_WINDOW, true);
        performTest(COLOR_PANEL, false);
        if (failureCount > 0) {
            throw new RuntimeException("Test failed: " + failureCount + " failure(s)");
        }
    }

    private void performTest(Object windowIdentification, boolean selectColorPanel) {
        setupWindows(windowIdentification, selectColorPanel);
        performMenuShortcutTest(windowIdentification, selectColorPanel);
        performMenuItemTest(windowIdentification, selectColorPanel);
        openOtherApplication();
        activateApplication();
        robot.delay(1000);
        performMenuShortcutTest(windowIdentification, selectColorPanel);
        performMenuItemTest(windowIdentification, selectColorPanel);
    }

    private void openOtherApplication() {
        try {
            String[] cmd = { "/usr/bin/open", "/Applications/System Preferences.app" };
            Runtime.getRuntime().exec(cmd);
            if (!isApplicationOpened) {
                String[] cmd2 = { "/usr/bin/osascript", "-e", "tell application \"System Preferences\" to set bounds of window 1 to {400, 180, 1068, 821}" };
                Runtime.getRuntime().exec(cmd2);
            }
            isApplicationOpened = true;
        } catch (IOException ex) {
            throw new RuntimeException("Unable to deactivate test application");
        }
        robot.delay(1000);
    }

    private void performMenuShortcutTest(Object windowIdentification, boolean selectColorPanel) {
        int currentActionCount = actionCounter;
        robot.keyPress(KeyEvent.VK_META);
        robot.keyPress(KeyEvent.VK_T);
        robot.keyRelease(KeyEvent.VK_T);
        robot.keyRelease(KeyEvent.VK_META);
        robot.waitForIdle();
        Object target = waitForAction(currentActionCount + 1);
        boolean isDirectKey = windowIdentification instanceof Window && !selectColorPanel;
        Object expectedTarget = getExpectedTarget(windowIdentification, isDirectKey);
        if (!Objects.equals(target, expectedTarget)) {
            failureCount++;
            String configuration = getConfigurationName(windowIdentification, selectColorPanel);
            System.err.println("***** Menu shortcut test failed for " + configuration + ". Expected: " + expectedTarget + ", Actual: " + target);
        }
    }

    private void performMenuItemTest(Object windowIdentification, boolean selectColorPanel) {
        int currentActionCount = actionCounter;
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
        Object target = waitForAction(currentActionCount + 1);
        Object expectedTarget = getExpectedTarget(windowIdentification, false);
        if (!Objects.equals(target, expectedTarget)) {
            failureCount++;
            String configuration = getConfigurationName(windowIdentification, selectColorPanel);
            System.err.println("***** Menu item test failed for " + configuration + ". Expected: " + expectedTarget + ", Actual: " + target);
        }
    }

    private String getConfigurationName(Object windowIdentification, boolean selectColorPanel) {
        String name = "Unknown";
        if (windowIdentification instanceof Window) {
            Window w = (Window) windowIdentification;
            name = getWindowTitle(w);
        } else if (windowIdentification == NATIVE_WINDOW) {
            name = "Native Window";
        } else if (windowIdentification == COLOR_PANEL) {
            name = "Color Panel";
        }
        if (selectColorPanel) {
            return name + " with color panel";
        } else {
            return name;
        }
    }

    private Object getExpectedTarget(Object windowIdentification, boolean isDirectKey) {
        if (windowIdentification instanceof Window) {
            Window w = (Window) windowIdentification;
            String title = getWindowTitle(w);
            if (isDirectKey) {
                title = title + " Key";
            }
            return title;
        }
        return "Application";
    }

    private String getWindowTitle(Window w) {
        if (w instanceof Frame) {
            Frame f = (Frame) w;
            return f.getTitle();
        }
        if (w instanceof Dialog) {
            Dialog d = (Dialog) w;
            return d.getTitle();
        }
        throw new IllegalStateException();
    }

    private synchronized void registerAction(Object target) {
        actionCounter++;
        actionTarget = target;
    }

    private synchronized Object waitForAction(int count) {
        try {
            for (int i = 0; i < 10; i++) {
                if (actionCounter == count) {
                    return actionTarget;
                }
                if (actionCounter > count) {
                    throw new IllegalStateException();
                }
                wait(100);
            }
        } catch (InterruptedException ex) {
        }
        return "No Action";
    }

    private void setupWindows(Object windowIdentification, boolean selectColorPanel) {
        clickOnWindowTitleBar(windowIdentification);
        if (selectColorPanel) {
            clickOnWindowTitleBar(COLOR_PANEL);
        }
    }

    private void clickOnWindowTitleBar(Object windowIdentification) {
        Rectangle bounds = getWindowBounds(windowIdentification);
        int x = bounds.x + 70;
        int y = bounds.y + 12;
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.waitForIdle();
    }

    private Rectangle getWindowBounds(Object windowIdentification) {
        if (windowIdentification instanceof Window) {
            Window w = (Window) windowIdentification;
            return w.getBounds();
        }
        if (windowIdentification == COLOR_PANEL) {
            return colorPanelBounds;
        }
        if (windowIdentification == NATIVE_WINDOW) {
            return nativeWindowBounds;
        }
        throw new IllegalArgumentException();
    }

    JMenuBar createMenuBar(String text, boolean isEnabled) {
        JMenuBar mb = new JMenuBar();
        JMenu menu = new JMenu("TestTestTestTestTestTestTestTestTestTest");
        mb.add(menu);
        JMenuItem item = new JMenuItem("TestTestTestTestTestTestTestTestTestTest");
        item.setAccelerator(commandT);
        item.setEnabled(isEnabled);
        item.addActionListener(ev -> {
            registerAction(text);
        });
        menu.add(item);
        return mb;
    }

    void dispose() {
        frame1.setVisible(false);
        frame2.setVisible(false);
        frame1.dispose();
        frame2.dispose();
        takedown();
        Desktop.getDesktop().setDefaultMenuBar(null);
        if (isApplicationOpened) {
            try {
                String[] cmd = { "/usr/bin/osascript", "-e", "tell application \"System Preferences\" to close window 1" };
                Process p = Runtime.getRuntime().exec(cmd);
                p.waitFor();
            } catch (IOException | InterruptedException ex) {
            }
        }
    }

    class MyAction extends AbstractAction {

        String text;

        public MyAction(String text) {
            super("Test");
            this.text = text;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            registerAction(text);
        }
    }

    private static native void setup();

    private static native void takedown();

    private static native void activateApplication();

    public static void main(String[] args) {
        if (!System.getProperty("os.name").contains("OS X")) {
            System.out.println("This test is for MacOS only. Automatically passed on other platforms.");
            return;
        }
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        try {
            runSwing(() -> {
                theTest = new TestMainKeyWindow();
            });
            theTest.runTest();
        } finally {
            if (theTest != null) {
                runSwing(() -> {
                    theTest.dispose();
                });
            }
        }
    }

    private static void runSwing(Runnable r) {
        try {
            SwingUtilities.invokeAndWait(r);
        } catch (InterruptedException e) {
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
