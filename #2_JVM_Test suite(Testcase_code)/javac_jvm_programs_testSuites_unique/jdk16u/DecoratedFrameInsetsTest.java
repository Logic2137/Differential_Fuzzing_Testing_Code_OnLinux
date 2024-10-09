



import java.awt.*;

public class DecoratedFrameInsetsTest {
    static Robot robot;
    private static Insets expectedInsets;

    public static void main(String[] args) throws Exception {
        robot = new Robot();
        expectedInsets = getExpectedInsets();
        System.out.println("Normal state insets: " + expectedInsets);
        testState(Frame.MAXIMIZED_BOTH);
        testState(Frame.ICONIFIED);
        testState(Frame.MAXIMIZED_HORIZ);
        testState(Frame.MAXIMIZED_VERT);
    }

    private static Insets getExpectedInsets() {
        Frame frame = new Frame();
        frame.setVisible(true);
        robot.waitForIdle();
        robot.delay(200);
        Insets expectedInsets = frame.getInsets();
        frame.dispose();
        return expectedInsets;
    }

    static void testState(int state) {
        Frame frame = new Frame();
        if( Toolkit.getDefaultToolkit().isFrameStateSupported(state)) {
            frame.setBounds(150, 150, 200, 200);
            frame.setExtendedState(state);
            frame.setVisible(true);
            robot.waitForIdle();
            robot.delay(200);
            System.out.println("State " + state +
                                               " insets: " + frame.getInsets());

            frame.setExtendedState(Frame.NORMAL);
            frame.toFront();
            robot.waitForIdle();
            robot.delay(200);
            Insets insets = frame.getInsets();
            frame.dispose();
            System.out.println("State " + state +
                                           " back to normal insets: " + insets);
            if(!expectedInsets.equals(insets)) {
                throw new RuntimeException("Insets are wrong " + insets);
            }
        }
    }
}
