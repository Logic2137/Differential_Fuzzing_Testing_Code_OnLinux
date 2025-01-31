import java.util.*;
import java.net.MalformedURLException;
import java.io.IOException;
import javax.management.*;
import javax.management.remote.*;

public class ReconnectTest {

    private static final String[] protocols = { "rmi", "iiop", "jmxmp" };

    private static final MBeanServer mbs = MBeanServerFactory.createMBeanServer();

    private static HashMap env = new HashMap(2);

    static {
        String timeout = "1000";
        env.put("jmx.remote.x.server.connection.timeout", timeout);
        env.put("jmx.remote.x.client.connection.check.period", timeout);
    }

    public static void main(String[] args) throws Exception {
        System.out.println(">>> test to reconnect.");
        boolean ok = true;
        for (int i = 0; i < protocols.length; i++) {
            try {
                if (!test(protocols[i])) {
                    System.out.println(">>> Test failed for " + protocols[i]);
                    ok = false;
                } else {
                    System.out.println(">>> Test successed for " + protocols[i]);
                }
            } catch (Exception e) {
                System.out.println(">>> Test failed for " + protocols[i]);
                e.printStackTrace(System.out);
                ok = false;
            }
        }
        if (ok) {
            System.out.println(">>> Test passed");
        } else {
            System.out.println(">>> TEST FAILED");
            System.exit(1);
        }
    }

    private static boolean test(String proto) throws Exception {
        System.out.println("\n\n>>> Test for protocol " + proto);
        JMXServiceURL u = null;
        JMXConnectorServer server = null;
        try {
            u = new JMXServiceURL(proto, null, 0);
            server = JMXConnectorServerFactory.newJMXConnectorServer(u, env, mbs);
        } catch (MalformedURLException e) {
            System.out.println("Skipping unsupported URL " + proto);
            return true;
        }
        server.start();
        u = server.getAddress();
        JMXConnector conn = JMXConnectorFactory.newJMXConnector(u, env);
        conn.connect();
        System.out.print("The default domain is ");
        System.out.println(conn.getMBeanServerConnection().getDefaultDomain());
        for (int i = 0; i < 3; i++) {
            System.out.println("************** Sleeping ...... " + i);
            Thread.sleep(2000);
            System.out.println("Sleep done.");
            System.out.println("The default domain is " + conn.getMBeanServerConnection().getDefaultDomain());
        }
        System.out.println("Close the client ...");
        conn.close();
        System.out.println("Close the server ...");
        server.stop();
        System.out.println("Bye bye.");
        return true;
    }
}
