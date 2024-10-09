



import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class InternalNameServiceTest {

    static final String HOSTS_FILE_NAME = System.getProperty("jdk.net.hosts.file");

    public static void main(String args[]) throws Exception {
        testHostToIPAddressMappings(HOSTS_FILE_NAME);
        testIpAddressToHostNameMappings(HOSTS_FILE_NAME);
    }

    private static void testHostToIPAddressMappings(String hostsFileName)
            throws Exception, UnknownHostException {
        System.out.println(" TEST HOST TO  IP ADDRESS MAPPINGS ");
        InetAddress testAddress;
        byte[] retrievedIpAddr;
        byte[] expectedIpAddr1 = { 1, 2, 3, 4 };
        byte[] expectedIpAddr2 = { 5, 6, 7, 8 };
        byte[] expectedIpAddrIpv6_1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};

        
        
        
        
        
        
        
        

        
        addMappingToHostsFile("test hosts file for internal NameService ", "#", hostsFileName,
                false);
        addMappingToHostsFile("host.sample-domain", "1.2.3.4", hostsFileName,
                true);

        testAddress = InetAddress.getByName("host.sample-domain");
        retrievedIpAddr = testAddress.getAddress();
        if (!Arrays.equals(retrievedIpAddr, expectedIpAddr1)) {
            throw new RuntimeException(
                    "retrievedIpAddr not equal to expectedipAddr");
        }

        addMappingToHostsFile("host1.sample-domain", "5.6.7.8", hostsFileName,
                true);
        addMappingToHostsFile("host2.sample-domain", "1.2.3.4", hostsFileName,
                true);

        testAddress = InetAddress.getByName("host1.sample-domain");
        retrievedIpAddr = testAddress.getAddress();
        if (!Arrays.equals(retrievedIpAddr, expectedIpAddr2)) {
            throw new RuntimeException(
                    "retrievedIpAddr not equal to expectedIpAddr");
        }

        testAddress = InetAddress.getByName("host2.sample-domain");
        retrievedIpAddr = testAddress.getAddress();
        if (!Arrays.equals(retrievedIpAddr, expectedIpAddr1)) {
            throw new RuntimeException(
                    "retrievedIpAddr not equal to expectedIpAddr");
        }

        try {
            addMappingToHostsFile("host3.sample-domain", "", hostsFileName,
                    true);
            testAddress = InetAddress.getByName("host3.sample-domain");
            throw new RuntimeException(
                    "Expected UnknownHostException not thrown");
        } catch (UnknownHostException uhEx) {
            System.out.println("UnknownHostException as expected for host host3.sample-domain");
        }

        try {
            addMappingToHostsFile("host4.sample-domain", " ", hostsFileName,
                    true);
            testAddress = InetAddress.getByName("host4.sample-domain");
            throw new RuntimeException(
                    "Expected UnknownHostException not thrown");
        } catch (UnknownHostException uhEx) {
            System.out.println("UnknownHostException as expected for host host4.sample-domain");
        }

        try {
            addMappingToHostsFile("host5.sample-domain", "  ", hostsFileName,
                    true);
            testAddress = InetAddress.getByName("host4.sample-domain");
            throw new RuntimeException(
                    "Expected UnknownHostException not thrown");
        } catch (UnknownHostException uhEx) {
            System.out.println("UnknownHostException as expected for host host5.sample-domain");
        }

        

        
        addMappingToHostsFile("host-ipv6.sample-domain", "::1", hostsFileName,
                true);
        testAddress = InetAddress.getByName("host-ipv6.sample-domain");
        retrievedIpAddr = testAddress.getAddress();
        if (!Arrays.equals(retrievedIpAddr, expectedIpAddrIpv6_1)) {
            System.out.println("retrieved ipv6 addr == " + Arrays.toString(retrievedIpAddr));
            System.out.println("expected ipv6 addr == " + Arrays.toString(expectedIpAddrIpv6_1));
            throw new RuntimeException(
                    "retrieved IPV6 Addr not equal to expected IPV6 Addr");
        }
    }

    private static void testIpAddressToHostNameMappings(String hostsFileName)
            throws Exception {
        System.out.println(" TEST IP ADDRESS TO HOST MAPPINGS ");
        InetAddress testAddress;
        String retrievedHost;
        String expectedHost = "testHost.testDomain";

        byte[] testHostIpAddr = { 10, 2, 3, 4 };
        byte[] testHostIpAddr2 = { 10, 5, 6, 7 };
        byte[] testHostIpAddr3 = { 10, 8, 9, 10 };
        byte[] testHostIpAddr4 = { 10, 8, 9, 11 };

        
        addMappingToHostsFile("test hosts file for internal NameService ", "#", hostsFileName,
                false);
        addMappingToHostsFile("testHost.testDomain", "10.2.3.4", hostsFileName,
                true);

        testAddress = InetAddress.getByAddress(testHostIpAddr);
        System.out.println("*******   testAddress == " + testAddress);
        retrievedHost = testAddress.getHostName();
        if (!expectedHost.equals(retrievedHost)) {
            throw new RuntimeException(
                    "retrieved host name not equal to expected host name");
        }

        addMappingToHostsFile("testHost.testDomain", "10.5.6.7", hostsFileName,
                true);

        testAddress = InetAddress.getByAddress(testHostIpAddr2);
        System.out.println("*******   testAddress == " + testAddress);
        retrievedHost = testAddress.getHostName();
        System.out.println("*******   retrievedHost == " + retrievedHost);
        if (!expectedHost.equals(retrievedHost)) {
            throw new RuntimeException("retrieved host name " + retrievedHost
                    + " not equal to expected host name" + expectedHost);
        }

        testAddress = InetAddress.getByAddress(testHostIpAddr4);
        System.out.println("*******   testAddress == " + testAddress);
        if ("10.8.9.11".equalsIgnoreCase(testAddress.getCanonicalHostName())) {
            System.out.println("addr = " + addrToString(testHostIpAddr4)
                    + "  resolve to a host address as expected");
        } else {
            System.out.println("addr = " + addrToString(testHostIpAddr4)
                    + " does not resolve as expected, testAddress == " + testAddress.getCanonicalHostName());
            throw new RuntimeException("problem with resolving "
                    + addrToString(testHostIpAddr4));
        }

        try {
            addMappingToHostsFile("", "10.8.9.10", hostsFileName, true);
            testAddress = InetAddress.getByAddress(testHostIpAddr3);
            System.out.println("*******   testAddress == " + testAddress);
            retrievedHost = testAddress.getCanonicalHostName();
        } catch (Throwable t) {
            throw new RuntimeException("problem with resolving "
                    + addrToString(testHostIpAddr3));
        }

    }

    private static String addrToString(byte addr[]) {
        return Byte.toString(addr[0]) + "." + Byte.toString(addr[1]) + "."
                + Byte.toString(addr[2]) + "." + Byte.toString(addr[3]);
    }

    private static void addMappingToHostsFile( String host,
                                               String addr,
                                               String hostsFileName,
                                               boolean append)
                                               throws Exception {
        String mapping = addr + " " + host;
        try (PrintWriter hfPWriter = new PrintWriter(new BufferedWriter(
                new FileWriter(hostsFileName, append)))) {
            hfPWriter.println(mapping);
        }
    }

}
