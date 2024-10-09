

import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXPrincipal;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnector;
import javax.security.auth.Subject;



public class RMIConnectorInternalMapTest {
    public static void main(String[] args) throws Exception {
        System.out.println("---RMIConnectorInternalMapTest starting...");

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

            Field rmbscMapField = RMIConnector.class.getDeclaredField("rmbscMap");
            rmbscMapField.setAccessible(true);
            Map<Subject, WeakReference<MBeanServerConnection>> map =
                    (Map<Subject, WeakReference<MBeanServerConnection>>) rmbscMapField.get(connectorClient);
            if (map != null && !map.isEmpty()) { 
                throw new RuntimeException("RMIConnector's rmbscMap must be empty at the initial time.");
            }

            Subject delegationSubject =
                    new Subject(true,
                    Collections.singleton(new JMXPrincipal("delegate")),
                    Collections.EMPTY_SET,
                    Collections.EMPTY_SET);
            MBeanServerConnection mbsc1 =
                    connectorClient.getMBeanServerConnection(delegationSubject);
            MBeanServerConnection mbsc2 =
                    connectorClient.getMBeanServerConnection(delegationSubject);

            if (mbsc1 == null) {
                throw new RuntimeException("Got null connection.");
            }
            if (mbsc1 != mbsc2) {
                throw new RuntimeException("Not got same connection with a same subject.");
            }

            map = (Map<Subject, WeakReference<MBeanServerConnection>>) rmbscMapField.get(connectorClient);
            if (map == null || map.isEmpty()) { 
                throw new RuntimeException("RMIConnector's rmbscMap has wrong size "
                        + "after creating a delegated connection.");
            }

            delegationSubject = null;
            mbsc1 = null;
            mbsc2 = null;

            int i = 0;
            while (!map.isEmpty() && i++ < 60) {
                System.gc();
                Thread.sleep(100);
            }
            System.out.println("---GC times: " + i);

            if (!map.isEmpty()) {
                throw new RuntimeException("Failed to clean RMIConnector's rmbscMap");
            } else {
                System.out.println("---RMIConnectorInternalMapTest: PASSED!");
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
