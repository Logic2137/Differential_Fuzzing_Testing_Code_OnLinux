import java.util.concurrent.CountDownLatch;

public class Stop {

    public static void main(String[] args) throws Exception {
        final CountDownLatch ready = new CountDownLatch(1);
        final ThreadGroup group = new ThreadGroup("");
        final Thread second = new Thread(group, () -> {
            ready.countDown();
            while (true) {
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException shouldNotHappen) {
                }
            }
        });
        final Thread first = new Thread(group, () -> {
            try {
                ready.await();
            } catch (InterruptedException shouldNotHappen) {
            }
            group.stop();
        });
        first.start();
        second.start();
        second.join();
    }
}
