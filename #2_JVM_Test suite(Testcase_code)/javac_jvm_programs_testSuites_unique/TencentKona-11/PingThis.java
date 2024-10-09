import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class PingThis {

    private static boolean hasIPv6() throws Exception {
        List<NetworkInterface> nics = Collections.list(NetworkInterface.getNetworkInterfaces());
        for (NetworkInterface nic : nics) {
            List<InetAddress> addrs = Collections.list(nic.getInetAddresses());
            for (InetAddress addr : addrs) {
                if (addr instanceof Inet6Address)
                    return true;
            }
        }
        return false;
    }

    public static void main(String[] args) throws Exception {
        if (System.getProperty("os.name").startsWith("Windows")) {
            return;
        }
        boolean preferIPv4Stack = "true".equals(System.getProperty("java.net.preferIPv4Stack"));
        List<String> addrs = new ArrayList<String>();
        InetAddress inetAddress = null;
        addrs.add("0.0.0.0");
        if (!preferIPv4Stack) {
            if (hasIPv6()) {
                addrs.add("::0");
            }
        }
        for (String addr : addrs) {
            inetAddress = InetAddress.getByName(addr);
            System.out.println("The target ip is " + inetAddress.getHostAddress());
            boolean isReachable = inetAddress.isReachable(3000);
            System.out.println("the target is reachable: " + isReachable);
            if (isReachable) {
                System.out.println("Test passed ");
            } else {
                System.out.println("Test failed ");
                throw new Exception("address " + inetAddress.getHostAddress() + " can not be reachable!");
            }
        }
    }
}
