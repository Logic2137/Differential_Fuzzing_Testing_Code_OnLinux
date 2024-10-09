

import java.awt.color.ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.color.ICC_ProfileRGB;
import java.util.concurrent.CountDownLatch;


public final class MTMatrixAccess {

    private static volatile boolean failed;

    public static void main(String[] args) throws Exception {
        test((ICC_ProfileRGB) ICC_Profile.getInstance(ColorSpace.CS_sRGB));
        test((ICC_ProfileRGB) ICC_Profile.getInstance(ColorSpace.CS_LINEAR_RGB));
    }

    private static void test(ICC_ProfileRGB rgb) throws InterruptedException {
        Thread[] threads = new Thread[100];
        CountDownLatch go = new CountDownLatch(1);
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(() -> {
                try {
                    go.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                try {
                    rgb.getMatrix();
                } catch (Throwable t) {
                    t.printStackTrace();
                    failed = true;
                }
            });
        }
        for (Thread thread : threads) {
            thread.start();
        }
        go.countDown();
        for (Thread thread : threads) {
            thread.join();
        }
        if (failed) {
            throw new RuntimeException();
        }
    }
}
