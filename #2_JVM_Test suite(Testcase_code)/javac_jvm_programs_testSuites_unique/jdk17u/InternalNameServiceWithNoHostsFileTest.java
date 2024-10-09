import java.net.InetAddress;
import java.net.UnknownHostException;

public class InternalNameServiceWithNoHostsFileTest {

    public static void main(String[] args) throws Exception {
        InetAddress testAddress = null;
        try {
            testAddress = InetAddress.getByName("host.sample-domain");
            throw new RuntimeException("UnknownHostException expected");
        } catch (UnknownHostException uhEx) {
            System.out.println("UHE caught as expected == " + uhEx.getMessage());
        }
    }
}
