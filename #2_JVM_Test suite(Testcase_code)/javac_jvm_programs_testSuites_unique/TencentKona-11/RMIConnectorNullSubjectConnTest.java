

import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnector;



public class RMIConnectorNullSubjectConnTest {
    public static void main(String[] args) throws Exception {
        System.out.println("---RMIConnectorNullSubjectConnTest starting...");

        JMXConnectorServer connectorServer = null;
        JMXConnector connectorClient = null;

        try {
            MBeanServer mserver = ManagementFactory.getPlatformMBeanServer();
            JMXServiceURL serverURL = new JMXServiceURL("rmi", "localhost", 0);
            connectorServer = JMXConnectorServerFactory.newJMXConnectorServer(serverURL, null, mserver);
            connectorServer.start();

            JMXServiceURL serverAddr = connectorServer.getAddress();
            connectorClient = JMXConnectorFactory.connect(serverAddr, null);
            connectorClient.connect();

            Field nullSubjectConnField = RMIConnector.class.getDeclaredField("nullSubjectConnRef");
            nullSubjectConnField.setAccessible(true);

            WeakReference<MBeanServerConnection> weak =
                    (WeakReference<MBeanServerConnection>)nullSubjectConnField.get(connectorClient);

            if (weak != null && weak.get() != null) {
                throw new RuntimeException("nullSubjectConnRef must be null at initial time.");
            }

            MBeanServerConnection conn1 = connectorClient.getMBeanServerConnection(null);
            MBeanServerConnection conn2 = connectorClient.getMBeanServerConnection(null);
            if (conn1 == null) {
                throw new RuntimeException("A connection with null subject should not be null.");
            } else if (conn1 != conn2) {
                throw new RuntimeException("The 2 connections with null subject are not equal.");
            }

            conn1 = null;
            conn2 = null;
            int i = 1;
            do {
                System.gc();
                Thread.sleep(100);
                weak = (WeakReference<MBeanServerConnection>)nullSubjectConnField.get(connectorClient);
            } while ((weak != null && weak.get() != null) && i++ < 60);

            System.out.println("---GC times: " + i);

            if (weak != null && weak.get() != null) {
                throw new RuntimeException("Failed to clean RMIConnector's nullSubjectConn");
            } else {
                System.out.println("---RMIConnectorNullSubjectConnTest: PASSED!");
            }
        } finally {
            try {
                connectorClient.close();
                connectorServer.stop();
            } catch (Exception e) {
            }
        }
    }
}
