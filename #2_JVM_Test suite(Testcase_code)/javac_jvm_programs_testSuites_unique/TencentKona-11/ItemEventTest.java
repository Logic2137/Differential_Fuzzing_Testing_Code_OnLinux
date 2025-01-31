



import java.awt.AWTException;
import java.awt.Event;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.List;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class ItemEventTest extends Frame
{
    List list;
    final String expectedSelectionOrder;
    StringBuilder actualSelectionOrder;
    Robot robot;

    public ItemEventTest()
    {
        try {
            robot = new Robot();
        } catch(AWTException e) {
            throw new RuntimeException(e.getMessage());
        }
        expectedSelectionOrder = "01230123";

        list = new List(4, true);
        list.add("0");
        list.add("1");
        list.add("2");
        list.add("3");

        add(list);
        setSize(400,400);
        setLayout(new FlowLayout());
        pack();
        setVisible(true);
        robot.waitForIdle();
    }

    @Override
    public boolean handleEvent(Event e) {
        if (e.target instanceof List) {
            if (e.id == Event.LIST_DESELECT || e.id == Event.LIST_SELECT) {
                actualSelectionOrder.append(e.arg);
            }
        }
        return true;
    }

    void testHandleEvent() {
        
        
        performTest();
    }

    void testItemListener() {
        list.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent ie) {
                actualSelectionOrder.append(ie.getItem());
            }
        });
        performTest();
    }

    void performTest() {
        actualSelectionOrder = new StringBuilder();
        Point loc = list.getLocationOnScreen();
        Rectangle rect = list.getBounds();
        int dY = rect.height / list.getItemCount();
        loc = new Point(loc.x + 10, loc.y + 5);

        String osName = System.getProperty("os.name");
        boolean isMac = osName.contains("Mac") || osName.contains("mac");
        if(isMac) {
            robot.keyPress(KeyEvent.VK_META);
            robot.waitForIdle();
        }

        
        for (int j = 0; j < 2; ++j) {
            for (int i = 0; i < list.getItemCount(); ++i) {
                robot.mouseMove(loc.x, loc.y + i * dY);
                robot.waitForIdle();
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.waitForIdle();
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
                robot.waitForIdle();
            }
        }

        if(isMac) {
            robot.keyRelease(KeyEvent.VK_META);
        }

        if (!expectedSelectionOrder.equals(actualSelectionOrder.toString())) {
            dispose();
            throw new RuntimeException("ItemEvent for selection & deselection"
                + " of multi select List's item is not correct"
                + " Expected : " + expectedSelectionOrder
                + " Actual : " + actualSelectionOrder);
        }
    }

    public static void main(String args[]) {
       ItemEventTest test = new ItemEventTest();
       test.testHandleEvent();
       test.testItemListener();
       test.dispose();
    }
}
