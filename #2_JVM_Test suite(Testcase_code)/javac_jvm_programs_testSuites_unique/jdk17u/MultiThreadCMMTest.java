import java.awt.image.DirectColorModel;

public final class MultiThreadCMMTest extends Thread {

    private static final int THREAD_COUNT = 100;

    private static final int ITERATION_COUNT = 20;

    private static volatile boolean failed = false;

    private static volatile Exception failureException = null;

    private static synchronized void setStatusFailed(Exception e) {
        if (!failed) {
            failureException = e;
            failed = true;
        }
    }

    public static void main(String[] args) throws Exception {
        Thread[] threadArray = new Thread[THREAD_COUNT];
        for (int i = 0; i < ITERATION_COUNT; i++) {
            for (int j = 0; j < threadArray.length; j++) {
                threadArray[j] = new MultiThreadCMMTest();
            }
            ;
            for (int j = 0; j < threadArray.length; j++) {
                threadArray[j].start();
            }
            for (int j = 0; j < threadArray.length; j++) {
                threadArray[j].join();
                if (failed) {
                    throw new RuntimeException(failureException);
                }
            }
        }
    }

    public void run() {
        int rMask16 = 0xF800;
        int gMask16 = 0x07C0;
        int bMask16 = 0x003E;
        int r;
        try {
            for (int i = 0; i < 1000; i++) {
                DirectColorModel dcm = new DirectColorModel(16, rMask16, gMask16, bMask16);
                r = dcm.getRed(10);
            }
        } catch (Exception e) {
            setStatusFailed(e);
        }
    }
}
