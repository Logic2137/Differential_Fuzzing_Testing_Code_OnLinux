import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;

public class SubNetworkInterfaceTest {

    public static void main(String[] args) throws SocketException, UnknownHostException {
        Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
        for (NetworkInterface netIf : Collections.list(nets)) {
            doReverseLookup(netIf);
        }
    }

    static void doReverseLookup(NetworkInterface netIf) throws SocketException, UnknownHostException {
        for (NetworkInterface subIf : Collections.list(netIf.getSubInterfaces())) {
            Enumeration<InetAddress> subInetAddresses = subIf.getInetAddresses();
            while (subInetAddresses != null && subInetAddresses.hasMoreElements()) {
                InetAddress inetAddress = subInetAddresses.nextElement();
                String reversalString = inetAddress.getHostAddress();
                InetAddress.getByName(reversalString);
            }
        }
    }
}
