



import java.lang.annotation.Retention;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

public class AnnotationTypeDeadlockTest {

    @Retention(RUNTIME)
    @AnnB
    public @interface AnnA {
    }

    @Retention(RUNTIME)
    @AnnA
    public @interface AnnB {
    }

    static class Task extends Thread {
        final CountDownLatch prepareLatch;
        final AtomicInteger goLatch;
        final Class<?> clazz;

        Task(CountDownLatch prepareLatch, AtomicInteger goLatch, Class<?> clazz) {
            super(clazz.getSimpleName());
            setDaemon(true); 
            this.prepareLatch = prepareLatch;
            this.goLatch = goLatch;
            this.clazz = clazz;
        }

        @Override
        public void run() {
            prepareLatch.countDown();  
            while (goLatch.get() > 0); 
            clazz.getDeclaredAnnotations();
        }
    }

    public static void main(String[] args) throws Exception {
        CountDownLatch prepareLatch = new CountDownLatch(2);
        AtomicInteger goLatch = new AtomicInteger(1);
        Task taskA = new Task(prepareLatch, goLatch, AnnA.class);
        Task taskB = new Task(prepareLatch, goLatch, AnnB.class);
        taskA.start();
        taskB.start();
        
        prepareLatch.await();
        
        goLatch.set(0);
        
        ThreadMXBean threadBean = ManagementFactory.getThreadMXBean();
        
        while (taskA.isAlive() || taskB.isAlive()) {
            
            taskA.join(500L);
            taskB.join(500L);
            
            long[] deadlockedIds = threadBean.findMonitorDeadlockedThreads();
            if (deadlockedIds != null && deadlockedIds.length > 0) {
                StringBuilder sb = new StringBuilder("deadlock detected:\n\n");
                for (ThreadInfo ti : threadBean.getThreadInfo(deadlockedIds, Integer.MAX_VALUE)) {
                    sb.append(ti);
                }
                throw new IllegalStateException(sb.toString());
            }
        }
    }
}
