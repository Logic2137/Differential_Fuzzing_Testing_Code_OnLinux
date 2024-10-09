
package gc.g1;

public class TestPeriodicCollectionJNI {

    static {
        System.loadLibrary("TestPeriodicCollectionJNI");
    }

    private static native boolean blockInNative(byte[] array);

    private static native void unblock();

    public static void block() {
        if (!blockInNative(new byte[0])) {
            throw new RuntimeException("failed to acquire lock to dummy object");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        long timeout = 2000;
        long startTime = System.currentTimeMillis();
        BlockInNative blocker = new BlockInNative();
        blocker.start();
        try {
            while (System.currentTimeMillis() < startTime + timeout) {
                System.out.println("Sleeping to let periodic GC trigger...");
                Thread.sleep(200);
            }
        } finally {
            unblock();
        }
    }
}

class BlockInNative extends Thread {

    public void run() {
        TestPeriodicCollectionJNI.block();
    }

    native void unlock();
}
