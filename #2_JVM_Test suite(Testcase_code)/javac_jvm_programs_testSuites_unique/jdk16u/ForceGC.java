

package jdk.test.lib.util;

import java.lang.ref.Cleaner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;


public class ForceGC {
    private final CountDownLatch cleanerInvoked = new CountDownLatch(1);
    private final Cleaner cleaner = Cleaner.create();
    private Object o;

    public ForceGC() {
        this.o = new Object();
        cleaner.register(o, () -> cleanerInvoked.countDown());
    }

    private void doit(int iter) {
        try {
            for (int i = 0; i < 10; i++) {
                System.gc();
                System.out.println("doit() iter: " + iter + ", gc " + i);
                if (cleanerInvoked.await(1L, TimeUnit.SECONDS)) {
                    return;
                }
            }
        } catch (InterruptedException unexpected) {
            throw new AssertionError("unexpected InterruptedException");
        }
    }

    
    public boolean await(BooleanSupplier s) {
        o = null; 
                  
        for (int i = 0; i < 10; i++) {
            if (s.getAsBoolean()) return true;
            doit(i);
            try { Thread.sleep(1000); } catch (InterruptedException e) {
                throw new AssertionError("unexpected interrupted sleep", e);
            }
        }
        return false;
    }
}
