



import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.lang.ProcessHandle;

public class ProcessIdTest {
    public static void main(String args[]) {
        RuntimeMXBean mbean = ManagementFactory.getRuntimeMXBean();
        long mbeanPid = mbean.getPid();
        long pid = ProcessHandle.current().pid();
        long pid1 = Long.parseLong(mbean.getName().split("@")[0]);
        if(mbeanPid != pid || mbeanPid != pid1) {
            throw new RuntimeException("Incorrect process ID returned");
        }

        System.out.println("Test Passed");
    }

}

