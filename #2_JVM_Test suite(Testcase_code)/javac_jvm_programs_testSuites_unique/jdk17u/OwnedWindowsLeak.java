import java.awt.Frame;
import java.awt.Window;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Vector;

public class OwnedWindowsLeak {

    public static void main(String[] args) throws Exception {
        Frame owner = new Frame("F");
        Vector<WeakReference<Window>> children = new Vector<WeakReference<Window>>();
        for (int i = 0; i < 1000; i++) {
            Window child = new Window(owner);
            child.setName("window_" + i);
            children.add(new WeakReference<Window>(child));
        }
        Vector garbage = new Vector();
        while (true) {
            try {
                garbage.add(new byte[1000]);
            } catch (OutOfMemoryError e) {
                break;
            }
        }
        garbage = null;
        for (WeakReference<Window> ref : children) {
            while (ref.get() != null) {
                System.out.println("ref.get() = " + ref.get());
                System.gc();
                Thread.sleep(1000);
            }
        }
        Field f = Window.class.getDeclaredField("ownedWindowList");
        f.setAccessible(true);
        Vector ownersChildren = (Vector) f.get(owner);
        while (ownersChildren.size() > 0) {
            System.out.println("ownersChildren = " + ownersChildren);
            System.gc();
            Thread.sleep(1000);
        }
        System.out.println("Test PASSED");
        owner.dispose();
    }
}
