import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;
import java.lang.management.ManagementFactory;
import java.util.List;
import javax.management.MBeanServer;

public class GetVMOption {

    private static final String HEAP_DUMP_ON_OOM = "HeapDumpOnOutOfMemoryError";

    private static final String EXPECTED_VALUE = "true";

    private static final String BAD_OPTION = "BadOption";

    private static final String HOTSPOT_DIAGNOSTIC_MXBEAN_NAME = "com.sun.management:type=HotSpotDiagnostic";

    public static void main(String[] args) throws Exception {
        List<HotSpotDiagnosticMXBean> list = ManagementFactory.getPlatformMXBeans(HotSpotDiagnosticMXBean.class);
        HotSpotDiagnosticMXBean mbean = list.get(0);
        checkVMOption(mbean);
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        mbean = ManagementFactory.newPlatformMXBeanProxy(mbs, HOTSPOT_DIAGNOSTIC_MXBEAN_NAME, HotSpotDiagnosticMXBean.class);
        checkVMOption(mbean);
    }

    private static void checkVMOption(HotSpotDiagnosticMXBean mbean) {
        VMOption option = mbean.getVMOption(HEAP_DUMP_ON_OOM);
        if (!option.getValue().equalsIgnoreCase(EXPECTED_VALUE)) {
            throw new RuntimeException("Unexpected value: " + option.getValue() + " expected: " + EXPECTED_VALUE);
        }
        boolean iae = false;
        try {
            mbean.getVMOption(BAD_OPTION);
        } catch (IllegalArgumentException e) {
            iae = true;
        }
        if (!iae) {
            throw new RuntimeException("Invalid VM Option" + " was not detected");
        }
    }
}
