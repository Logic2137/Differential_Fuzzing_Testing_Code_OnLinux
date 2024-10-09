import java.lang.management.ManagementFactory;
import javax.management.Descriptor;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.*;
import javax.management.remote.*;

public class DcmdMBeanTest {

    private static String HOTSPOT_DIAGNOSTIC_MXBEAN_NAME = "com.sun.management:type=DiagnosticCommand";

    public static void main(String[] args) throws Exception {
        System.out.println("--->JRCMD MBean Test: invocation on \"operation info\"...");
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
            ObjectName name = new ObjectName(HOTSPOT_DIAGNOSTIC_MXBEAN_NAME);
            MBeanInfo info = mbsc.getMBeanInfo(name);
            System.out.println("Class Name:" + info.getClassName());
            System.out.println("Description:" + info.getDescription());
            MBeanOperationInfo[] opInfo = info.getOperations();
            System.out.println("Operations:");
            for (int i = 0; i < opInfo.length; i++) {
                printOperation(opInfo[i]);
                System.out.println("\n@@@@@@\n");
            }
        } finally {
            try {
                cc.close();
                cs.stop();
            } catch (Exception e) {
            }
        }
        System.out.println("Test passed");
    }

    static void printOperation(MBeanOperationInfo info) {
        System.out.println("Name: " + info.getName());
        System.out.println("Description: " + info.getDescription());
        System.out.println("Return Type: " + info.getReturnType());
        System.out.println("Impact: " + info.getImpact());
        Descriptor desc = info.getDescriptor();
        System.out.println("Descriptor");
        for (int i = 0; i < desc.getFieldNames().length; i++) {
            if (desc.getFieldNames()[i].compareTo("dcmd.arguments") == 0) {
                System.out.println("\t" + desc.getFieldNames()[i] + ":");
                Descriptor desc2 = (Descriptor) desc.getFieldValue(desc.getFieldNames()[i]);
                for (int j = 0; j < desc2.getFieldNames().length; j++) {
                    System.out.println("\t\t" + desc2.getFieldNames()[j] + "=");
                    Descriptor desc3 = (Descriptor) desc2.getFieldValue(desc2.getFieldNames()[j]);
                    for (int k = 0; k < desc3.getFieldNames().length; k++) {
                        System.out.println("\t\t\t" + desc3.getFieldNames()[k] + "=" + desc3.getFieldValue(desc3.getFieldNames()[k]));
                    }
                }
            } else {
                System.out.println("\t" + desc.getFieldNames()[i] + "=" + desc.getFieldValue(desc.getFieldNames()[i]));
            }
        }
    }
}
