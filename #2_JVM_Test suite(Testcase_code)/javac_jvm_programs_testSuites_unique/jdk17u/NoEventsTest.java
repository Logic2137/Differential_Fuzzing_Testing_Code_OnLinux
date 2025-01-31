import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class NoEventsTest extends Frame {

    public static final int DEF_WIDTH = 400, DEF_HEIGHT = 300, DEF_TOP = 1, DEF_LEFT = 100, DEF_ROW = 0, DEF_COL = 0;

    static boolean automatic = true;

    static Window[] windows;

    static Frame main_frame, jumpingFrame;

    static Button focus_button;

    static Robot robot;

    static void pause(int timeout) {
        Toolkit.getDefaultToolkit().sync();
        robot.waitForIdle();
        robot.delay(100);
    }

    static GlobalListener listener;

    public static void main(String[] args) {
        listener = new GlobalListener();
        Toolkit.getDefaultToolkit().addAWTEventListener(listener, AWTEvent.FOCUS_EVENT_MASK | AWTEvent.WINDOW_EVENT_MASK);
        try {
            robot = new Robot();
        } catch (Exception e) {
        }
        main_frame = new Frame("focusable frame");
        focus_button = new Button("button to focus");
        main_frame.add(focus_button);
        main_frame.pack();
        main_frame.setVisible(true);
        main_frame.setLocation(10, 600);
        main_frame.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                listener.report();
                System.exit(0);
            }
        });
        jumpingFrame = new Frame("Jumping frame");
        jumpingFrame.setBounds(DEF_LEFT, DEF_TOP, DEF_WIDTH, DEF_HEIGHT);
        windows = new Window[7];
        windows[0] = new TestWindow(0, 0, false, main_frame);
        windows[2] = new NoEventsTest(1, 0, false, true);
        windows[3] = new NoEventsTest(2, 0, false, false);
        windows[5] = new TestDialog(0, 1, false, true, main_frame);
        windows[6] = new TestDialog(1, 1, false, false, main_frame);
        if (!automatic) {
            int windowInd;
            for (windowInd = 0; windowInd < windows.length; windowInd++) {
                if (windows[windowInd] != null) {
                    windows[windowInd].setVisible(true);
                }
            }
        }
        if (automatic) {
            int windowInd;
            for (windowInd = 0; windowInd < windows.length; windowInd++) {
                if (windows[windowInd] != null) {
                    windows[windowInd].setVisible(true);
                    focus_button.requestFocus();
                    pause(1000);
                    performFocusClick(windows[windowInd]);
                    focus_button.requestFocus();
                    pause(500);
                    performActionClick(windows[windowInd]);
                    jumpingFrame.setVisible(true);
                    pause(1000);
                    jumpingFrame.toBack();
                    pause(500);
                    jumpingFrame.toFront();
                    pause(500);
                    windows[windowInd].toBack();
                    pause(500);
                    windows[windowInd].toFront();
                    pause(500);
                    if (windows[windowInd] instanceof Frame) {
                        Frame toTest = (Frame) windows[windowInd];
                        pause(500);
                        toTest.setExtendedState(Frame.MAXIMIZED_BOTH);
                        pause(500);
                        toTest.setExtendedState(Frame.NORMAL);
                    }
                    windows[windowInd].dispose();
                    jumpingFrame.dispose();
                }
            }
            pause(1000);
            System.err.println("Test finished.");
            if (!listener.report()) {
                throw new RuntimeException("Test Failed. See error stream output for details");
            }
        }
    }

    static void performFocusClick(Window parent) {
        if (parent == null) {
            return;
        }
        for (int compInd = 0; compInd < parent.getComponentCount(); compInd++) {
            Component child = parent.getComponent(compInd);
            if (child instanceof TestPanel) {
                TestPanel pan = (TestPanel) child;
                pan.performFocusClicks(robot);
                pause(100);
            }
        }
    }

    static void performActionClick(Window parent) {
        if (parent == null) {
            return;
        }
        for (int compInd = 0; compInd < parent.getComponentCount(); compInd++) {
            Component child = parent.getComponent(compInd);
            if (child instanceof TestPanel) {
                TestPanel pan = (TestPanel) child;
                pan.performActionClicks(robot);
                pause(100);
            }
        }
    }

    public NoEventsTest(int row, int col, boolean focusable, boolean resizable) {
        super("Frame" + row + "" + col);
        TestPanel panel = new TestPanel(row, col);
        if (NoEventsTest.automatic) {
            row = NoEventsTest.DEF_ROW;
            col = NoEventsTest.DEF_COL;
        }
        setName(getTitle());
        add("Center", panel);
        Label l = new Label(getClass().getSuperclass().getName() + ", " + (focusable ? "focusable" : "non-focusable") + ", " + (resizable ? "resizable" : "non-resizable"));
        l.setBackground(Color.green);
        add("North", l);
        setBounds(NoEventsTest.DEF_LEFT + DEF_WIDTH * col, DEF_TOP + DEF_HEIGHT * row, DEF_WIDTH, DEF_HEIGHT);
        if (!focusable) {
            setFocusableWindowState(false);
        }
        if (!resizable) {
            setResizable(false);
        }
    }
}

class TestWindow extends Window {

    public TestWindow(int row, int col, boolean focusable, Frame owner) {
        super(owner);
        setName("Window" + row + "" + col);
        TestPanel panel = new TestPanel(row, col);
        if (NoEventsTest.automatic) {
            row = NoEventsTest.DEF_ROW;
            col = NoEventsTest.DEF_COL;
        }
        add("Center", panel);
        Label l = new Label(getClass().getSuperclass().getName() + ", " + (focusable ? "focusable" : "non-focusable") + ", " + (false ? "resizable" : "non-resizable"));
        l.setBackground(Color.green);
        add("North", l);
        setBounds(NoEventsTest.DEF_LEFT + NoEventsTest.DEF_WIDTH * col, NoEventsTest.DEF_TOP + NoEventsTest.DEF_HEIGHT * row, NoEventsTest.DEF_WIDTH, NoEventsTest.DEF_HEIGHT);
        if (!focusable) {
            setFocusableWindowState(false);
        }
    }
}

class TestDialog extends Dialog {

    public TestDialog(int row, int col, boolean focusable, boolean resizable, Frame owner) {
        super(owner);
        setName("Dialog" + row + "" + col);
        TestPanel panel = new TestPanel(row, col);
        if (NoEventsTest.automatic) {
            row = NoEventsTest.DEF_ROW;
            col = NoEventsTest.DEF_COL;
        }
        add("Center", panel);
        Label l = new Label(getClass().getSuperclass().getName() + ", " + (focusable ? "focusable" : "non-focusable") + ", " + (resizable ? "resizable" : "non-resizable"));
        l.setBackground(Color.green);
        add("North", l);
        setBounds(NoEventsTest.DEF_LEFT + NoEventsTest.DEF_WIDTH * col, NoEventsTest.DEF_TOP + NoEventsTest.DEF_HEIGHT * row, NoEventsTest.DEF_WIDTH, NoEventsTest.DEF_HEIGHT);
        if (!focusable) {
            setFocusableWindowState(false);
        }
        if (!resizable) {
            setResizable(false);
        }
    }
}

class TestPanel extends Panel {

    void clickComponent(Component comp, Robot robot) {
        if (comp instanceof Choice) {
            return;
        }
        Point compLoc = comp.getLocationOnScreen();
        Dimension size = comp.getSize();
        robot.mouseMove(compLoc.x + size.width / 2, compLoc.y + size.height / 2);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
    }

    void performFocusClicks(Robot robot) {
        for (int childInd = 0; childInd < getComponentCount(); childInd++) {
            performFocusClick(getComponent(childInd), robot);
        }
    }

    void performFocusClick(Component comp, Robot robot) {
        clickComponent(comp, robot);
    }

    void performActionClicks(Robot robot) {
        for (int childInd = 0; childInd < getComponentCount(); childInd++) {
            performActionClick(getComponent(childInd), robot);
        }
    }

    void performActionClick(Component comp, Robot robot) {
    }

    public TestPanel(int row, int col) {
        setLayout(new FlowLayout());
        Button b;
        add(b = new Button("press" + row + "" + col));
        b.setName(b.getLabel());
        TextField t;
        add(t = new TextField("text" + row + "" + col));
        t.setName(t.getText());
        java.awt.List list = new java.awt.List();
        add(list);
        list.setName("list");
        list.add("one");
        list.add("two");
        list.add("three");
        list.setMultipleMode(true);
        list.setName("list" + row + "" + col);
        Checkbox check = new Checkbox("checker");
        add(check);
        check.setName("check" + row + "" + col);
        Choice choice = new Choice();
        choice.add("one");
        choice.add("two");
        choice.add("three");
        add(choice);
        choice.setName("choice" + row + "" + col);
        Canvas can = new Canvas() {

            public Dimension getPreferredSize() {
                return new Dimension(10, 10);
            }
        };
        can.setBackground(Color.blue);
        add(can);
        can.setName("canvas" + row + "" + col);
        TextArea ta = new TextArea("text\ntttt\naaaa\nwwwww\nqqqqqq\neeeeee\nrrrrrr\nyyyyyy\nuuuuu", 3, 5);
        add(ta);
        ta.setName("textarea" + row + "" + col);
        Scrollbar bar = new Scrollbar(Scrollbar.HORIZONTAL);
        add(bar);
        bar.setName("scrollbar" + row + "" + col);
        CheckboxGroup group = new CheckboxGroup();
        Checkbox ch1 = new Checkbox("one", group, true);
        Checkbox ch2 = new Checkbox("two", group, false);
        add(ch1);
        add(ch2);
        ch1.setName("checkbox1 " + row + "" + col);
        ch2.setName("checkbox2 " + row + "" + col);
        ScrollPane pane = new ScrollPane(ScrollPane.SCROLLBARS_ALWAYS);
        add(pane);
        Button bigButton = new Button("abc") {

            public Dimension getPreferredSize() {
                return new Dimension(100, 100);
            }
        };
        pane.add(bigButton);
        bigButton.setName("bigbutton" + row + "" + col);
    }
}

class GlobalListener implements AWTEventListener {

    java.util.List errors = new java.util.LinkedList();

    public boolean report() {
        if (errors.size() != 0) {
            System.err.println("Test FAILED");
        } else {
            System.err.println("Test PASSED");
            return true;
        }
        ListIterator iter = errors.listIterator();
        while (iter.hasNext()) {
            System.err.println(iter.next());
        }
        return false;
    }

    public GlobalListener() {
    }

    Window getWindowParent(Component comp) {
        while (comp != null && !(comp instanceof Window)) {
            comp = comp.getParent();
        }
        return (Window) comp;
    }

    void reportError(AWTEvent e, String message) {
        String error = "ERROR: " + message + " : " + e;
        errors.add(error);
        System.err.println(error);
    }

    public void eventDispatched(AWTEvent e) {
        Component comp = (Component) e.getSource();
        Window parent = getWindowParent(comp);
        if (!(e instanceof WindowEvent || e instanceof FocusEvent)) {
            System.err.println("Strange event " + e);
        }
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.err.println(e);
        }
        switch(e.getID()) {
            case WindowEvent.WINDOW_OPENED:
            case WindowEvent.WINDOW_CLOSING:
            case WindowEvent.WINDOW_CLOSED:
            case WindowEvent.WINDOW_ICONIFIED:
            case WindowEvent.WINDOW_DEICONIFIED:
            case WindowEvent.WINDOW_STATE_CHANGED:
                return;
            case WindowEvent.WINDOW_LOST_FOCUS:
                {
                    WindowEvent we = (WindowEvent) e;
                    if (we.getOppositeWindow() != null && !we.getOppositeWindow().getFocusableWindowState()) {
                        reportError(e, "frame lost focus because of non-focusable window");
                    }
                    break;
                }
        }
        if (!parent.getFocusableWindowState()) {
            reportError(e, "focus event for component in non-focusable window " + parent.getName());
        }
        if (!comp.isFocusable()) {
            reportError(e, "focus event for non-focusable component");
        }
    }
}
