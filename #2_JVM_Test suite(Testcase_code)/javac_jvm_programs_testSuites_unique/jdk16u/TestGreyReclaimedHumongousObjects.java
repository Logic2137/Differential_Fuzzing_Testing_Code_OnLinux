

package gc.g1;














































import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import com.sun.management.HotSpotDiagnosticMXBean;
import java.lang.management.ManagementFactory;

public class TestGreyReclaimedHumongousObjects {

    static class NamedThreadFactory implements ThreadFactory {
       private int threadNum = 0;

       @Override
       public Thread newThread(Runnable r) {
         return new Thread(r, THREAD_NAME + (threadNum++));
       }
    }

    static class Runner extends Thread {
        private final Date startDate = new Date();
        private final int obj_size;
        private final Object[] old_garbage;
        private int old_index = 0;

        public Runner(int obj_size) {
            this.obj_size = obj_size;
            old_garbage = new Object[OLD_COUNT];
        }

        private void allocate_garbage() {
            byte[] garbage = new byte[obj_size];
            old_garbage[Math.abs(++old_index % OLD_COUNT)] = garbage;
        }

        @Override
        public void run() {
            try {
                while (!isInterrupted()) {
                    allocate_garbage();
                    Thread.sleep(0); 
                }
            } catch (InterruptedException e) {
                System.out.println("Aborted after "
                                   + (new Date().getTime() - startDate.getTime())
                                   + " ms");
                interrupt();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        HotSpotDiagnosticMXBean diagnostic =
                ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);

        System.out.println("Max memory= " + MAX_MEMORY + " bytes");

        int obj_size = 0;
        long seconds_to_run = 0;
        if (args.length != 2) {
            throw new RuntimeException("Object size argument must be supplied");
        } else {
            obj_size = Integer.parseInt(args[0]);
            seconds_to_run = Integer.parseInt(args[1]);
        }
        System.out.println("Objects size= " + obj_size + " bytes");
        System.out.println("Seconds to run=" + seconds_to_run);

        int region_size =
            Integer.parseInt(diagnostic.getVMOption("G1HeapRegionSize").getValue());
        if (obj_size < (region_size / 2)) {
            throw new RuntimeException("Object size " + obj_size +
                                       " is not humongous with region size " + region_size);
        }

        ExecutorService executor =
            Executors.newFixedThreadPool(THREAD_COUNT, new NamedThreadFactory());
        System.out.println("Starting " + THREAD_COUNT + " threads");

        for (int i = 0; i < THREAD_COUNT; i++) {
            executor.execute(new Runner(obj_size));
        }

        Thread.sleep(seconds_to_run * 1000);
        executor.shutdownNow();

        if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
            System.err.println("Thread pool did not terminate after 10 seconds after shutdown");
        }
    }

    private static final long MAX_MEMORY = Runtime.getRuntime().maxMemory();
    private static final int OLD_COUNT = 4;
    private static final int THREAD_COUNT = 12;
    private static final String THREAD_NAME = "TestGreyRH-";
}
