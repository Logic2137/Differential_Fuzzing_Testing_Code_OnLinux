import java.awt.*;
import java.awt.event.InputEvent;

public class ComponentPreferredSize {

    private int width = 300;

    private int height = 200;

    private final int hGap, vGap;

    private final int rows = 3;

    private final int columns = 2;

    private final int componentCount = 6;

    private Button[] buttons;

    private Frame frame;

    private Robot robot;

    private GridLayout layout;

    private volatile boolean actionPerformed = false;

    public ComponentPreferredSize(int hGap, int vGap) throws Exception {
        this.hGap = hGap;
        this.vGap = vGap;
        robot = new Robot();
        EventQueue.invokeAndWait(() -> {
            frame = new Frame("Test frame");
            frame.setSize(width, height);
            layout = new GridLayout(rows, columns, hGap, vGap);
            frame.setLayout(layout);
            buttons = new Button[componentCount];
            for (int i = 0; i < componentCount; i++) {
                buttons[i] = new Button("Button" + i);
                buttons[i].setPreferredSize(new Dimension((int) Math.random() * 100, (int) Math.random() * 100));
                frame.add(buttons[i]);
                buttons[i].addActionListener((event) -> {
                    actionPerformed = true;
                });
            }
            frame.setVisible(true);
        });
    }

    public static void main(String[] args) throws Exception {
        int hGap = 0;
        int vGap = 0;
        for (int i = 0; i < args.length; i++) {
            switch(args[i]) {
                case "-hg":
                    hGap = Integer.parseInt(args[++i]);
                    break;
                case "-vg":
                    vGap = Integer.parseInt(args[++i]);
                    break;
            }
        }
        new ComponentPreferredSize(hGap, vGap).doTest();
    }

    private void resizeFrame() throws Exception {
        EventQueue.invokeAndWait(() -> {
            Insets insets = frame.getInsets();
            double dH = (height - insets.top - insets.bottom - vGap * (rows - 1)) % rows;
            double dW = (width - insets.left - insets.right - hGap * (columns - 1)) % columns;
            height -= dH;
            width -= dW;
            frame.setSize(width, height);
            frame.revalidate();
        });
        robot.waitForIdle();
    }

    public void testBoundaries(int topLeftX, int topLeftY, int bottomRightX, int bottomRightY) throws Exception {
        actionPerformed = false;
        robot.mouseMove(topLeftX, topLeftY);
        robot.delay(500);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(500);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(3000);
        if (!actionPerformed) {
            frame.dispose();
            throw new RuntimeException("Clicking on the left top of button did not trigger action event");
        }
        actionPerformed = false;
        robot.mouseMove(bottomRightX, bottomRightY);
        robot.delay(500);
        robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(500);
        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
        robot.delay(3000);
        if (!actionPerformed) {
            frame.dispose();
            throw new RuntimeException("Clicking on the bottom right of button did not trigger action event");
        }
    }

    private void doTest() throws Exception {
        robot.waitForIdle();
        resizeFrame();
        int availableWidth = width - frame.getInsets().left - frame.getInsets().right;
        int componentWidth = (availableWidth + hGap) / columns - hGap;
        int availableHeight = height - frame.getInsets().top - frame.getInsets().bottom;
        int componentHeight = (availableHeight + vGap) / rows - vGap;
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].getSize().width != componentWidth || buttons[i].getSize().height != componentHeight) {
                frame.dispose();
                throw new RuntimeException("FAIL: Button " + i + " not of proper size" + "Expected: " + componentWidth + "*" + componentHeight + "Actual: " + buttons[i].getSize().width + "*" + buttons[i].getSize().height);
            }
        }
        int currentRow = 1;
        int currentColumn = 0;
        for (int i = 0; i < buttons.length; i++) {
            currentColumn++;
            if (currentColumn > columns) {
                currentColumn = 1;
                currentRow++;
            }
            int topPosX = frame.getLocationOnScreen().x + frame.getInsets().left + (currentColumn - 1) * (componentWidth + hGap);
            int topPosY = frame.getLocationOnScreen().y + frame.getInsets().top + (currentRow - 1) * (componentHeight + vGap);
            int bottomPosX = topPosX + componentWidth - 1;
            int bottomPosY = topPosY + componentHeight - 1;
            testBoundaries(topPosX, topPosY, bottomPosX, bottomPosY);
        }
        frame.dispose();
    }
}
