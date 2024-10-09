import static java.lang.System.out;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class TestKeyStoreEntry {

    private static final char[] PASSWDK = new char[] { 't', 'e', 'r', 'c', 'e', 's' };

    private static final char[] PASSWDF = new String("guardian Angel").toCharArray();

    private static final String[] KS_ALGOS = { "DES", "DESede", "Blowfish" };

    private static final int NUM_ALGOS = KS_ALGOS.length;

    private static final String[] KS_TYPE = { "jks", "jceks", "pkcs12", "PKCS11KeyStore" };

    private static final String[] PRO_TYPE = { "SUN", "SunJCE", "SunJSSE", "SunPKCS11-Solaris" };

    private final SecretKey[] sks = new SecretKey[NUM_ALGOS];

    TestKeyStoreEntry() throws Exception {
        KeyGenerator[] kgs = new KeyGenerator[NUM_ALGOS];
        for (int i = 0; i < NUM_ALGOS; i++) {
            kgs[i] = KeyGenerator.getInstance(KS_ALGOS[i], "SunJCE");
            sks[i] = kgs[i].generateKey();
        }
    }

    public static void main(String[] args) throws Exception {
        TestKeyStoreEntry jstest = new TestKeyStoreEntry();
        jstest.run();
    }

    public void run() throws Exception {
        Provider[] providers = Security.getProviders();
        for (Provider p : providers) {
            String prvName = p.getName();
            if (prvName.startsWith("SunJCE") || prvName.startsWith("SunPKCS11-Solaris")) {
                try {
                    runTest(p);
                    out.println("Test with provider " + p.getName() + "" + " passed");
                } catch (java.security.KeyStoreException e) {
                    if (prvName.startsWith("SunPKCS11-Solaris")) {
                        out.println("KeyStoreException is expected because " + "PKCS11KeyStore is invalid keystore type.");
                        e.printStackTrace();
                    } else {
                        throw e;
                    }
                }
            }
        }
    }

    public void runTest(Provider p) throws Exception {
        try (FileOutputStream fos = new FileOutputStream("jceks");
            FileInputStream fis = new FileInputStream("jceks")) {
            KeyStore ks = KeyStore.getInstance("jceks", p);
            ks.load(null, null);
            String aliasHead = new String("secretKey");
            for (int j = 0; j < NUM_ALGOS; j++) {
                ks.setKeyEntry(aliasHead + j, sks[j], PASSWDK, null);
            }
            ks.store(fos, PASSWDF);
            for (int k = 0; k < NUM_ALGOS; k++) {
                ks.deleteEntry(aliasHead + k);
            }
            if (ks.size() != 0) {
                throw new RuntimeException("ERROR: re-initialization failed");
            }
            ks.load(fis, PASSWDF);
            Key temp = null;
            String alias = null;
            if (ks.size() != NUM_ALGOS) {
                throw new RuntimeException("ERROR: wrong number of key" + " entries");
            }
            for (int m = 0; m < ks.size(); m++) {
                alias = aliasHead + m;
                temp = ks.getKey(alias, PASSWDK);
                if (!temp.equals(sks[m])) {
                    throw new RuntimeException("ERROR: key comparison (" + m + ") failed");
                }
                if (ks.isCertificateEntry(alias) || !ks.isKeyEntry(alias)) {
                    throw new RuntimeException("ERROR: type identification (" + m + ") failed");
                }
            }
        }
    }
}
