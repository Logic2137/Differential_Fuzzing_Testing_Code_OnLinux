import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

public class ThreadStartTest {

    public static void main(String[] args) {
        ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
        mbean.getThreadInfo(Thread.currentThread().getId());
        for (int i = 0; i < 1000; i++) {
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                }
            });
            t.start();
        }
    }
}
