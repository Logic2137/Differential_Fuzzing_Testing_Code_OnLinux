

import java.lang.reflect.Field;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.NamingManager;

import com.sun.jndi.dns.DnsContext;



public class IPv6NameserverPlatformParsingTest {

    private static boolean foundIPv6 = false;

    public static void main(String[] args) {
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, com.sun.jndi.dns.DnsContextFactory.class.getName());

        String[] servers;
        try {
            Context ctx = NamingManager.getInitialContext(env);
            if (!com.sun.jndi.dns.DnsContextFactory.platformServersAvailable()) {
                throw new RuntimeException("FAIL: no platform servers available, test does not make sense");
            }
            DnsContext context = (DnsContext)ctx;
            servers = getServersFromContext(context);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
        for (String server: servers) {
            System.out.println("DEBUG: 'nameserver = " + server + "'");
            if (server.indexOf(':') >= 0 && server.indexOf('.') < 0) {
                System.out.println("DEBUG: ==> Found IPv6 address in servers list: " + server);
                foundIPv6 = true;
            }
        }
        try {
            new com.sun.jndi.dns.DnsClient(servers, 100, 1);
        } catch (NumberFormatException e) {
            throw new RuntimeException("FAIL: Tried to parse non-[]-encapsulated IPv6 address.", e);
        } catch (Exception e) {
            throw new RuntimeException("ERROR: Something unexpected happened.");
        }
        if (!foundIPv6) {
            
            
            throw new RuntimeException("ERROR: No IPv6 address returned from platform.");
        }
        System.out.println("PASS: Found IPv6 address and DnsClient parsed it correctly.");
    }

    private static String[] getServersFromContext(DnsContext context) {
        try {
            Field serversField = DnsContext.class.getDeclaredField("servers");
            serversField.setAccessible(true);
            return (String[])serversField.get(context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
