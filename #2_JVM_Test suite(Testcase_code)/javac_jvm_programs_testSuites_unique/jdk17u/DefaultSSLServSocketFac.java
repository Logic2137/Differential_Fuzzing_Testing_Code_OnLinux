import java.security.Security;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class DefaultSSLServSocketFac {

    public static void main(String[] args) throws Exception {
        String reservedSSFacProvider = Security.getProperty("ssl.ServerSocketFactory.provider");
        try {
            Security.setProperty("ssl.ServerSocketFactory.provider", "oops");
            ServerSocketFactory ssocketFactory = SSLServerSocketFactory.getDefault();
            SSLServerSocket sslServerSocket = (SSLServerSocket) ssocketFactory.createServerSocket();
        } catch (Exception e) {
            if (!(e.getCause() instanceof ClassNotFoundException)) {
                throw e;
            }
        } finally {
            if (reservedSSFacProvider == null) {
                reservedSSFacProvider = "";
            }
            Security.setProperty("ssl.ServerSocketFactory.provider", reservedSSFacProvider);
        }
    }
}
