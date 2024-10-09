
package gc.z;

public class TestNoUncommit {

    private static final int allocSize = 200 * 1024 * 1024;

    private static volatile Object keepAlive = null;

    private static long capacity() {
        return Runtime.getRuntime().totalMemory();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Allocating");
        keepAlive = new byte[allocSize];
        final var afterAlloc = capacity();
        System.out.println("Reclaiming");
        keepAlive = null;
        System.gc();
        Thread.sleep(5 * 1000);
        final var afterDelay = capacity();
        if (afterAlloc > afterDelay) {
            throw new Exception("Should not uncommit");
        }
        System.out.println("Success");
    }
}
