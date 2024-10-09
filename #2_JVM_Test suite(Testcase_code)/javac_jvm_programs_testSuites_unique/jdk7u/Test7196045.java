



import java.lang.management.ManagementFactory;
import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

public class Test7196045 {

    public static long duration = 1000 * 60 * 2;
    private static final String HOTSPOT_INTERNAL = "sun.management:type=HotspotInternal";

    public static void main(String[] args) {

        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        ObjectName objName= null;
        try {
            ObjectName hotspotInternal = new ObjectName(HOTSPOT_INTERNAL);
            try {
                server.registerMBean(new sun.management.HotspotInternal(), hotspotInternal);
            } catch (JMException e) {
                throw new RuntimeException("HotSpotWatcher: Failed to register the HotspotInternal MBean" + e);
            }
            objName= new ObjectName("sun.management:type=HotspotThreading");

        } catch (MalformedObjectNameException e1) {
            throw new RuntimeException("Bad object name" + e1);
        }

        long endTime = System.currentTimeMillis() + duration;
        long i = 0;
        while (true) {
            try {
                server.getAttribute(objName, "InternalThreadCpuTimes");
            } catch (Exception ex) {
                System.err.println("Exception while getting attribute: " + ex);
            }
            i++;
            if (i % 10000 == 0) {
                System.out.println("Successful iterations: " + i);
            }
            if (System.currentTimeMillis() > endTime) {
                break;
            }
        }
        System.out.println("PASSED.");
    }
}
