

import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.Dimension;



public class MaximizedNormalBoundsUndecoratedTest {
    private Frame frame;
    public static void main(String args[]) {
        if (!Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)
                && !Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.NORMAL)) {
            return;
        }
        MaximizedNormalBoundsUndecoratedTest test = new MaximizedNormalBoundsUndecoratedTest();
        boolean doPass = true;
        if( !test.doTest() ) {
            System.out.println("Maximizing frame not saving correct normal bounds");
            doPass = false;
        }

        if(!doPass) {
            throw new RuntimeException("Maximizing frame not saving correct normal bounds");
        }
    }

    boolean doTest() {
        Dimension beforeMaximizeCalled = new Dimension(300,300);

        frame = new Frame("Test Frame");
        frame.setUndecorated(true);
        frame.setFocusable(true);
        frame.setSize(beforeMaximizeCalled);
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setExtendedState(Frame.NORMAL);

        Dimension afterMaximizedCalled= frame.getBounds().getSize();

        frame.dispose();

        if (beforeMaximizeCalled.equals(afterMaximizedCalled)) {
            return true;
        }
        return false;
    }
}
