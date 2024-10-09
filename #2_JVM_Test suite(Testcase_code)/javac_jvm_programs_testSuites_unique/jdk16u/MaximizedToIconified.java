





import java.awt.Frame;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;

public class MaximizedToIconified
{
    static volatile int lastFrameState = Frame.NORMAL;
    static volatile boolean failed = false;
    static volatile Toolkit myKit;
    private static Robot robot;

    private static void checkState(Frame frame, int state) {
        frame.setExtendedState(state);
        robot.waitForIdle();
        robot.delay(100);

        System.out.println("state = " + state + "; getExtendedState() = " + frame.getExtendedState());

        if (failed) {
            frame.dispose();
            throw new RuntimeException("getOldState() != previous getNewState() in WINDOW_STATE_CHANGED event.");
        }
        if (lastFrameState != frame.getExtendedState()) {
            frame.dispose();
            throw new RuntimeException("getExtendedState() != last getNewState() in WINDOW_STATE_CHANGED event.");
        }
        if (frame.getExtendedState() != state) {
            frame.dispose();
            throw new RuntimeException("getExtendedState() != " + state + " as expected.");
        }
    }

    private static void examineStates(int states[]) {

        Frame frame = new Frame("test");
        frame.setSize(200, 200);
        frame.setVisible(true);

        robot.waitForIdle();

        frame.addWindowStateListener(new WindowStateListener() {
            public void windowStateChanged(WindowEvent e) {
                System.out.println("last = " + lastFrameState + "; getOldState() = " + e.getOldState() +
                        "; getNewState() = " + e.getNewState());
                if (e.getOldState() == lastFrameState) {
                    lastFrameState = e.getNewState();
                } else {
                    System.out.println("Wrong getOldState(): expected = " + lastFrameState + "; received = " +
                            e.getOldState());
                    failed = true;
                }
            }
        });

        for (int state : states) {
            if (myKit.isFrameStateSupported(state)) {
                checkState(frame, state);
            } else {
                System.out.println("Frame state = " + state + " is NOT supported by the native system. The state is skipped.");
            }
        }

        if (frame != null) {
            frame.dispose();
        }
    }

    private static void doTest() {

        myKit = Toolkit.getDefaultToolkit();

        
        
        
        examineStates(new int[] {Frame.MAXIMIZED_BOTH, Frame.ICONIFIED, Frame.NORMAL});
        examineStates(new int[] {Frame.ICONIFIED, Frame.MAXIMIZED_BOTH, Frame.NORMAL});

    }

    public static void main( String args[] ) throws Exception
    {
        robot = new Robot();
        doTest();

    }

}
