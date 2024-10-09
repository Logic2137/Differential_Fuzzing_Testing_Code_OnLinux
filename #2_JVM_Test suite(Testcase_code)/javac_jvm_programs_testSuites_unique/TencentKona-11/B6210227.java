



import java.util.*;
import java.net.*;

public class B6210227 {
    public static void main(String[] args) throws Exception
    {
        ServerSocket ss = new ServerSocket(0);
        int port = ss.getLocalPort();

        byte[] bad = {0,0,0,0};
        try {
            InetSocketAddress isa = new InetSocketAddress(InetAddress.getLocalHost(), port);
            Socket s = new Socket();
            s.connect( isa, 1000 );
            InetAddress iaLocal = s.getLocalAddress(); 
            String      sLocalHostname = iaLocal.getHostName();
            if (Arrays.equals (iaLocal.getAddress(), bad)) {
                throw new RuntimeException ("0.0.0.0 returned");
            }
            System.out.println("local hostname is "+sLocalHostname );
        } catch(Exception e) {
            System.out.println("Exception happened");
            throw e;
        } finally {
            ss.close();
        }
    }
}

