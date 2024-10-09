



import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ForkJoinPool;

public class Uniprocessor {

    static volatile boolean done = false;

    public static void main(String[] args) throws InterruptedException {
        
        
        CountDownLatch ran = new CountDownLatch(1);
        ForkJoinPool.commonPool().submit(() -> ran.countDown());
        ran.await();
    }
}
