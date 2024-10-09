

package gc.z;



import java.util.ArrayList;

public class TestUncommit {
    private static final int delay = 10; 
    private static final int allocSize = 200 * 1024 * 1024; 
    private static final int smallObjectSize = 4 * 1024; 
    private static final int mediumObjectSize = 2 * 1024 * 1024; 
    private static final int largeObjectSize = allocSize;

    private static volatile ArrayList<byte[]> keepAlive;

    private static long capacity() {
        return Runtime.getRuntime().totalMemory();
    }

    private static void allocate(int objectSize) {
        keepAlive = new ArrayList<>();
        for (int i = 0; i < allocSize; i+= objectSize) {
            keepAlive.add(new byte[objectSize]);
        }
    }

    private static void reclaim() {
        keepAlive = null;
        System.gc();
    }

    private static void test(boolean enabled, int objectSize) throws Exception {
        final var beforeAlloc = capacity();

        
        allocate(objectSize);

        final var afterAlloc = capacity();

        
        reclaim();

        
        Thread.sleep(delay * 1000 / 2);

        final var beforeUncommit = capacity();

        
        Thread.sleep(delay * 1000);

        final var afterUncommit = capacity();

        System.out.println("  Uncommit Enabled: " + enabled);
        System.out.println("    Uncommit Delay: " + delay);
        System.out.println("       Object Size: " + objectSize);
        System.out.println("        Alloc Size: " + allocSize);
        System.out.println("      Before Alloc: " + beforeAlloc);
        System.out.println("       After Alloc: " + afterAlloc);
        System.out.println("   Before Uncommit: " + beforeUncommit);
        System.out.println("    After Uncommit: " + afterUncommit);
        System.out.println();

        
        if (enabled) {
            if (beforeUncommit == beforeAlloc) {
                throw new Exception("Uncommitted too fast");
            }

            if (afterUncommit >= afterAlloc) {
                throw new Exception("Uncommitted too slow");
            }

            if (afterUncommit < beforeAlloc) {
                throw new Exception("Uncommitted too much");
            }

            if (afterUncommit > beforeAlloc) {
                throw new Exception("Uncommitted too little");
            }
        } else {
            if (afterAlloc > beforeUncommit ||
                afterAlloc > afterUncommit) {
                throw new Exception("Should not uncommit");
            }
        }
    }

    public static void main(String[] args) throws Exception {
        final boolean enabled = Boolean.parseBoolean(args[0]);
        final int iterations = Integer.parseInt(args[1]);

        for (int i = 0; i < iterations; i++) {
            System.out.println("Iteration " + i);
            test(enabled, smallObjectSize);
            test(enabled, mediumObjectSize);
            test(enabled, largeObjectSize);
        }
    }
}
