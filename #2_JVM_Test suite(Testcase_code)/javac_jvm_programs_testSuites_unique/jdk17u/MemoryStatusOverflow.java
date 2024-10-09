import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.lang.management.ManagementFactory;
import com.sun.management.OperatingSystemMXBean;

public class MemoryStatusOverflow {

    static final long MEMORYSTATUS_OVERFLOW = (1L << 32) - 1;

    public static void main(String... args) throws Exception {
        OperatingSystemMXBean bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        List<String> failedGetterNames = new ArrayList<String>();
        List<String> testedGetterNames = Arrays.asList("getTotalSwapSpaceSize", "getFreeSwapSpaceSize", "getTotalPhysicalMemorySize", "getFreePhysicalMemorySize");
        for (String getterName : testedGetterNames) {
            Method getter = OperatingSystemMXBean.class.getMethod(getterName);
            long value = (Long) getter.invoke(bean);
            if (value == MEMORYSTATUS_OVERFLOW) {
                failedGetterNames.add(getterName);
            }
        }
        if (!failedGetterNames.isEmpty()) {
            throw new AssertionError(failedGetterNames);
        }
        System.out.println("Test passed.");
    }
}
