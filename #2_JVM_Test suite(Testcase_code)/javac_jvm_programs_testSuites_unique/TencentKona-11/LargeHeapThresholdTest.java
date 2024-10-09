




import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;


public class LargeHeapThresholdTest {

    final static long TWO_G = ((long) Integer.MAX_VALUE + 1); 

    public static void main(String[] args) {
        List<MemoryPoolMXBean> pools = ManagementFactory.getMemoryPoolMXBeans();
        boolean verified = false;
        for (MemoryPoolMXBean i : pools) {
            if ((i.getUsage().getMax() >= TWO_G)
                    && i.isUsageThresholdSupported()) {
                i.setUsageThreshold(TWO_G);
                if(i.getUsageThreshold() != TWO_G)
                    throw new RuntimeException("Usage threshold for"
                            + " pool '" + i.getName() + "' is " + i.getUsageThreshold()
                            + " and not equal to 2GB");
                verified = true;
            }
        }
        System.out.println("Ability to use big heap thresholds has "
                + (verified ? "" : "NOT ") + "been verified");
    }
}
