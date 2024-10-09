
package provider;

import java.io.IOException;
import java.util.Map;

import javax.management.remote.JMXConnectorProvider;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXServiceURL;

import javax.management.remote.rmi.RMIConnector;
import javax.management.remote.JMXProviderException;

public class JMXConnectorProviderImpl implements JMXConnectorProvider {
    private static boolean called = false;
    public static boolean called() {
        return called;
    }

    public JMXConnector newJMXConnector(JMXServiceURL url,
                                        Map<String,?> map)
        throws IOException {
        final String protocol = url.getProtocol();
        called = true;
        System.out.println("JMXConnectorProviderImpl called");

        if(protocol.equals("rmi"))
            return new RMIConnector(url, map);
        if(protocol.equals("throw-provider-exception"))
            throw new JMXProviderException("I have been asked to throw");

        throw new IllegalArgumentException("UNKNOWN PROTOCOL");
    }
}
