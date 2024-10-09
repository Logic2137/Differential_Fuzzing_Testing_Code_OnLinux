import java.lang.management.ManagementFactory;

public class TestMemoryMXBean {

    public static void main(String[] args) throws Exception {
        final long M = 1024 * 1024;
        final long expectedInitialCapacity = Long.parseLong(args[0]) * M;
        final long expectedMaxCapacity = Long.parseLong(args[1]) * M;
        final var memoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        final long initialCapacity = memoryUsage.getInit();
        final long capacity = memoryUsage.getCommitted();
        final long maxCapacity = memoryUsage.getMax();
        System.out.println("expectedInitialCapacity: " + expectedInitialCapacity);
        System.out.println("    expectedMaxCapacity: " + expectedMaxCapacity);
        System.out.println("        initialCapacity: " + initialCapacity);
        System.out.println("               capacity: " + capacity);
        System.out.println("            maxCapacity: " + maxCapacity);
        if (initialCapacity != expectedInitialCapacity) {
            throw new Exception("Unexpected initial capacity");
        }
        if (maxCapacity != expectedMaxCapacity) {
            throw new Exception("Unexpected max capacity");
        }
        if (capacity < initialCapacity || capacity > maxCapacity) {
            throw new Exception("Unexpected capacity");
        }
    }
}
