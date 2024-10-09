import com.sun.management.OperatingSystemMXBean;
import java.lang.management.ManagementFactory;

public class GetFreeSwapSpaceSize {

    public static void main(String[] args) {
        System.out.println("TestGetFreeSwapSpaceSize");
        OperatingSystemMXBean osBean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        for (int i = 0; i < 100; i++) {
            long size = osBean.getFreeSwapSpaceSize();
            if (size < 0) {
                System.out.println("Error: getFreeSwapSpaceSize returns " + size);
                System.exit(-1);
            }
        }
    }
}
