

import java.awt.Frame;
import java.awt.Toolkit;
import javax.swing.JFrame;
import java.awt.EventQueue;
import java.awt.FlowLayout;



public class UndecoratedInitiallyIconified {
    private static JFrame frame;
    public static void main(String args[]) throws Exception {
        if (!Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.ICONIFIED)) {
            return;
        }
        EventQueue.invokeAndWait( () -> {
            frame = new JFrame("Test Frame");
            frame.setLayout(new FlowLayout());
            frame.setBounds(50,50,300,300);
            frame.setUndecorated(true);
            frame.setExtendedState(Frame.ICONIFIED);
            if(frame.getExtendedState() != Frame.ICONIFIED) {
                throw new RuntimeException("getExtendedState is not Frame.ICONIFIED as expected");
            }
        });
    }
}
