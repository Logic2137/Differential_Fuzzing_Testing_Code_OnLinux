


import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

public class HumongousAlloc {

    public static byte[] dummy;
    private static int sleepFreq = 40;
    private static int sleepTime = 1000;
    private static double size = 0.75;
    private static int iterations = 50;
    private static int MB = 1024 * 1024;

    public static void allocate(int size, int sleepTime, int sleepFreq) throws InterruptedException {
        System.out.println("Will allocate objects of size: " + size
                + " bytes and sleep for " + sleepTime
                + " ms after every " + sleepFreq + "th allocation.");
        int count = 0;
        while (count < iterations) {
            for (int i = 0; i < sleepFreq; i++) {
                dummy = new byte[size - 16];
            }
            Thread.sleep(sleepTime);
            count++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        allocate((int) (size * MB), sleepTime, sleepFreq);
        List<GarbageCollectorMXBean> collectors = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean collector : collectors) {
            if (collector.getName().contains("G1 Old")) {
               long count = collector.getCollectionCount();
                if (count > 0) {
                    throw new RuntimeException("Failed: FullGCs should not have happened. The number of FullGC run is " + count);
                }
                else {
                    System.out.println("Passed.");
                }
            }
        }
    }
}

