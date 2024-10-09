import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.lang.ref.WeakReference;

public class AwtListGarbageCollectionTest {

    public static void main(String[] args) {
        Frame frame = new Frame("List leak test");
        try {
            test(frame);
        } finally {
            frame.dispose();
        }
    }

    private static void test(Frame frame) {
        WeakReference<List> weakListRef = null;
        try {
            frame.setSize(300, 200);
            frame.setVisible(true);
            List strongListRef = new List();
            frame.add(strongListRef);
            strongListRef.setMultipleMode(true);
            frame.remove(strongListRef);
            weakListRef = new WeakReference<List>(strongListRef);
            strongListRef = null;
            String veryLongString = new String(new char[100]);
            while (true) {
                veryLongString += veryLongString;
            }
        } catch (OutOfMemoryError e) {
            if (weakListRef == null) {
                throw new RuntimeException("Weak list ref wasn't created");
            } else if (weakListRef.get() != null) {
                throw new RuntimeException("List wasn't garbage collected");
            }
        }
    }
}
