



import java.io.IOException;
import java.net.*;
import java.nio.channels.DatagramChannel;
import java.util.Arrays;
import java.util.stream.IntStream;
import static java.lang.System.out;

public class PreferIPv6AddressesTest {

    
    static final String HOST_NAME = "www.google.com";

    static final InetAddress LOOPBACK = InetAddress.getLoopbackAddress();

    static final String preferIPV6Address =
            System.getProperty("java.net.preferIPv6Addresses", "false");

    public static void main(String args[]) throws IOException {

        InetAddress addrs[];
        try {
            addrs = InetAddress.getAllByName(HOST_NAME);
        } catch (UnknownHostException e) {
            out.println("Unknown host " + HOST_NAME + ", cannot run test.");
            return;
        }

        int firstIPv4Address = IntStream.range(0, addrs.length)
                .filter(x -> addrs[x] instanceof Inet4Address)
                .findFirst().orElse(-1);
        int firstIPv6Address = IntStream.range(0, addrs.length)
                .filter(x -> addrs[x] instanceof Inet6Address)
                .findFirst().orElse(-1);

        out.println("IPv6 supported: " + IPv6Supported());
        out.println("Addresses: " + Arrays.asList(addrs));

        if (preferIPV6Address.equalsIgnoreCase("true") && firstIPv6Address != -1) {
            int off = firstIPv4Address != -1 ? firstIPv4Address : addrs.length;
            assertAllv6Addresses(addrs, 0, off);
            assertAllv4Addresses(addrs, off, addrs.length);
            assertLoopbackAddress(Inet6Address.class);
            assertAnyLocalAddress(Inet6Address.class);
        } else if (preferIPV6Address.equalsIgnoreCase("false") && firstIPv4Address != -1) {
            int off = firstIPv6Address != -1 ? firstIPv6Address : addrs.length;
            assertAllv4Addresses(addrs, 0, off);
            assertAllv6Addresses(addrs, off, addrs.length);
            assertLoopbackAddress(Inet4Address.class);
            assertAnyLocalAddress(Inet4Address.class);
        } else if (preferIPV6Address.equalsIgnoreCase("system") && IPv6Supported()) {
            assertLoopbackAddress(Inet6Address.class);
            assertAnyLocalAddress(Inet6Address.class);
        } else if (preferIPV6Address.equalsIgnoreCase("system") && !IPv6Supported()) {
            assertLoopbackAddress(Inet4Address.class);
            assertAnyLocalAddress(Inet4Address.class);
        }
    }

    static void assertAllv4Addresses(InetAddress[] addrs, int off, int len) {
        IntStream.range(off, len)
                 .mapToObj(x -> addrs[x])
                 .forEach(x -> {
                     if (!(x instanceof Inet4Address))
                         throw new RuntimeException("Expected IPv4, got " + x);
                 });
    }

    static void assertAllv6Addresses(InetAddress[] addrs, int off, int len) {
        IntStream.range(off, len)
                .mapToObj(x -> addrs[x])
                .forEach(x -> {
                    if (!(x instanceof Inet6Address))
                        throw new RuntimeException("Expected IPv6, got " + x);
                });
    }

    static void assertLoopbackAddress(Class<?> expectedType) {
        if (!LOOPBACK.getClass().isAssignableFrom(expectedType))
            throw new RuntimeException("Expected " + expectedType
                    + ", got " + LOOPBACK.getClass());
    }

    static void assertAnyLocalAddress(Class<?> expectedType) {
        InetAddress anyAddr = (new InetSocketAddress(0)).getAddress();
        if (!anyAddr.getClass().isAssignableFrom(expectedType))
            throw new RuntimeException("Expected " + expectedType
                    + ", got " + anyAddr.getClass());
    }

    static boolean IPv6Supported() throws IOException {
        try {
            DatagramChannel.open(StandardProtocolFamily.INET6);
            return true;
        } catch (UnsupportedOperationException x) {
            return false;
        }
    }
}
