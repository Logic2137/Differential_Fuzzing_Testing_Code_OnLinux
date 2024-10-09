

import java.net.InetAddress;
import java.rmi.AccessException;
import java.rmi.activation.ActivationSystem;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Set;




public class NonLocalActivationTest
{
    public static void main(String[] args) throws Exception {

        String host = System.getProperty("activation.host");
        if (host == null || host.isEmpty()) {
            throw new RuntimeException("Specify host with system property: -Dactivation.host=<host>");
        }

        
        String myHostName = InetAddress.getLocalHost().getHostName();
        Set<InetAddress> myAddrs = Set.of(InetAddress.getAllByName(myHostName));
        Set<InetAddress> hostAddrs = Set.of(InetAddress.getAllByName(host));
        if (hostAddrs.stream().anyMatch(i -> myAddrs.contains(i))
                || hostAddrs.stream().anyMatch(h -> h.isLoopbackAddress())) {
            throw new RuntimeException("Error: property 'activation.host' must not be the local host%n");
        }

        
        
        Registry registry = LocateRegistry.getRegistry(host, ActivationSystem.SYSTEM_PORT);
        try {
            
            registry.lookup("java.rmi.activation.ActivationSystem");
        } catch (Exception nf) {
            throw new RuntimeException("Not a ActivationSystem registry, does not contain java.rmi.activation.ActivationSystem", nf);
        }

        try {
            registry.bind("foo", null);
            throw new RuntimeException("Remote access should not succeed for method: bind");
        } catch (Exception e) {
            assertIsAccessException(e, "Registry.bind");
        }

        try {
            registry.rebind("foo", null);
            throw new RuntimeException("Remote access should not succeed for method: rebind");
        } catch (Exception e) {
            assertIsAccessException(e, "Registry.rebind");
        }

        try {
            registry.unbind("foo");
            throw new RuntimeException("Remote access should not succeed for method: unbind");
        } catch (Exception e) {
            assertIsAccessException(e, "Registry.unbind");
        }


        
        
        ActivationSystem as = (ActivationSystem) registry.lookup("java.rmi.activation.ActivationSystem");

        

        try {
            as.registerGroup(null);
        } catch (Exception aex) {
            assertIsAccessException(aex, "ActivationSystem.nonLocalAccess");
        }

        try {
            as.getActivationDesc(null);
        } catch (Exception aex) {
            assertIsAccessException(aex, "ActivationSystem.nonLocalAccess");
        }

        try {
            as.getActivationGroupDesc(null);
        } catch (Exception aex) {
            assertIsAccessException(aex, "ActivationSystem.nonLocalAccess");
        }

        try {
            as.registerObject(null);
        } catch (Exception aex) {
            assertIsAccessException(aex, "ActivationSystem.nonLocalAccess");
        }

        try {
            as.unregisterGroup(null);
        } catch (Exception aex) {
            assertIsAccessException(aex, "ActivationSystem.nonLocalAccess");
        }

        try {
            as.unregisterObject(null);
        } catch (Exception aex) {
            assertIsAccessException(aex, "ActivationSystem.nonLocalAccess");
        }

        try {
            as.setActivationDesc(null, null);
        } catch (Exception aex) {
            assertIsAccessException(aex, "ActivationSystem.nonLocalAccess");
        }

        try {
            as.setActivationGroupDesc(null, null);
        } catch (Exception aex) {
            assertIsAccessException(aex, "ActivationSystem.nonLocalAccess");
        }
    }

    
    private static void assertIsAccessException(Exception ex, String msg1) {
        Throwable t = ex;
        System.out.println();
        while (!(t instanceof AccessException) && t.getCause() != null) {
            t = t.getCause();
        }
        if (t instanceof AccessException) {
            String msg = t.getMessage();
            int asIndex = msg.indexOf(msg1);
            int disallowIndex = msg.indexOf("disallowed");
            int nonLocalHostIndex = msg.indexOf("non-local host");
            if (asIndex < 0 ||
                    disallowIndex < 0 ||
                    nonLocalHostIndex < 0 ) {
                throw new RuntimeException("exception message is malformed", t);
            }
            System.out.printf("Found expected AccessException: %s%n", t);
        } else {
            throw new RuntimeException("AccessException did not occur", ex);
        }
    }
}
