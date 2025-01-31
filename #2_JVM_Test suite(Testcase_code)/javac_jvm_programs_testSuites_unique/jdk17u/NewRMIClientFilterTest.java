import java.io.InvalidClassException;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.Map;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXConnectorServerFactory;
import javax.management.remote.JMXServiceURL;
import javax.management.remote.rmi.RMIConnectorServer;

public class NewRMIClientFilterTest {

    public static void main(String[] args) throws Exception {
        System.out.println("---NewRMIClientFilterTest-main: starting ...");
        String filter1 = java.lang.String.class.getName() + ";!*";
        String filter2 = java.lang.String.class.getName() + ";" + MyCredentials.class.getName() + ";!*";
        JMXServiceURL url = new JMXServiceURL("rmi", null, 0);
        JMXServiceURL serverUrl = null;
        Map<String, Object> env = new HashMap<>(1);
        JMXConnectorServer server = null;
        System.out.println("\n---NewRMIClientFilterTest-main: testing types = null");
        server = newServer(url, null);
        serverUrl = server.getAddress();
        doTest(serverUrl, null);
        doTest(serverUrl, new String[] { "toto", "titi" });
        doTest(serverUrl, new Object[] { new MyCredentials(), "toto" });
        server.stop();
        System.out.println("\n---NewRMIClientFilterTest-main: testing types = String[]");
        env.put(RMIConnectorServer.CREDENTIALS_FILTER_PATTERN, filter1);
        server = newServer(url, env);
        serverUrl = server.getAddress();
        doTest(serverUrl, null);
        doTest(serverUrl, new String[] { "toto", "titi" });
        try {
            doTest(serverUrl, new MyCredentials());
            throw new Error("Bad client is not refused!");
        } catch (Exception e) {
            isInvalidClassEx(e);
        } finally {
            server.stop();
        }
        System.out.println("\n---NewRMIClientFilterTest-main: testing user specific types = String, MyCredentials");
        env.put(RMIConnectorServer.CREDENTIALS_FILTER_PATTERN, filter2);
        server = newServer(url, env);
        serverUrl = server.getAddress();
        doTest(serverUrl, null);
        doTest(serverUrl, new String[] { "toto", "titi" });
        doTest(serverUrl, new MyCredentials[] { new MyCredentials(), (MyCredentials) null });
        try {
            doTest(serverUrl, new Object[] { "toto", new byte[3] });
            throw new Error("Bad client is not refused!");
        } catch (Exception e) {
            isInvalidClassEx(e);
        } finally {
            server.stop();
        }
        System.out.println("---NewRMIClientFilterTest-main PASSED!!!");
    }

    private static void doTest(JMXServiceURL serverAddr, Object credentials) throws Exception {
        System.out.println("---NewRMIClientFilterTest-test:\n\tserver address: " + serverAddr + "\n\tcredentials: " + credentials);
        Map<String, Object> env = new HashMap<>(1);
        env.put("jmx.remote.credentials", credentials);
        JMXConnector client = null;
        try {
            client = JMXConnectorFactory.connect(serverAddr, env);
            client.getMBeanServerConnection().getDefaultDomain();
        } finally {
            try {
                client.close();
            } catch (Exception e) {
            }
        }
        System.out.println("---NewRMIClientFilterTest-test: PASSED!");
    }

    private static JMXConnectorServer newServer(JMXServiceURL url, Map<String, Object> env) throws Exception {
        JMXConnectorServer server = JMXConnectorServerFactory.newJMXConnectorServer(url, env, ManagementFactory.getPlatformMBeanServer());
        server.start();
        return server;
    }

    private static class MyCredentials implements Serializable {
    }

    private static void isInvalidClassEx(Exception e) {
        Throwable cause = e;
        while (cause != null) {
            if (cause instanceof InvalidClassException) {
                System.out.println("---NewRMIClientFilterTest-InvalidClassException expected: " + cause);
                return;
            }
            cause = cause.getCause();
        }
        e.printStackTrace();
        throw new RuntimeException("Did not get expected InvalidClassException!");
    }
}
