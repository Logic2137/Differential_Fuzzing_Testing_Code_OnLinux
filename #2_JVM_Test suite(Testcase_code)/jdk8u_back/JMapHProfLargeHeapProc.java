import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import sun.management.VMManagement;

public class JMapHProfLargeHeapProc {

    private static final List<byte[]> heapGarbage = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        buildLargeHeap(args);
        System.out.println("PID[" + getProcessId() + "]");
        System.in.read();
    }

    private static void buildLargeHeap(String[] args) {
        for (long i = 0; i < Integer.parseInt(args[0]); i++) {
            heapGarbage.add(new byte[1024]);
        }
    }

    public static int getProcessId() throws Exception {
        RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        Field jvm = runtime.getClass().getDeclaredField("jvm");
        jvm.setAccessible(true);
        VMManagement mgmt = (sun.management.VMManagement) jvm.get(runtime);
        Method pid_method = mgmt.getClass().getDeclaredMethod("getProcessId");
        pid_method.setAccessible(true);
        int pid = (Integer) pid_method.invoke(mgmt);
        return pid;
    }
}
