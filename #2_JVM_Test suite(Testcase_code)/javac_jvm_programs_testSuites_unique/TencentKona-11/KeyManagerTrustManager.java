



import java.net.*;
import java.security.*;
import java.security.cert.*;
import java.security.spec.*;
import javax.net.ssl.*;

public class KeyManagerTrustManager implements X509KeyManager {

    public String[] getServerAliases(String keyType, Principal[] issuers) {
        return null;
    }
    public String[] getClientAliases(String keyType, Principal[] issuers) {
        return null;
    }
    public String chooseServerAlias(String keyType, Principal[] issuers,
            Socket socket) {
        return null;
    }
    public String chooseClientAlias(String [] keyType, Principal[] issuers,
            Socket socket) {
        return null;
    }
    public PrivateKey getPrivateKey(String alias) {
        return null;
    }
    public X509Certificate[] getCertificateChain(String alias) {
        return null;
    }

    public void doit(KeyManagerFactory kmf, TrustManagerFactory tmf,
            ManagerFactoryParameters mfp) throws Exception {
        kmf.init(mfp);
        tmf.init(mfp);
    }

    public static void main(String args[]) throws Exception {
        String kmfAlg = null;
        String tmfAlg = null;

        
        String reservedKMFacAlg =
            Security.getProperty("ssl.KeyManagerFactory.algorithm");
        String reservedTMFacAlg =
            Security.getProperty("ssl.TrustManagerFactory.algorithm");

        try {
            Security.setProperty("ssl.KeyManagerFactory.algorithm", "hello");
            Security.setProperty("ssl.TrustManagerFactory.algorithm",
                                                                "goodbye");

            kmfAlg = KeyManagerFactory.getDefaultAlgorithm();
            tmfAlg = TrustManagerFactory.getDefaultAlgorithm();

            if (!kmfAlg.equals("hello")) {
                throw new Exception("ssl.KeyManagerFactory.algorithm not set");
            }
            if (!tmfAlg.equals("goodbye")) {
                throw new Exception(
                        "ssl.TrustManagerFactory.algorithm not set");
            }
        } finally {
            
            if (reservedKMFacAlg == null) {
                reservedKMFacAlg = "";
            }

            if (reservedTMFacAlg == null) {
                reservedTMFacAlg = "";
            }
            Security.setProperty("ssl.KeyManagerFactory.algorithm",
                                                            reservedKMFacAlg);
            Security.setProperty("ssl.TrustManagerFactory.algorithm",
                                                            reservedTMFacAlg);
        }
    }
}
