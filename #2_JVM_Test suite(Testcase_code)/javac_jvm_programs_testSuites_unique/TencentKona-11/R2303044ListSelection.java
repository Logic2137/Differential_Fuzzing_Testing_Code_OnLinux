

import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.List;
import java.awt.Robot;


public final class R2303044ListSelection {

    public static final String ITEM_NAME = "myItem";

    public static void main(final String[] args) throws HeadlessException {
        final Frame frame = new Frame("Test Frame");
        final List list = new List();
        frame.setSize(300, 200);
        list.add(ITEM_NAME);
        list.select(0);
        frame.add(list);
        frame.validate();
        frame.setVisible(true);
        sleep();
        if (!ITEM_NAME.equals(list.getSelectedItem())) {
            throw new RuntimeException("List item not selected item.");
        }
        list.removeAll();
        frame.dispose();
    }

    private static void sleep() {
        try {
            Robot robot = new Robot();
            robot.waitForIdle();
            Thread.sleep(1000);
        } catch (final Exception ignored) {
            ignored.printStackTrace();
        }
    }
}
