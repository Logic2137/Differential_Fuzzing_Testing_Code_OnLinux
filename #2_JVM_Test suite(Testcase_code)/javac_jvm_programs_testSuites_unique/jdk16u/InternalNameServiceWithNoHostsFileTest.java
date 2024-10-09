





import java.net.InetAddress;
import java.net.UnknownHostException;


public class InternalNameServiceWithNoHostsFileTest {
    public static void main(String args[]) throws Exception {

        String hostsFileName = System.getProperty("test.src", ".") + "/TestHosts-II";
        System.setProperty("jdk.net.hosts.file", hostsFileName);
        System.setProperty("sun.net.inetaddr.ttl", "0");
        InetAddress testAddress = null;

        try {
            testAddress = InetAddress.getByName("host.sample-domain");
            throw new RuntimeException ("UnknownHostException expected");
        } catch (UnknownHostException uhEx) {
            System.out.println("UHE caught as expected == " + uhEx.getMessage());
        }
    }
}
