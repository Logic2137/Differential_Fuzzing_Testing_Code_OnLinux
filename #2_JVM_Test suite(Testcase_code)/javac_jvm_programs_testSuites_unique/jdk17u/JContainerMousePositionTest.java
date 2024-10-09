import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicReference;

public class JContainerMousePositionTest {

    private static JButton jButton1;

    private static JButton jButton4;

    private static JFrame frame1;

    private static Container contentPane;

    public static void main(final String[] args) throws Exception {
        Robot robot = new Robot();
        robot.setAutoDelay(200);
        robot.setAutoWaitForIdle(true);
        SwingUtilities.invokeAndWait(JContainerMousePositionTest::init);
        robot.delay(500);
        robot.waitForIdle();
        AtomicReference<Point> centerC4 = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            centerC4.set(jButton4.getLocation());
            contentPane.remove(jButton4);
            contentPane.validate();
            contentPane.repaint();
        });
        robot.waitForIdle();
        AtomicReference<Rectangle> frameBounds = new AtomicReference<>();
        AtomicReference<Insets> frameInsets = new AtomicReference<>();
        AtomicReference<Dimension> button1Size = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            frameBounds.set(frame1.getBounds());
            frameInsets.set(frame1.getInsets());
            button1Size.set(jButton1.getSize());
        });
        robot.mouseMove(frameBounds.get().x + frameInsets.get().left + button1Size.get().width / 2, frameBounds.get().y + frameInsets.get().top + button1Size.get().height / 2);
        AtomicReference<Point> pFalse = new AtomicReference<>();
        AtomicReference<Point> pTrue = new AtomicReference<>();
        SwingUtilities.invokeAndWait(() -> {
            pFalse.set(frame1.getMousePosition(false));
            pTrue.set(frame1.getMousePosition(true));
        });
        robot.waitForIdle();
        if (pFalse.get() != null) {
            throw new RuntimeException("Test failed: Container.getMousePosition(false) returned non-null over one of children.");
        }
        System.out.println("Test stage completed: Container.getMousePosition(false) returned null result over child Component. Passed.");
        if (pTrue.get() == null) {
            throw new RuntimeException("Test failed: Container.getMousePosition(true) returned null result over child Component");
        }
        System.out.println("Test stage compelted: Container.getMousePosition(true) returned non-null result over child Component. Passed.");
        robot.mouseMove(frameBounds.get().x + frameBounds.get().width + 10, frameBounds.get().y + frameBounds.get().height + 10);
        SwingUtilities.invokeAndWait(() -> {
            pFalse.set(frame1.getMousePosition(false));
            pTrue.set(frame1.getMousePosition(true));
        });
        robot.waitForIdle();
        if (pFalse.get() != null || pTrue.get() != null) {
            throw new RuntimeException("Test failed: Container.getMousePosition(boolean) returned incorrect result outside Container");
        }
        System.out.println("Test stage completed: Container.getMousePosition(boolean) returned null result outside Container. Passed.");
        robot.mouseMove(frameBounds.get().x + frameInsets.get().left + centerC4.get().x, frameBounds.get().y + frameInsets.get().top + centerC4.get().y);
        robot.waitForIdle();
        SwingUtilities.invokeAndWait(() -> {
            pFalse.set(contentPane.getMousePosition(false));
            pTrue.set(frame1.getMousePosition(true));
        });
        robot.waitForIdle();
        if (pFalse.get() == null || pTrue.get() == null) {
            throw new RuntimeException("Test failed: Container.getMousePosition(boolean) returned null result inside Container.");
        }
        System.out.println("Test stage completed: Container.getMousePosition(boolean) returned non-null results  inside Container. Passed.");
        if (pTrue.get().x != frameInsets.get().left + centerC4.get().x || pTrue.get().y != frameInsets.get().top + centerC4.get().y) {
            throw new RuntimeException("Test failed: Container.getMousePosition(true) returned incorrect result inside Container.");
        }
        System.out.println("Test stage completed: Container.getMousePosition(true) returned correct result inside Container. Passed.");
        System.out.println("TEST PASSED");
    }

    private static void init() {
        frame1 = new JFrame("Testing getMousePosition() on LWs");
        jButton1 = new JButton("C1");
        jButton4 = new JButton("C4");
        contentPane = frame1.getContentPane();
        contentPane.setLayout(new GridLayout(2, 2, 25, 25));
        contentPane.add(jButton1);
        contentPane.add(new JButton("C2"));
        contentPane.add(new JButton("C3"));
        contentPane.add(jButton4);
        frame1.setSize(200, 200);
        frame1.setVisible(true);
    }
}
