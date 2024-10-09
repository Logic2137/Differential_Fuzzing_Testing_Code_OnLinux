import sun.management.ManagementFactoryHelper;
import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;

public class TestG1HeapRegionSize {

    public static void main(String[] args) {
        HotSpotDiagnosticMXBean diagnostic = ManagementFactoryHelper.getDiagnosticMXBean();
        String expectedValue = getExpectedValue(args);
        VMOption option = diagnostic.getVMOption("UseG1GC");
        if (option.getValue().equals("false")) {
            System.out.println("Skipping this test. It is only a G1 test.");
            return;
        }
        option = diagnostic.getVMOption("G1HeapRegionSize");
        if (!expectedValue.equals(option.getValue())) {
            throw new RuntimeException("Wrong value for G1HeapRegionSize. Expected " + expectedValue + " but got " + option.getValue());
        }
    }

    private static String getExpectedValue(String[] args) {
        if (args.length != 1) {
            throw new RuntimeException("Wrong number of arguments. Expected 1 but got " + args.length);
        }
        return args[0];
    }
}
