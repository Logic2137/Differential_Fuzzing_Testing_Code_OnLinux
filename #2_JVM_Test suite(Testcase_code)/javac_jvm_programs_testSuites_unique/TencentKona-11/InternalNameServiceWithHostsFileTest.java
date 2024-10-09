



import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class InternalNameServiceWithHostsFileTest {
    public static void main(String args[]) throws Exception {

        
        String hostsFileName = System.getProperty("test.src", ".")
                + "/TestHosts-III";
        System.setProperty("jdk.net.hosts.file", hostsFileName);
        System.setProperty("sun.net.inetaddr.ttl", "0");

        
        byte[] expectedIpv6Address = { (byte) 0xfe, (byte) 0x80, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 1 };
        
        byte[] expectedIpv6LocalAddress = { (byte) 0xfe, (byte) 0x00, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
        
        byte[] expectedIpv4Address = { 10, 2, 3, 4 };
        
        byte[] expectedIpv6LocalhostAddress = { 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 1 };

        try {
            
            testHostsMapping(expectedIpv4Address, "testHost.testDomain");
            
            testHostsMapping(expectedIpv6LocalhostAddress, "ip6-localhost");
            
            testHostsMapping(expectedIpv6LocalAddress, "ip6-localnet");
            
            testHostsMapping(expectedIpv6Address, "link-local-host");

        } catch (UnknownHostException uhEx) {
            System.out.println("UHE unexpected caught == " + uhEx.getMessage());
        }
    }

    private static void testHostsMapping(byte[] expectedIpAddress, String hostName)
            throws UnknownHostException {
        InetAddress testAddress;
        byte[] rawIpAddress;
        testAddress = InetAddress.getByName(hostName);
        System.out
                .println("############################  InetAddress == "
                        + testAddress);

        rawIpAddress = testAddress.getAddress();
        if (!Arrays.equals(rawIpAddress, expectedIpAddress)) {
            System.out.println("retrieved address == "
                    + Arrays.toString(rawIpAddress)
                    + " not equal to expected address == "
                    + Arrays.toString(expectedIpAddress));
            throw new RuntimeException(
                    "retrieved address not equal to expected address");
        }
        System.out.println("retrieved address == "
                + Arrays.toString(rawIpAddress)
                + " equal to expected address == "
                + Arrays.toString(expectedIpAddress));
    }
}
