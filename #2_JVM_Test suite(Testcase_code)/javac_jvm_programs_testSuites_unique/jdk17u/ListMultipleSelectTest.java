import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.List;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;

public final class ListMultipleSelectTest {

    private static List aList;

    private static Panel panel;

    private static Point p;

    private static Robot robot;

    private static int[] selected;

    public static void main(String[] args) throws Exception {
        Frame frame = new Frame();
        try {
            panel = new Panel();
            aList = new List();
            aList.add("Test item1");
            aList.add("Test item2");
            aList.add("Test item3");
            aList.add("Test item4");
            frame.setLayout(new FlowLayout());
            panel.add(aList);
            frame.setSize(200, 200);
            frame.setLocationRelativeTo(null);
            panel.setVisible(true);
            frame.add(panel);
            frame.setVisible(true);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(" InterruptedException. Test failed. ");
            }
            Dimension listSize = aList.getSize();
            p = aList.getLocationOnScreen();
            int stepY = listSize.height / aList.getItemCount();
            System.out.println("Multiple mode is ON");
            aList.setMultipleMode(true);
            robot = new Robot();
            robot.setAutoDelay(0);
            robot.setAutoWaitForIdle(false);
            robot.delay(10);
            robot.waitForIdle();
            for (int i = 0; i < aList.getItemCount(); i++) {
                mousePress(p.x + listSize.width / 2, p.y + stepY / 2 + stepY * i);
            }
            selected = aList.getSelectedIndexes();
            System.out.println("Multiple mode is ON");
            aList.setMultipleMode(true);
            int[] newSelected = aList.getSelectedIndexes();
            if (!java.util.Arrays.equals(newSelected, selected)) {
                throw new RuntimeException(" Incorrect item remains selected " + "after setMultipleMode(true). ");
            }
            aList.setMultipleMode(false);
            System.out.println("Multiple mode is OFF");
            selected = aList.getSelectedIndexes();
            if (selected[0] != 3 || selected.length != 1) {
                throw new RuntimeException(" Incorrect item remains selected " + "after setMultipleMode(false) or it is more then one " + "item remaining.Forward. ");
            }
            System.out.println("Multiple mode is ON");
            aList.setMultipleMode(true);
            if (selected[0] != 3 || selected.length != 1) {
                throw new RuntimeException(" Incorrect item remains selected " + "after setMultipleMode(true) or it is more then one " + "item remaining. ");
            }
            deselectAll();
            for (int i = aList.getItemCount() - 1; i >= 0; i--) {
                mousePress(p.x + listSize.width / 2, p.y + stepY / 2 + stepY * i);
            }
            System.out.println("Multiple mode is OFF");
            aList.setMultipleMode(false);
            selected = aList.getSelectedIndexes();
            if (selected[0] != 0 || selected.length != 1) {
                throw new RuntimeException(" Incorrect item remains selected " + "after setMultipleMode(false) or it is more then one " + "item remaining.Backward. ");
            }
            System.out.println("Test succeeded.");
        } finally {
            frame.dispose();
        }
    }

    private static void mousePress(int x, int y) {
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.delay(1000);
    }

    private static void deselectAll() {
        for (int i = 0; i < selected.length; i++) {
            aList.deselect(selected[i]);
        }
    }
}
