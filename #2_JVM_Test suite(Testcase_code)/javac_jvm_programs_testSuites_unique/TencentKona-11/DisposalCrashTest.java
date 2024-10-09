



import static java.awt.color.ColorSpace.*;
import java.awt.color.ICC_Profile;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.Vector;

public class DisposalCrashTest {

    static final ReferenceQueue<ICC_Profile> queue = new ReferenceQueue<>();
    static final Vector<Reference<? extends ICC_Profile>> v = new Vector<>();

    public static void main(String[] args) {
        int[] ids = new int[]{
            CS_sRGB, CS_CIEXYZ, CS_GRAY, CS_LINEAR_RGB, CS_PYCC
        };

        for (int id : ids) {
            ICC_Profile p = getCopyOf(id);
        }

        while (!v.isEmpty()) {
            System.gc();
            System.out.println(".");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {};

            final Reference<? extends ICC_Profile> ref = queue.poll();
            System.out.println("Got reference: " + ref);

            v.remove(ref);
        }

        System.out.println("Test PASSED.");
    }

    private static ICC_Profile getCopyOf(int id) {
        ICC_Profile std = ICC_Profile.getInstance(id);

        byte[] data = std.getData();

        ICC_Profile p = ICC_Profile.getInstance(data);

        WeakReference<ICC_Profile> ref = new WeakReference<>(p, queue);

        v.add(ref);

        return p;
    }
}
