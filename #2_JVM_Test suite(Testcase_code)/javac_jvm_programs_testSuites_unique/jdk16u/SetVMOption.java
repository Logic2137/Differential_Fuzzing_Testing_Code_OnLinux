



import java.lang.management.ManagementFactory;
import java.util.*;
import com.sun.management.HotSpotDiagnosticMXBean;
import com.sun.management.VMOption;
import com.sun.management.VMOption.Origin;

public class SetVMOption {
    private static final String HEAP_DUMP_ON_OOM = "HeapDumpOnOutOfMemoryError";
    private static final String EXPECTED_VALUE = "true";
    private static final String BAD_VALUE = "yes";
    private static final String NEW_VALUE = "false";
    private static final String MANAGEMENT_SERVER = "ManagementServer";
    private static HotSpotDiagnosticMXBean mbean;

    public static void main(String[] args) throws Exception {
        mbean =
            ManagementFactory.getPlatformMXBean(HotSpotDiagnosticMXBean.class);

        VMOption option = findHeapDumpOnOomOption();
        if (!option.getValue().equalsIgnoreCase(EXPECTED_VALUE)) {
            throw new RuntimeException("Unexpected value: " +
                option.getValue() + " expected: " + EXPECTED_VALUE);
        }
        if (option.getOrigin() != Origin.VM_CREATION) {
            throw new RuntimeException("Unexpected origin: " +
                option.getOrigin() + " expected: VM_CREATION");
        }
        if (!option.isWriteable()) {
            throw new RuntimeException("Expected " + HEAP_DUMP_ON_OOM +
                " to be writeable");
        }

        
        mbean.setVMOption(HEAP_DUMP_ON_OOM, NEW_VALUE);

        option = findHeapDumpOnOomOption();
        if (!option.getValue().equalsIgnoreCase(NEW_VALUE)) {
            throw new RuntimeException("Unexpected value: " +
                option.getValue() + " expected: " + NEW_VALUE);
        }
        if (option.getOrigin() != Origin.MANAGEMENT) {
            throw new RuntimeException("Unexpected origin: " +
                option.getOrigin() + " expected: MANAGEMENT");
        }
        VMOption o = mbean.getVMOption(HEAP_DUMP_ON_OOM);
        if (!option.getValue().equals(o.getValue())) {
            throw new RuntimeException("Unmatched value: " +
                option.getValue() + " expected: " + o.getValue());
        }
        if (!option.getValue().equals(o.getValue())) {
            throw new RuntimeException("Unmatched value: " +
                option.getValue() + " expected: " + o.getValue());
        }
        if (option.getOrigin() != o.getOrigin()) {
            throw new RuntimeException("Unmatched origin: " +
                option.getOrigin() + " expected: " + o.getOrigin());
        }
        if (option.isWriteable() != o.isWriteable()) {
            throw new RuntimeException("Unmatched writeable: " +
                option.isWriteable() + " expected: " + o.isWriteable());
        }

        
        List<VMOption> options = mbean.getDiagnosticOptions();
        VMOption mgmtServerOption = null;
        for (VMOption o1 : options) {
            if (o1.getName().equals(MANAGEMENT_SERVER)) {
                 mgmtServerOption = o1;
                 break;
            }
        }
        if (mgmtServerOption != null) {
            throw new RuntimeException(MANAGEMENT_SERVER +
                " is not expected to be writeable");
        }
        mgmtServerOption = mbean.getVMOption(MANAGEMENT_SERVER);
        if (mgmtServerOption == null) {
            throw new RuntimeException(MANAGEMENT_SERVER +
                " should exist.");
        }
        if (mgmtServerOption.getOrigin() != Origin.DEFAULT) {
            throw new RuntimeException(MANAGEMENT_SERVER +
                " should have the default value.");
        }
        if (mgmtServerOption.isWriteable()) {
            throw new RuntimeException(MANAGEMENT_SERVER +
                " is not expected to be writeable");
        }
    }

    public static VMOption findHeapDumpOnOomOption() {
        List<VMOption> options = mbean.getDiagnosticOptions();
        VMOption gcDetails = null;
        for (VMOption o : options) {
            if (o.getName().equals(HEAP_DUMP_ON_OOM)) {
                 gcDetails = o;
                 break;
            }
        }
        if (gcDetails == null) {
            throw new RuntimeException("VM option " + HEAP_DUMP_ON_OOM +
                " not found");
        }
        return gcDetails;
    }
}
