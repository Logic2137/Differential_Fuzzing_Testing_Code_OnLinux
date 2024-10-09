import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class CheckOperatingSystemMXBean {

    public static void main(String[] args) {
        System.out.println("Checking OperatingSystemMXBean");
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        System.out.println(String.format("Runtime.availableProcessors: %d", Runtime.getRuntime().availableProcessors()));
        System.out.println(String.format("OperatingSystemMXBean.getAvailableProcessors: %d", osBean.getAvailableProcessors()));
        System.out.println(String.format("OperatingSystemMXBean.getTotalPhysicalMemorySize: %d", osBean.getTotalPhysicalMemorySize()));
        System.out.println(String.format("OperatingSystemMXBean.getFreePhysicalMemorySize: %d", osBean.getFreePhysicalMemorySize()));
        System.out.println(String.format("OperatingSystemMXBean.getTotalSwapSpaceSize: %d", osBean.getTotalSwapSpaceSize()));
        System.out.println(String.format("OperatingSystemMXBean.getFreeSwapSpaceSize: %d", osBean.getFreeSwapSpaceSize()));
        System.out.println(String.format("OperatingSystemMXBean.getSystemCpuLoad: %f", osBean.getSystemCpuLoad()));
    }
}
