


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Security;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class CacheTest {


    public static void main(String args[]) throws Exception {

        
        String ttlProp = "networkaddress.cache.negative.ttl";
        int ttl = 0;
        String policy = Security.getProperty(ttlProp);
        if (policy != null) {
            ttl = Integer.parseInt(policy);
        }
        if (ttl <= 0  || ttl > 15) {
            System.err.println("Security property " + ttlProp + " needs to " +
                " in 1-15 second range to execute this test");
            return;

        }
        String hostsFileName = System.getProperty("jdk.net.hosts.file");

        

        
        addMappingToHostsFile("theclub", "129.156.220.219", hostsFileName, false);

        
        InetAddress.getByName("theclub");

        
        

        try {
            InetAddress.getByName("luster");
            throw new RuntimeException("Test internal error " +
                " - luster is being resolved by name service");
        } catch (UnknownHostException x) {
        }

        
        addMappingToHostsFile("luster", "10.5.18.21", hostsFileName, true);

        
        
        Thread.currentThread().sleep(ttl*1000 + 1000);
        InetAddress.getByName("luster");
    }

    private static void addMappingToHostsFile ( String host,
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
