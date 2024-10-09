import java.net.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import static java.lang.System.out;

public class IndexTest {

    static final boolean isWindows = System.getProperty("os.name").startsWith("Windows");

    public static void main(String[] args) throws Exception {
        Enumeration<NetworkInterface> netifs = NetworkInterface.getNetworkInterfaces();
        NetworkInterface nif;
        while (netifs.hasMoreElements()) {
            nif = netifs.nextElement();
            String dName = nif.getDisplayName();
            if (isWindows && dName != null && dName.contains("Teredo"))
                continue;
            int index = nif.getIndex();
            if (index >= 0) {
                NetworkInterface nif2 = NetworkInterface.getByIndex(index);
                if (!nif.equals(nif2)) {
                    out.printf("%nExpected interfaces to be the same, but got:%n");
                    displayInterfaceInformation(nif);
                    displayInterfaceInformation(nif2);
                    throw new RuntimeException("both interfaces should be equal");
                }
            }
        }
        try {
            nif = NetworkInterface.getByIndex(-1);
            out.printf("%ngetByIndex(-1) should have thrown, but instead returned:%n");
            displayInterfaceInformation(nif);
            throw new RuntimeException("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        nif = NetworkInterface.getByIndex(Integer.MAX_VALUE - 1);
        if (nif != null) {
            out.printf("%ngetByIndex(MAX_VALUE - 1), expected null, got:%n");
            displayInterfaceInformation(nif);
            throw new RuntimeException("getByIndex() should have returned null");
        }
    }

    static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
        out.printf("Display name: %s%n", netint.getDisplayName());
        out.printf("Name: %s%n", netint.getName());
        Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();
        for (InetAddress inetAddress : Collections.list(inetAddresses)) out.printf("InetAddress: %s%n", inetAddress);
        out.printf("Up? %s%n", netint.isUp());
        out.printf("Loopback? %s%n", netint.isLoopback());
        out.printf("PointToPoint? %s%n", netint.isPointToPoint());
        out.printf("Supports multicast? %s%n", netint.supportsMulticast());
        out.printf("Virtual? %s%n", netint.isVirtual());
        out.printf("Hardware address: %s%n", Arrays.toString(netint.getHardwareAddress()));
        out.printf("MTU: %s%n", netint.getMTU());
        out.printf("Index: %s%n", netint.getIndex());
        out.printf("%n");
    }
}
