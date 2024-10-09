import java.lang.management.BufferPoolMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.PlatformLoggingMXBean;
import java.lang.management.PlatformManagedObject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class GetObjectName {

    private static boolean failed = false;

    public static void main(String[] args) throws Exception {
        int tasks = 10000;
        ExecutorService executor = Executors.newFixedThreadPool(10);
        submitTasks(executor, tasks);
        executor.shutdown();
        executor.awaitTermination(10, TimeUnit.SECONDS);
        if (!failed) {
            System.out.println("Test passed.");
        }
    }

    static void submitTasks(ExecutorService executor, int count) {
        for (int i = 0; i < count && !failed; i++) {
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    List<PlatformManagedObject> mbeans = new ArrayList<>();
                    mbeans.add(ManagementFactory.getPlatformMXBean(PlatformLoggingMXBean.class));
                    mbeans.addAll(ManagementFactory.getPlatformMXBeans(BufferPoolMXBean.class));
                    for (PlatformManagedObject pmo : mbeans) {
                        if (pmo.getObjectName() == null) {
                            failed = true;
                            throw new RuntimeException("TEST FAILED: getObjectName() returns null");
                        }
                    }
                }
            });
        }
    }
}
