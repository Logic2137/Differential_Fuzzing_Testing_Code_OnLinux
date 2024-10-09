import java.awt.Frame;

public class SetExtendedState {

    public static void main(String[] args) {
        Frame frame = new Frame("frame");
        frame.setBounds(100, 100, 200, 200);
        frame.setVisible(true);
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setExtendedState(Frame.ICONIFIED);
        if (frame.getExtendedState() != Frame.ICONIFIED) {
            frame.dispose();
            throw new RuntimeException("Test Failed");
        }
        frame.dispose();
    }
}
