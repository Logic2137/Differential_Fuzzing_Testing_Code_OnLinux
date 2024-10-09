import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;

public class ReferenceEnqueue {

    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 5; i++) {
            new WeakRef().run();
            new ExplicitEnqueue().run();
        }
        System.out.println("Test passed.");
    }

    static class WeakRef {

        final ReferenceQueue<Object> queue = new ReferenceQueue<Object>();

        final Reference<Object> ref;

        final int iterations = 1000;

        WeakRef() {
            this.ref = new WeakReference<Object>(new Object(), queue);
        }

        void run() throws InterruptedException {
            System.gc();
            for (int i = 0; i < iterations; i++) {
                System.gc();
                if (ref.isEnqueued()) {
                    break;
                }
                Thread.sleep(100);
            }
            if (ref.isEnqueued() == false) {
                System.out.println("Reference not enqueued yet");
                return;
            }
            if (ref.enqueue() == true) {
                throw new RuntimeException("Error: enqueue() returned true;" + " expected false");
            }
            if (queue.poll() == null) {
                throw new RuntimeException("Error: poll() returned null;" + " expected ref object");
            }
        }
    }

    static class ExplicitEnqueue {

        final ReferenceQueue<Object> queue = new ReferenceQueue<>();

        final List<Reference<Object>> refs = new ArrayList<>();

        final int iterations = 1000;

        ExplicitEnqueue() {
            this.refs.add(new SoftReference<>(new Object(), queue));
            this.refs.add(new WeakReference<>(new Object(), queue));
        }

        void run() throws InterruptedException {
            for (Reference<Object> ref : refs) {
                if (ref.enqueue() == false) {
                    throw new RuntimeException("Error: enqueue failed");
                }
                if (ref.get() != null) {
                    throw new RuntimeException("Error: referent must be cleared");
                }
            }
            System.gc();
            for (int i = 0; refs.size() > 0 && i < iterations; i++) {
                Reference<Object> ref = (Reference<Object>) queue.poll();
                if (ref == null) {
                    System.gc();
                    Thread.sleep(100);
                    continue;
                }
                if (refs.remove(ref) == false) {
                    throw new RuntimeException("Error: unknown reference " + ref);
                }
            }
            if (!refs.isEmpty()) {
                throw new RuntimeException("Error: not all references are removed");
            }
        }
    }
}
