import java.awt.*;
import java.awt.event.*;

public class PopupPosTest {

    public static void main(final String[] args) {
        Frame frame = new TestFrame();
    }
}

class TestFrame extends Frame implements ItemListener {

    Robot robot;

    Toolkit tk = Toolkit.getDefaultToolkit();

    Choice choice = new Choice();

    boolean indexChanged = false;

    final static int INITIAL_ITEM = 99;

    volatile boolean stateChanged;

    public TestFrame() {
        for (int i = 0; i < 100; i++) {
            choice.addItem("Item Item Item " + i);
        }
        choice.addItemListener(this);
        choice.select(INITIAL_ITEM);
        choice.setFont(new Font("Courier", Font.BOLD + Font.ITALIC, 100));
        add(choice, BorderLayout.CENTER);
        Dimension screen = tk.getScreenSize();
        setSize(screen.width - 10, screen.height - 70);
        setVisible(true);
        toFront();
        try {
            robot = new Robot();
            robot.setAutoDelay(50);
            robot.waitForIdle();
            Point pt = choice.getLocationOnScreen();
            mouseMoveAndPressOnChoice(pt.x + choice.getWidth() / 2, pt.y + choice.getHeight() / 4);
            mouseMoveAndPressOnChoice(pt.x + choice.getWidth() / 2, pt.y + choice.getHeight() / 2);
            mouseMoveAndPressOnChoice(pt.x + choice.getWidth() / 2, pt.y + choice.getHeight() * 3 / 4);
            stateChanged = false;
            openChoice();
            closeChoice();
        } catch (Throwable e) {
            throw new RuntimeException("The test was not completed.\n\n" + e);
        }
        if (!indexChanged) {
            throw new RuntimeException("Test failed. Another item wasn't selected.");
        }
        if (stateChanged) {
            throw new RuntimeException("Test failed. ItemEvent was generated on a simple mouse click when the dropdown appears under mouse");
        }
    }

    public void itemStateChanged(ItemEvent ie) {
        System.out.println("choice.stateChanged = " + ie);
        stateChanged = true;
    }

    public void mouseMoveAndPressOnChoice(int x, int y) {
        openChoice();
        robot.mouseMove(x, y);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(30);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
        closeChoice();
        checkSelectedIndex();
    }

    public void openChoice() {
        Point pt = choice.getLocationOnScreen();
        robot.mouseMove(pt.x + choice.getWidth() - choice.getHeight() / 4, pt.y + choice.getHeight() / 2);
        robot.mousePress(InputEvent.BUTTON1_MASK);
        robot.delay(30);
        robot.mouseRelease(InputEvent.BUTTON1_MASK);
        robot.waitForIdle();
    }

    public void closeChoice() {
        robot.keyPress(KeyEvent.VK_ESCAPE);
        robot.keyRelease(KeyEvent.VK_ESCAPE);
        robot.waitForIdle();
    }

    public void checkSelectedIndex() {
        if (choice.getSelectedIndex() != INITIAL_ITEM) {
            System.out.println("choice.getSelectedIndex = " + choice.getSelectedIndex());
            indexChanged = true;
        }
    }
}
