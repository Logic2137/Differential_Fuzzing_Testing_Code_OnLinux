

package provider;

import java.io.IOException;
import java.util.Map;

import javax.management.MBeanServer;

import javax.management.remote.JMXConnectorServerProvider;
import javax.management.remote.JMXConnectorServer;
import javax.management.remote.JMXServiceURL;

import javax.management.remote.rmi.RMIConnectorServer;
import javax.management.remote.JMXProviderException;

public class JMXConnectorServerProviderImpl
    implements JMXConnectorServerProvider {
    private static boolean called = false;

    public static boolean called() {
        return called;
    }

    public JMXConnectorServer newJMXConnectorServer(JMXServiceURL url,
                                                    Map<String,?> map,
                                                    MBeanServer mbeanServer)
        throws IOException {
        final String protocol = url.getProtocol();
        called = true;
        System.out.println("JMXConnectorServerProviderImpl called");
        if(protocol.equals("rmi"))
            return new RMIConnectorServer(url, map, mbeanServer);
        if(protocol.equals("throw-provider-exception"))
            throw new JMXProviderException("I have been asked to throw");

        throw new IllegalArgumentException("UNKNOWN PROTOCOL");
    }
}
