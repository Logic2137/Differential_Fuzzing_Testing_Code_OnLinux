import java.awt.*;

public class ExceptionOnSetExtendedStateTest {

    private static final int[] frameStates = { Frame.NORMAL, Frame.ICONIFIED, Frame.MAXIMIZED_BOTH };

    private static boolean validatePlatform() {
        String osName = System.getProperty("os.name");
        if (osName == null) {
            throw new RuntimeException("Name of the current OS could not be retrieved.");
        }
        return osName.startsWith("Mac");
    }

    private static void testStateChange(int oldState, int newState, boolean decoratedFrame) {
        System.out.println(String.format("testStateChange: oldState='%d', newState='%d', decoratedFrame='%b'", oldState, newState, decoratedFrame));
        Frame frame = new Frame("ExceptionOnSetExtendedStateTest");
        frame.setSize(200, 200);
        frame.setUndecorated(!decoratedFrame);
        frame.setVisible(true);
        try {
            Robot robot = new Robot();
            robot.waitForIdle();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new RuntimeException("Unexpected failure");
        }
        frame.setExtendedState(oldState);
        sleep(1000);
        frame.setExtendedState(newState);
        boolean stateWasNotChanged = true;
        int currentState = 0;
        for (int i = 0; (i < 3) && stateWasNotChanged; i++) {
            sleep(1000);
            currentState = frame.getExtendedState();
            if ((currentState == newState) || (((newState & Frame.ICONIFIED) != 0) && ((currentState & Frame.ICONIFIED) != 0))) {
                stateWasNotChanged = false;
            }
        }
        frame.dispose();
        if (stateWasNotChanged) {
            throw new RuntimeException(String.format("Frame state was not changed. currentState='%d'", currentState));
        }
    }

    private static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        if (!validatePlatform()) {
            System.out.println("This test is only for OS X.");
            return;
        }
        for (int i = 0; i < frameStates.length; i++) {
            testStateChange(frameStates[i], Frame.ICONIFIED | Frame.MAXIMIZED_BOTH, true);
            testStateChange(frameStates[i], Frame.ICONIFIED | Frame.MAXIMIZED_BOTH, false);
            testStateChange(Frame.ICONIFIED | Frame.MAXIMIZED_BOTH, frameStates[i], true);
            testStateChange(Frame.ICONIFIED | Frame.MAXIMIZED_BOTH, frameStates[i], false);
        }
    }
}
