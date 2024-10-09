import java.net.InetAddress;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Set;

public class NonLocalJMXRemoteTest {

    public static void main(String[] args) throws Exception {
        String host = System.getProperty("jmx-registry.host");
        if (host == null || host.isEmpty()) {
            throw new RuntimeException("Specify host with system property: -Djmx-registry.host=<host>");
        }
        int port = Integer.getInteger("jmx-registry.port", -1);
        if (port <= 0) {
            throw new RuntimeException("Specify port with system property: -Djmx-registry.port=<port>");
        }
        String myHostName = InetAddress.getLocalHost().getHostName();
        Set<InetAddress> myAddrs = Set.of(InetAddress.getAllByName(myHostName));
        Set<InetAddress> hostAddrs = Set.of(InetAddress.getAllByName(host));
        if (hostAddrs.stream().anyMatch(i -> myAddrs.contains(i)) || hostAddrs.stream().anyMatch(h -> h.isLoopbackAddress())) {
            throw new RuntimeException("Error: property 'jmx-registry.host' must not be the local host%n");
        }
        Registry registry = LocateRegistry.getRegistry(host, port);
        try {
            registry.lookup("jmxrmi");
        } catch (NotBoundException nf) {
            throw new RuntimeException("Not a JMX registry, jmxrmi is not bound", nf);
        }
        try {
            registry.bind("foo", null);
            throw new RuntimeException("Remote access should not succeed for method: bind");
        } catch (Exception e) {
            assertIsAccessException(e);
        }
        try {
            registry.rebind("foo", null);
            throw new RuntimeException("Remote access should not succeed for method: rebind");
        } catch (Exception e) {
            assertIsAccessException(e);
        }
        try {
            registry.unbind("foo");
            throw new RuntimeException("Remote access should not succeed for method: unbind");
        } catch (Exception e) {
            assertIsAccessException(e);
        }
    }

    private static void assertIsAccessException(Throwable ex) {
        Throwable t = ex;
        while (!(t instanceof AccessException) && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof AccessException) {
            String msg = t.getMessage();
            int asIndex = msg.indexOf("Registry");
            int disallowIndex = msg.indexOf("disallowed");
            int nonLocalHostIndex = msg.indexOf("non-local host");
            if (asIndex < 0 || disallowIndex < 0 || nonLocalHostIndex < 0) {
                System.out.println("Exception message is " + msg);
                throw new RuntimeException("exception message is malformed", t);
            }
            System.out.printf("Found expected AccessException: %s%n%n", t);
        } else {
            throw new RuntimeException("AccessException did not occur when expected", ex);
        }
    }
}
