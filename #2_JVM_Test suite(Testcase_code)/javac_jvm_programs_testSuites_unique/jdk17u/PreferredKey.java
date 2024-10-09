import java.io.*;
import java.net.*;
import java.security.*;
import javax.net.ssl.*;

public class PreferredKey {

    static String pathToStores = "../../../../javax/net/ssl/etc";

    static String keyStoreFile = "keystore";

    static String passwd = "passphrase";

    public static void main(String[] args) throws Exception {
        Security.setProperty("jdk.certpath.disabledAlgorithms", "MD2, RSA keySize < 1024");
        Security.setProperty("jdk.tls.disabledAlgorithms", "SSLv3, RC4, DH keySize < 768");
        KeyStore ks;
        KeyManagerFactory kmf;
        X509KeyManager km;
        String keyFilename = System.getProperty("test.src", ".") + "/" + pathToStores + "/" + keyStoreFile;
        char[] password = passwd.toCharArray();
        ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keyFilename), password);
        kmf = KeyManagerFactory.getInstance("NewSunX509");
        kmf.init(ks, password);
        km = (X509KeyManager) kmf.getKeyManagers()[0];
        String[] aliases = km.getClientAliases("RSA", null);
        String alias = km.chooseClientAlias(new String[] { "RSA", "DSA" }, null, null);
        if (aliases != null || alias != null) {
            String algorithm = km.getPrivateKey(alias).getAlgorithm();
            if (!algorithm.equals("RSA") || !algorithm.equals(km.getPrivateKey(aliases[0]).getAlgorithm())) {
                throw new Exception("Failed to get the preferable key aliases");
            }
        }
        aliases = km.getClientAliases("DSA", null);
        alias = km.chooseClientAlias(new String[] { "DSA", "RSA" }, null, null);
        if (aliases != null || alias != null) {
            String algorithm = km.getPrivateKey(alias).getAlgorithm();
            if (!algorithm.equals("DSA") || !algorithm.equals(km.getPrivateKey(aliases[0]).getAlgorithm())) {
                throw new Exception("Failed to get the preferable key aliases");
            }
        }
    }
}
