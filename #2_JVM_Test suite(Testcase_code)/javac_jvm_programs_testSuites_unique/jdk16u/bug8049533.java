

import java.awt.Frame;
import java.awt.Panel;
import java.awt.event.MouseWheelEvent;
import javax.swing.SwingUtilities;


public class bug8049533 {

    private static final double PRECISE_WHEEL_ROTATION = 3.14;

    public static void main(String[] args) {
        Frame frame = new Frame();
        Panel panel = new Panel();
        frame.add(panel);

        MouseWheelEvent event = new MouseWheelEvent(panel,
                0, 0, 0, 0, 0, 0, 0, 0, false, 0, 0,
                2, 
                PRECISE_WHEEL_ROTATION); 

        MouseWheelEvent convertedEvent = (MouseWheelEvent) SwingUtilities.
                convertMouseEvent(event.getComponent(), event, null);

        if (convertedEvent.getPreciseWheelRotation() != PRECISE_WHEEL_ROTATION) {
            throw new RuntimeException("PreciseWheelRotation field is not copied!");
        }
    }
}
