



import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;

public class NetworkInterfaceEmptyGetInetAddressesTest {

    static int exceptionCount = 0;

    public static void testMethods(NetworkInterface netIf) throws Exception {
        testNetworkInterface_getInterfaceAddresses(netIf);

        if (exceptionCount > 0) {
            throw new RuntimeException("Unexpected Exceptions in test");
        }
    }

    private static void testNetworkInterface_getInterfaceAddresses(
            NetworkInterface netIf) {
        try {
            netIf.getInterfaceAddresses();
        } catch (Exception ex) {
            ex.printStackTrace();
            incrementExceptionCount();
        }
    }

    private static void incrementExceptionCount() {
        exceptionCount++;
    }

    public static void main(String[] args) throws Exception {
        MulticastSocket mcastSock = null;
        try {
            mcastSock = new MulticastSocket();
            System.out.println("macst socket address == "
                    + mcastSock.getLocalAddress());
            NetworkInterface netIf = mcastSock.getNetworkInterface();
            testMethods(netIf);
        } finally {
            if (mcastSock != null) {
                mcastSock.close();
            }
        }
    }
}
