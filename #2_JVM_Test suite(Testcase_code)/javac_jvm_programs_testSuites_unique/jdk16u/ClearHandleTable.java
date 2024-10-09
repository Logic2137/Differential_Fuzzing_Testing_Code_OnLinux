



import java.io.*;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClearHandleTable {
    private static final int TIMES = 1000;

    public static void main(String[] args) throws Exception {
        final int nreps = 100;
        ObjectOutputStream oout =
            new ObjectOutputStream(new ByteArrayOutputStream());
        List<WeakReference<?>> refs = new ArrayList<>(nreps);

        for (int i = 0; i < nreps; i++) {
            String str = new String("blargh");
            oout.writeObject(str);
            refs.add(new WeakReference<Object>(str));
        }

        oout.reset();

        int count = 0;
        for (int i=0; i<TIMES; i++) {
            
            System.gc();

            Iterator<WeakReference<?>> itr = refs.iterator();
            while(itr.hasNext()) {
                WeakReference<?> ref = itr.next();
                if (ref.get() == null) {
                    itr.remove();
                }
            }
            if (refs.isEmpty())
                break;
            Thread.sleep(20);
            count++;
            if (count % 10 == 0)
                System.out.println("Looping " + count + " times");
        }

        if (!refs.isEmpty()) {
            throw new Error("failed to garbage collect object");
        }
    }
}
