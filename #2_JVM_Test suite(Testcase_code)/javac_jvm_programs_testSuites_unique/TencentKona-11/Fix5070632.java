



import javax.net.ssl.SSLSocketFactory;
import java.net.SocketException;
import javax.net.SocketFactory;
import java.security.*;

public class Fix5070632 {
    public static void main(String[] args) throws Exception {
        
        String reservedSFacProvider =
            Security.getProperty("ssl.SocketFactory.provider");

        
        

        Security.setProperty("ssl.SocketFactory.provider", "foo.NonExistant");
        SSLSocketFactory fac = (SSLSocketFactory)SSLSocketFactory.getDefault();
        try {
            fac.createSocket();
        } catch(SocketException se) {
            
            System.out.println("Throw SocketException");
            se.printStackTrace();
            return;
        } finally {
            
            if (reservedSFacProvider == null) {
                reservedSFacProvider = "";
            }
            Security.setProperty("ssl.SocketFactory.provider",
                                                reservedSFacProvider);
        }

        
        throw new Exception("should throw SocketException");
    }
}
