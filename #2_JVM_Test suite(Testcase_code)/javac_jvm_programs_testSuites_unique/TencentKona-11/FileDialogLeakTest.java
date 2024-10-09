

import java.awt.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;



public class FileDialogLeakTest {
    static CountDownLatch latch = new CountDownLatch(3);
    static boolean passed;

    public static void main(String[] args) throws Exception {
        test();
        System.gc();
        System.runFinalization();
        latch.await(1, TimeUnit.SECONDS);
        if (!passed) {
            throw new RuntimeException("Test failed.");
        }
    }

    private static void test() throws Exception {
        FileDialog fd = new FileDialog((Frame) null) {
            @Override
            protected void finalize() throws Throwable {
                System.out.println("Finalize");
                super.finalize();
                passed = true;
                latch.countDown();
            }
        };

        new Thread(() -> {
            latch.countDown();
            fd.setVisible(true);
            latch.countDown();
        }).start();
        latch.await(1, TimeUnit.SECONDS);
        fd.dispose();
        latch.await(1, TimeUnit.SECONDS);
    }

}

