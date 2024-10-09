





import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

public class textToNumericFormat {

    public static void main(String[] args) throws UnknownHostException {
        List<String> goodList = new ArrayList<>();
        List<String> badList = new ArrayList<>();
        String goodAddrs[] = {
                           "224.0.1.0",
                           "238.255.255.255",
                           "239.255.255.255",
                           "239.255.65535",
                           "239.16777215",
                           "4294967295" };

        String badAddrs[] = {
                           "238.255.255.2550",
                           "256.255.255.255",
                           "238.255.2550.255",
                           "238.2550.255.255",
                           "2380.255.255.255",
                           "239.255.65536",
                           "239.16777216",
                           "4294967296",
                           ".1.1.1",
                           "1..1.1",
                           "1.1.1.",
                           "..." };

        for (int i=0; i<goodAddrs.length; i++) {
            try {
                
                InetAddress ia = InetAddress.getByName(goodAddrs[i]);
            } catch (UnknownHostException e) {
                
                goodList.add(goodAddrs[i]);
            }

        }

        for (int i=0; i<badAddrs.length; i++) {
            try {
                
                InetAddress ia = InetAddress.getByName(badAddrs[i]);
                
                badList.add(badAddrs[i]);
            } catch (UnknownHostException e) {
                
            }

        }

        
        if (goodList.size() > 0 || badList.size() > 0) {
            throw new RuntimeException((goodList.size() > 0?
                                        ("Good address not parsed: "+ goodList)
                                        : "") +
                                       (badList.size() > 0 ?
                                        ("Bad Address parsed: "+ badList)
                                        : ""));
        }

    }

}
