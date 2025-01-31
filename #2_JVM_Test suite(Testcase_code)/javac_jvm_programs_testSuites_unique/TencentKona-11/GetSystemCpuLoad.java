



import java.lang.management.*;
import com.sun.management.OperatingSystemMXBean;

public class GetSystemCpuLoad {
    public static void main(String[] argv) throws Exception {
        OperatingSystemMXBean mbean = (com.sun.management.OperatingSystemMXBean)
            ManagementFactory.getOperatingSystemMXBean();
        double load;
        for(int i=0; i<10; i++) {
            load = mbean.getSystemCpuLoad();
            if((load<0.0 || load>1.0) && load != -1.0) {
                throw new RuntimeException("getSystemCpuLoad() returns " + load
                       +  " which is not in the [0.0,1.0] interval");
            }
            try {
                Thread.sleep(200);
            } catch(InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
