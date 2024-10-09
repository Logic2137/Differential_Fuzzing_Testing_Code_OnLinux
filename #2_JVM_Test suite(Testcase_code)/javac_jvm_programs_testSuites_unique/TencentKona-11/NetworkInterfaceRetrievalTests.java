



import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class NetworkInterfaceRetrievalTests {
    public static void main(String[] args) throws Exception {
        int checkFailureCount = 0;

        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            while (en.hasMoreElements()) {
                NetworkInterface ni = en.nextElement();
                Enumeration<InetAddress> addrs = ni.getInetAddresses();
                System.out.println("############ Checking network interface + "
                        + ni + " #############");
                while (addrs.hasMoreElements()) {
                    InetAddress addr = addrs.nextElement();
                    System.out.println("************ Checking address  + "
                            + addr + " *************");
                    NetworkInterface addrNetIf = NetworkInterface
                            .getByInetAddress(addr);
                    if (addrNetIf.equals(ni)) {
                        System.out.println("Retreived net if " + addrNetIf
                                + " equal to owning net if " + ni);
                    } else {
                        System.out.println("Retreived net if " + addrNetIf
                                + "NOT  equal to owning net if " + ni
                                + "***********");
                        checkFailureCount++;
                    }

                }
            }

        } catch (Exception ex) {

        }

        if (checkFailureCount > 0) {
            throw new RuntimeException(
                    "NetworkInterface lookup by address didn't match owner network interface");
        }
    }
}
