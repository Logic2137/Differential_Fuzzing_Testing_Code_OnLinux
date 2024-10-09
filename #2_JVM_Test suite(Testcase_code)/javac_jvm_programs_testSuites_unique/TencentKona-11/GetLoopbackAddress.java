


import java.net.*;

public class GetLoopbackAddress
{
    static InetAddress IPv4Loopback;
    static InetAddress IPv6Loopback;

    static {
        try {
            IPv4Loopback = InetAddress.getByAddress(
                new byte[] {0x7F,0x00,0x00,0x01});

            IPv6Loopback = InetAddress.getByAddress(
                new byte[] {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00,
                            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01});
        } catch (UnknownHostException e) {
        }
    }

    public static void main(String[] args) throws Exception {
        InetAddress addr = InetAddress.getLoopbackAddress();

        if (!(addr.equals(IPv4Loopback) || addr.equals(IPv6Loopback))) {
            throw new RuntimeException("Failed: getLoopbackAddress" +
                 " not returning a valid loopback address");
        }

        InetAddress addr2 = InetAddress.getLoopbackAddress();

        if (addr != addr2) {
            throw new RuntimeException("Failed: getLoopbackAddress" +
                " should return a reference to the same InetAddress loopback instance.");
        }

        InetAddress addrFromNullHost = InetAddress.getByName(null);
        if (!addrFromNullHost.isLoopbackAddress()) {
            throw new RuntimeException("getByName(null) did not return a" +
            " loopback address, but " + addrFromNullHost);
        }
        InetAddress addrFromEmptyHost = InetAddress.getByName("");
        if (!addrFromEmptyHost.isLoopbackAddress()) {
            throw new RuntimeException("getByName with a host of length == 0," +
                " did not return a loopback address, but " + addrFromEmptyHost);
        }

        InetAddress[] addrsByNull = InetAddress.getAllByName(null);
        if (!addrsByNull[0].isLoopbackAddress()) {
            throw new RuntimeException("getAllByName(null) did not return" +
            " a loopback address, but " + addrsByNull[0]);
        }
        InetAddress[] addrsByEmpty = InetAddress.getAllByName("");
        if (!addrsByEmpty[0].isLoopbackAddress()) {
            throw new RuntimeException("getAllByName with a host of length" +
            " == 0, did not return a loopback address, but " + addrsByEmpty[0]);
        }
    }
}
