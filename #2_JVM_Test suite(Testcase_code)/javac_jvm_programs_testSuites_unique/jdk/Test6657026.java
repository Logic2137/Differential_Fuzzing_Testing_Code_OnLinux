



import sun.awt.SunToolkit;

import java.awt.event.ActionEvent;
import java.util.Set;
import javax.swing.JSplitPane;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class Test6657026 extends BasicSplitPaneUI implements Runnable {

    public static void main(String[] args) throws InterruptedException {
        if (new JSplitPane().getFocusTraversalKeys(0).isEmpty()){
            throw new Error("unexpected traversal keys");
        }
        new JSplitPane() {
            public void setFocusTraversalKeys(int id, Set keystrokes) {
                keystrokes.clear();
                super.setFocusTraversalKeys(id, keystrokes);
            }
        };
        if (new JSplitPane().getFocusTraversalKeys(0).isEmpty()) {
            throw new Error("shared traversal keys");
        }
        KEYBOARD_DIVIDER_MOVE_OFFSET = -KEYBOARD_DIVIDER_MOVE_OFFSET;

        ThreadGroup group = new ThreadGroup("$$$");
        Thread thread = new Thread(group, new Test6657026());
        thread.start();
        thread.join();
    }

    public void run() {
        SunToolkit.createNewAppContext();
        if (new JSplitPane().getFocusTraversalKeys(0).isEmpty()) {
            throw new Error("shared traversal keys");
        }
        JSplitPane pane = new JSplitPane();
        pane.setUI(this);

        createFocusListener().focusGained(null); 
        test(pane, "positiveIncrement", 3);
        test(pane, "negativeIncrement", 0);
    }

    private static void test(JSplitPane pane, String action, int expected) {
        ActionEvent event = new ActionEvent(pane, expected, action);
        pane.getActionMap().get(action).actionPerformed(event);
        int actual = pane.getDividerLocation();
        if (actual != expected) {
            throw new Error(actual + ", but expected " + expected);
        }
    }
}
