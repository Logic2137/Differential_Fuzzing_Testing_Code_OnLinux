



import java.awt.*;

public class HideMaximized {
    public static void main(String[] args) {
        if (!Toolkit.getDefaultToolkit().isFrameStateSupported(Frame.MAXIMIZED_BOTH)) {
            
            return;
        }

        
        Frame frame = new Frame("test");
        test(frame);

        
        frame = new Frame("undecorated test");
        frame.setUndecorated(true);
        test(frame);
    }

    private static void test(Frame frame) {
        frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        frame.setVisible(true);

        try { Thread.sleep(1000); } catch (Exception ex) {}

        if (frame.getExtendedState() != Frame.MAXIMIZED_BOTH) {
            throw new RuntimeException("The maximized state has not been applied");
        }

        
        frame.dispose();

        try { Thread.sleep(1000); } catch (Exception ex) {}

        if (frame.getExtendedState() != Frame.MAXIMIZED_BOTH) {
            throw new RuntimeException("The maximized state has been reset");
        }
    }
}
