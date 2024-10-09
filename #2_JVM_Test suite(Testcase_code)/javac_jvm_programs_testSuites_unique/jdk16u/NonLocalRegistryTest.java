

import java.net.InetAddress;
import java.rmi.AccessException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Set;




public class NonLocalRegistryTest {

    public static void main(String[] args) throws Exception {

        String host = System.getProperty("registry.host");
        if (host == null || host.isEmpty()) {
            throw new RuntimeException("Specify host with system property: -Dregistry.host=<host>");
        }

        
        String myHostName = InetAddress.getLocalHost().getHostName();
        Set<InetAddress> myAddrs = Set.of(InetAddress.getAllByName(myHostName));
        Set<InetAddress> hostAddrs = Set.of(InetAddress.getAllByName(host));
        if (hostAddrs.stream().anyMatch(i -> myAddrs.contains(i))
                || hostAddrs.stream().anyMatch(h -> h.isLoopbackAddress())) {
            throw new RuntimeException("Error: property 'registry.host' must not be the local host%n");
        }

        Registry registry = LocateRegistry.getRegistry(host, Registry.REGISTRY_PORT);

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
            int rrIndex = msg.indexOf("Registry.Registry");     
            int disallowIndex = msg.indexOf("disallowed");
            int nonLocalHostIndex = msg.indexOf("non-local host");
            if (asIndex < 0 ||
                    rrIndex != -1 ||
                    disallowIndex < 0 ||
                    nonLocalHostIndex < 0 ) {
                throw new RuntimeException("exception message is malformed", t);
            }
            System.out.printf("Found expected AccessException: %s%n%n", t);
        } else {
            throw new RuntimeException("AccessException did not occur when expected", ex);
        }
    }
}
