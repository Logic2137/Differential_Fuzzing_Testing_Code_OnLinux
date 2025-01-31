

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class ClientConnection {

    public final String IANA_JDP_ADDRESS = "224.0.23.178";
    public final String IANA_JDP_PORT = "7095";
    public final String UNDEFINED_NAME = "TheVMwithNoName";

    public final int port;
    public final InetAddress address;
    public final int pauseInSeconds;
    public final String instanceName;

    public ClientConnection()
            throws UnknownHostException {

        String discoveryAddress = System.getProperty("com.sun.management.jdp.address", IANA_JDP_ADDRESS);
        address = InetAddress.getByName(discoveryAddress);

        String discoveryPort = System.getProperty("com.sun.management.jdp.port", IANA_JDP_PORT);
        port = Integer.parseInt(discoveryPort);

        String pause = System.getProperty("com.sun.management.jdp.pause", "1");
        pauseInSeconds = Integer.parseUnsignedInt(pause);

        instanceName = System.getProperty("com.sun.management.jdp.name", UNDEFINED_NAME);

    }

    public MulticastSocket connectWithTimeout(int msTimeOut) throws IOException {
        MulticastSocket socket = new MulticastSocket(port);
        socket.joinGroup(address);
        socket.setSoTimeout(msTimeOut);
        return socket;
    }
}
