





import java.lang.ref.*;

public class TestRefprocSanity {

    static final long TARGET_MB = Long.getLong("target", 1_000); 
    static final int WINDOW = 1_000;

    static final Reference<MyObject>[] refs = new Reference[WINDOW];

    static Object sink;

    public static void main(String[] args) throws Exception {
        long count = TARGET_MB * 1024 * 1024 / 128;
        int rIdx = 0;

        ReferenceQueue rq = new ReferenceQueue();

        for (int c = 0; c < WINDOW; c++) {
            refs[c] = select(c, new MyObject(c), rq);
        }

        for (int c = 0; c < count; c++) {
            verifyRefAt(rIdx);
            refs[rIdx] = select(c, new MyObject(rIdx), rq);

            rIdx++;
            if (rIdx >= WINDOW) {
                rIdx = 0;
            }

            
            sink = new byte[100];

            
            while (rq.poll() != null);
        }
    }

    static Reference<MyObject> select(int v, MyObject ext, ReferenceQueue rq) {
        switch (v % 10) {
            case 0:  return new SoftReference<MyObject>(null);
            case 1:  return new SoftReference<MyObject>(null, rq);
            case 2:  return new SoftReference<MyObject>(ext);
            case 3:  return new SoftReference<MyObject>(ext, rq);
            case 4:  return new WeakReference<MyObject>(null);
            case 5:  return new WeakReference<MyObject>(null, rq);
            case 6:  return new WeakReference<MyObject>(ext);
            case 7:  return new WeakReference<MyObject>(ext, rq);
            case 8:  return new PhantomReference<MyObject>(null, rq);
            case 9:  return new PhantomReference<MyObject>(ext, rq);
            default: throw new IllegalStateException();
        }
    }

    static void verifyRefAt(int idx) {
        Reference<MyObject> ref = refs[idx];
        MyObject mo = ref.get();
        if (mo != null && mo.x != idx) {
            throw new IllegalStateException("Referent tag is incorrect: " + mo.x + ", should be " + idx);
        }
    }

    static class MyObject {
        final int x;

        public MyObject(int x) {
            this.x = x;
        }
    }

}
