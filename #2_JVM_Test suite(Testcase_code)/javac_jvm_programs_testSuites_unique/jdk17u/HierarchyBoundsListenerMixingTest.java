import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import javax.swing.SwingUtilities;
import java.io.*;

public class HierarchyBoundsListenerMixingTest {

    protected void prepareControls() {
        dummy = new Frame();
        dummy.setSize(100, 100);
        dummy.setLocation(0, 350);
        dummy.setVisible(true);
        frame = new Frame("Test Frame");
        frame.setLayout(new FlowLayout());
        panel = new Panel();
        button = new Button("Button");
        label = new Label("Label");
        list = new List();
        list.add("One");
        list.add("Two");
        list.add("Three");
        choice = new Choice();
        choice.add("Red");
        choice.add("Orange");
        choice.add("Yellow");
        checkbox = new Checkbox("Checkbox");
        scrollbar = new Scrollbar(Scrollbar.HORIZONTAL, 0, 1, 0, 255);
        textfield = new TextField(15);
        textarea = new TextArea(5, 15);
        components = new Component[] { panel, button, label, list, choice, checkbox, scrollbar, textfield, textarea };
        ancestorResized = new boolean[components.length];
        ancestorMoved = new boolean[components.length];
        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent event) {
                System.err.println("User closed the window");
            }
        });
        HierarchyBoundsListener listener = new HierarchyBoundsListenerImpl();
        for (int i = 0; i < components.length; i++) {
            components[i].addHierarchyBoundsListener(listener);
            frame.add(components[i]);
        }
        frame.setBounds(100, 100, 300, 300);
        frame.setVisible(true);
    }

    private int frameBorderCounter() {
        String JAVA_HOME = System.getProperty("java.home");
        try {
            Process p = Runtime.getRuntime().exec(JAVA_HOME + "/bin/java FrameBorderCounter");
            try {
                p.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            if (p.exitValue() != 0) {
                throw new RuntimeException("FrameBorderCounter exited with not null code!\n" + readInputStream(p.getErrorStream()));
            }
            return Integer.parseInt(readInputStream(p.getInputStream()).trim());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private String readInputStream(InputStream is) throws IOException {
        byte[] buffer = new byte[4096];
        int len = 0;
        StringBuilder sb = new StringBuilder();
        try (InputStreamReader isr = new InputStreamReader(is)) {
            while ((len = is.read(buffer)) > 0) {
                sb.append(new String(buffer, 0, len));
            }
        }
        return sb.toString();
    }

    protected boolean performTest() {
        int BORDER_SHIFT = frameBorderCounter();
        BORDER_SHIFT = Math.abs(BORDER_SHIFT) == 1 ? BORDER_SHIFT : BORDER_SHIFT / 2;
        Robot robot = null;
        try {
            robot = new Robot();
            Thread.sleep(delay * 10);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Robot creation exception.");
        }
        robot.mouseMove((int) components[0].getLocationOnScreen().x + components[0].getSize().width / 2, (int) components[0].getLocationOnScreen().y + components[0].getSize().height / 2);
        robot.delay(delay);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(delay);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(delay);
        resetValues();
        try {
            EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    frame.setSize(new Dimension(frame.getSize().width + 10, frame.getSize().height + 10));
                    frame.invalidate();
                    frame.validate();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            passed = false;
        }
        if (!resizeTriggered) {
            synchronized (resizeLock) {
                try {
                    resizeLock.wait(delay * 10);
                } catch (Exception e) {
                }
            }
        }
        for (int i = 0; i < components.length; i++) {
            if (!ancestorResized[i]) {
                System.err.println("FAIL: Frame resized using API call. " + "Ancestor resized event did not occur for " + components[i].getClass());
                passed = false;
            }
        }
        if (moveCount > 0) {
            System.err.println("FAIL: Ancestor moved event occured when Frame resized using API");
            passed = false;
        }
        robot.delay(delay * 5);
        resetValues();
        int x = (int) frame.getLocationOnScreen().x;
        int y = (int) frame.getLocationOnScreen().y;
        int w = frame.getSize().width;
        int h = frame.getSize().height;
        robot.mouseMove(x + w + BORDER_SHIFT, y + h / 2);
        robot.delay(delay);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(delay);
        for (int i = 0; i < 20; i++) {
            robot.mouseMove(x + w + i + BORDER_SHIFT, y + h / 2);
            robot.delay(50);
        }
        robot.delay(delay);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        if (!resizeTriggered) {
            synchronized (resizeLock) {
                try {
                    resizeLock.wait(delay * 10);
                } catch (Exception e) {
                }
            }
        }
        for (int i = 0; i < components.length; i++) {
            if (!ancestorResized[i]) {
                System.err.println("FAIL: Frame resized using mouse action. " + "Ancestor resized event did not occur for " + components[i].getClass());
                passed = false;
            }
        }
        if (moveCount > 0) {
            System.err.println("FAIL: Ancestor moved event occured when Frame resized using mouse");
            passed = false;
        }
        resetValues();
        try {
            EventQueue.invokeAndWait(new Runnable() {

                public void run() {
                    frame.setLocation(frame.getLocation().x + 20, frame.getLocation().y + 20);
                    frame.invalidate();
                    frame.validate();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            passed = false;
        }
        if (!moveTriggered) {
            synchronized (moveLock) {
                try {
                    moveLock.wait(delay * 10);
                } catch (Exception e) {
                }
            }
        }
        for (int i = 0; i < components.length; i++) {
            if (!ancestorMoved[i]) {
                System.err.println("FAIL: Frame moved using API call. " + "Ancestor moved event did not occur for " + components[i].getClass());
                passed = false;
            }
        }
        if (resizeCount > 0) {
            System.err.println("FAIL: Ancestor resized event occured when Frame moved using API");
            passed = false;
        }
        robot.delay(delay * 10);
        resetValues();
        x = (int) frame.getLocationOnScreen().x;
        y = (int) frame.getLocationOnScreen().y;
        w = frame.getSize().width;
        h = frame.getSize().height;
        robot.mouseMove((int) dummy.getLocationOnScreen().x + dummy.getSize().width / 2, (int) dummy.getLocationOnScreen().y + dummy.getSize().height / 2);
        robot.delay(delay);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(delay);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(delay);
        robot.mouseMove(x + w / 2, y + 10);
        robot.delay(delay);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(delay);
        for (int i = 1; i <= 20; i++) {
            robot.mouseMove(x + w / 2 + i, y + 10);
            robot.delay(50);
        }
        robot.delay(delay);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        if (!moveTriggered) {
            synchronized (moveLock) {
                try {
                    moveLock.wait(delay * 10);
                } catch (Exception e) {
                }
            }
        }
        for (int i = 0; i < components.length; i++) {
            if (!ancestorMoved[i]) {
                System.err.println("FAIL: Frame moved using mouse action. " + "Ancestor moved event did not occur for " + components[i].getClass());
                passed = false;
            }
        }
        if (resizeCount > 0) {
            System.err.println("FAIL: Ancestor resized event occured when Frame moved using mouse");
            passed = false;
        }
        return passed;
    }

    private void resetValues() {
        moveTriggered = false;
        resizeTriggered = false;
        moveCount = 0;
        resizeCount = 0;
        for (int i = 0; i < ancestorResized.length; i++) {
            ancestorResized[i] = false;
            ancestorMoved[i] = false;
        }
    }

    private void keyType(int key, Robot robot) throws Exception {
        robot.keyPress(key);
        robot.delay(keyDelay);
        robot.keyRelease(key);
        robot.delay(keyDelay);
    }

    class HierarchyBoundsListenerImpl implements HierarchyBoundsListener {

        public void ancestorMoved(HierarchyEvent ce) {
            if (check) {
                System.out.println("Moved " + ce.getComponent());
            }
            for (int i = 0; i < components.length; i++) {
                if (components[i].equals(ce.getComponent())) {
                    ancestorMoved[i] = true;
                    moveCount++;
                    if (moveCount == components.length) {
                        moveTriggered = true;
                        synchronized (moveLock) {
                            try {
                                moveLock.notifyAll();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        }

        public void ancestorResized(HierarchyEvent ce) {
            if (check) {
                System.out.println("Resized " + ce.getComponent());
            }
            for (int i = 0; i < components.length; i++) {
                if (components[i].equals(ce.getComponent())) {
                    if (!frame.equals(ce.getChanged()) || ce.getChangedParent() != null) {
                        System.err.println("FAIL: Invalid value of HierarchyEvent.getXXX");
                        System.err.println("ce.getChanged() : " + ce.getChanged());
                        System.err.println("ce.getChangedParent() : " + ce.getChangedParent());
                        passed = false;
                    }
                    ancestorResized[i] = true;
                    resizeCount++;
                    if (resizeCount == components.length) {
                        resizeTriggered = true;
                        synchronized (resizeLock) {
                            try {
                                resizeLock.notifyAll();
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }
        }
    }

    private Frame frame, dummy;

    private Panel panel;

    private Button button;

    private Label label;

    private List list;

    private Choice choice;

    private Checkbox checkbox;

    private Scrollbar scrollbar;

    private TextField textfield;

    private TextArea textarea;

    private Component[] components;

    private boolean[] ancestorResized;

    private boolean[] ancestorMoved;

    private int delay = 500;

    private int keyDelay = 50;

    private int moveCount = 0;

    private int resizeCount = 0;

    private boolean passed = true;

    private volatile boolean moveTriggered = false;

    private volatile boolean resizeTriggered = false;

    private final Object moveLock = new Object();

    private final Object resizeLock = new Object();

    private boolean check = false;

    private void invoke() throws InterruptedException {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {

                public void run() {
                    prepareControls();
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            if (!performTest()) {
                fail("Test failed");
            }
        } catch (InvocationTargetException ex) {
            fail(ex.getMessage());
        }
    }

    private static void init() throws InterruptedException {
        HierarchyBoundsListenerMixingTest instance = new HierarchyBoundsListenerMixingTest();
        instance.invoke();
        pass();
    }

    private static boolean theTestPassed = false;

    private static boolean testGeneratedInterrupt = false;

    private static String failureMessage = "";

    private static Thread mainThread = null;

    private static int sleepTime = 300000;

    public static void main(String[] args) throws InterruptedException {
        mainThread = Thread.currentThread();
        try {
            init();
        } catch (TestPassedException e) {
            return;
        }
        try {
            Thread.sleep(sleepTime);
            throw new RuntimeException("Timed out after " + sleepTime / 1000 + " seconds");
        } catch (InterruptedException e) {
            if (!testGeneratedInterrupt) {
                throw e;
            }
            testGeneratedInterrupt = false;
            if (theTestPassed == false) {
                throw new RuntimeException(failureMessage);
            }
        }
    }

    public static synchronized void setTimeoutTo(int seconds) {
        sleepTime = seconds * 1000;
    }

    public static synchronized void pass() {
        System.out.println("The test passed.");
        System.out.println("The test is over, hit  Ctl-C to stop Java VM");
        if (mainThread == Thread.currentThread()) {
            theTestPassed = true;
            throw new TestPassedException();
        }
        theTestPassed = true;
        testGeneratedInterrupt = true;
        mainThread.interrupt();
    }

    public static synchronized void fail() {
        fail("it just plain failed! :-)");
    }

    public static synchronized void fail(String whyFailed) {
        System.out.println("The test failed: " + whyFailed);
        System.out.println("The test is over, hit  Ctl-C to stop Java VM");
        if (mainThread == Thread.currentThread()) {
            throw new RuntimeException(whyFailed);
        }
        theTestPassed = false;
        testGeneratedInterrupt = true;
        failureMessage = whyFailed;
        mainThread.interrupt();
    }
}

class TestPassedException extends RuntimeException {
}
