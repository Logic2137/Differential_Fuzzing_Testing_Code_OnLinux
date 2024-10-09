




import java.net.*;
import java.util.*;


public class IPv4Only {
    public static void main(String[] args) throws Exception {
        Enumeration<NetworkInterface> nifs = NetworkInterface.getNetworkInterfaces();
        while (nifs.hasMoreElements()) {
            NetworkInterface nif = nifs.nextElement();
            Enumeration<InetAddress> addrs = nif.getInetAddresses();
            while (addrs.hasMoreElements()) {
               InetAddress hostAddr = addrs.nextElement();
               if ( hostAddr instanceof Inet6Address ){
                    throw new RuntimeException( "NetworkInterfaceV6List failed - found v6 address " + hostAddr.getHostAddress() );
               }
            }
        }
    }
}
