



import java.nio.channels.*;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class SelectAndClose {
    static Selector selector;
    static volatile boolean awakened = false;
    static volatile boolean closed = false;

    public static void main(String[] args) throws Exception {
        selector = Selector.open();

        
        final CountDownLatch selectLatch = new CountDownLatch(1);
        new Thread(new Runnable() {
                public void run() {
                    try {
                        selectLatch.countDown();
                        selector.select();
                        awakened = true;
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                }
            }).start();

        
        selectLatch.await();
        Thread.sleep(2000);

        
        Thread closeThread = new Thread(new Runnable() {
                public void run() {
                    try {
                        selector.close();
                        closed = true;
                    } catch (IOException e) {
                        System.err.println(e);
                    }
                }
            });
        closeThread.start();

        
        closeThread.join();

        if (!awakened)
            selector.wakeup();

        
        if (!awakened)
            throw new RuntimeException("Select did not wake up");
        if (!closed)
            throw new RuntimeException("Selector did not close");
    }
}
