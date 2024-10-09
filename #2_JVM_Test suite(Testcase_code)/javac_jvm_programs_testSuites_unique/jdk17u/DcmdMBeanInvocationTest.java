import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.*;
import javax.management.remote.*;

public class DcmdMBeanInvocationTest {

    private static String HOTSPOT_DIAGNOSTIC_MXBEAN_NAME = "com.sun.management:type=DiagnosticCommand";

    public static void main(String[] args) throws Exception {
        System.out.println("--->JRCMD MBean Test: invocation on \"help -all\" ...");
        ObjectName name = new ObjectName(HOTSPOT_DIAGNOSTIC_MXBEAN_NAME);
        String[] helpArgs = { "-all" };
        Object[] dcmdArgs = { helpArgs };
        String[] signature = { String[].class.getName() };
        MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
        JMXServiceURL url = new JMXServiceURL("rmi", null, 0);
        JMXConnectorServer cs = null;
        JMXConnector cc = null;
        try {
            cs = JMXConnectorServerFactory.newJMXConnectorServer(url, null, mbs);
            cs.start();
            JMXServiceURL addr = cs.getAddress();
            cc = JMXConnectorFactory.connect(addr);
            MBeanServerConnection mbsc = cc.getMBeanServerConnection();
            String result = (String) mbsc.invoke(name, "help", dcmdArgs, signature);
            System.out.println(result);
        } finally {
            try {
                cc.close();
                cs.stop();
            } catch (Exception e) {
            }
        }
        System.out.println("Test passed");
    }
}
