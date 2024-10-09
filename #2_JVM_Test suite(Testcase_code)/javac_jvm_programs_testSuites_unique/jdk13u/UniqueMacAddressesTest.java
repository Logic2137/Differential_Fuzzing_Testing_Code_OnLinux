import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class UniqueMacAddressesTest {

    public static void main(String[] args) throws Exception {
        new UniqueMacAddressesTest().execute();
        System.out.println("UniqueMacAddressesTest: OK");
    }

    public UniqueMacAddressesTest() {
        System.out.println("UniqueMacAddressesTest: start ");
    }

    public void execute() throws Exception {
        Enumeration<NetworkInterface> networkInterfaces;
        boolean areMacAddressesUnique = false;
        List<NetworkInterface> networkInterfaceList = new ArrayList<NetworkInterface>();
        networkInterfaces = NetworkInterface.getNetworkInterfaces();
        createNetworkInterfaceList(networkInterfaces, networkInterfaceList);
        areMacAddressesUnique = checkMacAddressesAreUnique(networkInterfaceList);
        if (!areMacAddressesUnique) {
            throw new RuntimeException("mac address uniqueness test failed");
        }
    }

    private boolean checkMacAddressesAreUnique(List<NetworkInterface> networkInterfaces) throws Exception {
        boolean uniqueMacAddresses = true;
        for (NetworkInterface networkInterface : networkInterfaces) {
            for (NetworkInterface comparisonNetIf : networkInterfaces) {
                System.out.println("Comparing netif " + networkInterface.getName() + " and netif " + comparisonNetIf.getName());
                if (testMacAddressesEqual(networkInterface, comparisonNetIf)) {
                    uniqueMacAddresses = false;
                    break;
                }
            }
            if (uniqueMacAddresses != true)
                break;
        }
        return uniqueMacAddresses;
    }

    private boolean testMacAddressesEqual(NetworkInterface netIf1, NetworkInterface netIf2) throws Exception {
        byte[] rawMacAddress1 = null;
        byte[] rawMacAddress2 = null;
        boolean macAddressesEqual = false;
        if (!netIf1.getName().equals(netIf2.getName())) {
            System.out.println("compare hardware addresses " + createMacAddressString(netIf1) + " and " + createMacAddressString(netIf2));
            rawMacAddress1 = netIf1.getHardwareAddress();
            rawMacAddress2 = netIf2.getHardwareAddress();
            macAddressesEqual = Arrays.equals(rawMacAddress1, rawMacAddress2);
        } else {
            macAddressesEqual = false;
        }
        return macAddressesEqual;
    }

    private String createMacAddressString(NetworkInterface netIf) throws Exception {
        byte[] macAddr = netIf.getHardwareAddress();
        StringBuilder sb = new StringBuilder();
        if (macAddr != null) {
            for (int i = 0; i < macAddr.length; i++) {
                sb.append(String.format("%02X%s", macAddr[i], (i < macAddr.length - 1) ? "-" : ""));
            }
        }
        return sb.toString();
    }

    private void createNetworkInterfaceList(Enumeration<NetworkInterface> nis, List<NetworkInterface> networkInterfaceList) throws Exception {
        byte[] macAddr = null;
        NetworkInterface netIf = null;
        while (nis.hasMoreElements()) {
            netIf = (NetworkInterface) nis.nextElement();
            if (netIf.isUp()) {
                macAddr = netIf.getHardwareAddress();
                if (macAddr != null) {
                    System.out.println("Adding NetworkInterface " + netIf.getName() + " with mac address " + createMacAddressString(netIf));
                    networkInterfaceList.add(netIf);
                }
            }
        }
    }
}
