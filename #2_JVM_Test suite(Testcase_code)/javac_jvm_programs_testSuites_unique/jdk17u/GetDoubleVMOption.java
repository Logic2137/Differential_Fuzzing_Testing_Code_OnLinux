import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;
import java.lang.management.ManagementFactory;
import java.util.List;
import javax.management.MBeanServer;

public class GetDoubleVMOption {

    private static final String COMPILE_THRESHOLD_SCALING = "CompileThresholdScaling";

    private static final String EXPECTED_VALUE = "0.14";

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
        VMOption option = mbean.getVMOption(COMPILE_THRESHOLD_SCALING);
        if (!option.getValue().equalsIgnoreCase(EXPECTED_VALUE)) {
            throw new RuntimeException("Unexpected value: " + option.getValue() + " expected: " + EXPECTED_VALUE);
        }
    }
}
