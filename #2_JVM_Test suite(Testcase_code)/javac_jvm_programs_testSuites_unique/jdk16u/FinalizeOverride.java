

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;



public class FinalizeOverride {
    
    private static AtomicInteger finalizedCount = new AtomicInteger();

    
    
    private static AtomicInteger finalizedSum = new AtomicInteger();
    private static volatile boolean privateFinalizeInvoked = false;

    public static void main(String[] argvs) throws IOException {
        patchPrivateFinalize();

        test(new Base(10), 10);
        test(new Subclass(20), 0);
        test(new SubSubclass(30), 30);
        test(new PublicFinalize(40), 40*100+40);
        test(new PrivateFinalize(50), 50);
        test(new NoOverride(60), 60);
    }

    static void test(Object o, int expected) {
        int count = finalizedCount.get();
        int sum = finalizedSum.get();
        privateFinalizeInvoked = false;

        
        o = null;
        while (finalizedCount.get() != (count+1)) {
            System.gc();
            System.runFinalization();
            
            
            
            
            
            
            
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                System.out.println("Main thread interrupted, continuing execution.");
            }
        }

        if (privateFinalizeInvoked) {
            throw new RuntimeException("private finalize method invoked");
        }
        if (finalizedCount.get() != (count+1)) {
            throw new RuntimeException("Unexpected count=" + finalizedCount +
                " expected=" + (count+1));
        }
        if (finalizedSum.get() != (sum+expected)) {
            throw new RuntimeException("Unexpected sum=" + finalizedSum +
                " prev=" + sum + " value=" + expected);
        }
    }

    static void patchPrivateFinalize() throws IOException {
        
        String testClasses = System.getProperty("test.classes", ".");
        Path p = Paths.get(testClasses, "FinalizeOverride$PrivateFinalize.class");
        byte[] bytes = Files.readAllBytes(p);
        int len = "f_nal_ze".length();
        for (int i=0; i < bytes.length-len; i++) {
            if (bytes[i] == 'f' &&
                bytes[i+1] == '_' &&
                bytes[i+2] == 'n' &&
                bytes[i+3] == 'a' &&
                bytes[i+4] == 'l' &&
                bytes[i+5] == '_' &&
                bytes[i+6] == 'z' &&
                bytes[i+7] == 'e')
            {
                
                bytes[i+1] = 'i';
                bytes[i+5] = 'i';
                break;
            }
        }
        Files.write(p, bytes);
    }

    static class Base {
        protected int value;
        Base(int v) {
            this.value = v;
        }
        int called() {
            finalizedSum.addAndGet(value);
            return value;
        }
        protected void finalize() {
            System.out.println("Base.finalize() sum += " + called());
            finalizedCount.incrementAndGet();
        }
    }
    static class PublicFinalize extends Base {
        PublicFinalize(int v) {
            super(v);
        }
        public void finalize() {
            finalizedSum.addAndGet(value * 100);
            System.out.println("PublicFinalize.finalize() sum += " + called() +
                "+"+value+"*100");
            finalizedCount.incrementAndGet();
        }
    }
    static class Subclass extends Base {
        Subclass(int v) {
            super(v);
        }
        protected void finalize() {
            
            System.out.println("Subclass.finalize() sum += 0");
            finalizedCount.incrementAndGet();
        }
    }
    static class SubSubclass extends Subclass {
        SubSubclass(int v) {
            super(v);
        }
        protected final void finalize() {
            finalizedSum.addAndGet(value);
            System.out.println("SubSubclass.finalize() sum +=" +value);
            finalizedCount.incrementAndGet();
        }
    }
    static class PrivateFinalize extends Base {
        PrivateFinalize(int v) {
            super(v);
        }
        private void f_nal_ze() {
            
            System.out.println("Error: private finalize invoked!!");
            privateFinalizeInvoked = true;
            finalizedCount.incrementAndGet();
        }
    }
    static class NoOverride extends PrivateFinalize {
        NoOverride(int v) {
            super(v);
        }
    }
}
