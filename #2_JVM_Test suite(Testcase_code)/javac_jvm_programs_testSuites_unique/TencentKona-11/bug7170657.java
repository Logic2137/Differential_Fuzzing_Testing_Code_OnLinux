

import java.awt.Frame;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.SwingUtilities;
import javax.swing.event.MenuDragMouseEvent;


public final class bug7170657 {

    private static boolean FAILED;

    public static void main(final String[] args) {
        final int mask = InputEvent.META_DOWN_MASK | InputEvent.CTRL_MASK;

        Frame f = new Frame();

        MouseEvent mwe = new MouseWheelEvent(f, 1, 1, mask, 1, 1, 1, 1, 1, true,
                                             1, 1, 1);
        MouseEvent mdme = new MenuDragMouseEvent(f, 1, 1, mask, 1, 1, 1, 1, 1,
                                                 true, null, null);
        MouseEvent me = new MouseEvent(f, 1, 1, mask, 1, 1, 1, 1, 1, true,
                                       MouseEvent.NOBUTTON);

        test(f, mwe);
        test(f, mdme);
        test(f, me);

        if (FAILED) {
            throw new RuntimeException("Wrong mouse event");
        }
    }


    private static void test(final Frame frame, final MouseEvent me) {
        MouseEvent newme = SwingUtilities.convertMouseEvent(frame, me, frame);
        if (me.getModifiersEx() != newme.getModifiersEx()
                || me.getModifiers() != newme.getModifiers()) {
            fail(me, newme);
        }
    }

    private static void fail(final MouseEvent exp, final MouseEvent act) {
        System.err.println("Expected: " + exp);
        System.err.println("Actual: " + act);
        FAILED = true;
    }
}
