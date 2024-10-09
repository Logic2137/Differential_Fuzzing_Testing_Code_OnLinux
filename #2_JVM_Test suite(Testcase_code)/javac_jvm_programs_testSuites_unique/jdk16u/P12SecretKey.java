



import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class P12SecretKey {

    private static final String ALIAS = "alias";

    public static void main(String[] args) throws Exception {
        P12SecretKey testp12 = new P12SecretKey();
        String keystoreType = args[0];
        String algName = args[1];
        int keySize = Integer.parseInt(args[2]);
        testp12.run(keystoreType, algName, keySize);
    }

    private void run(String keystoreType, String algName, int keySize) throws Exception {
        char[] pw = "password".toCharArray();
        KeyStore ks = KeyStore.getInstance(keystoreType);
        ks.load(null, pw);

        KeyGenerator kg = KeyGenerator.getInstance(algName);
        kg.init(keySize);
        SecretKey key = kg.generateKey();

        KeyStore.SecretKeyEntry ske = new KeyStore.SecretKeyEntry(key);
        KeyStore.ProtectionParameter kspp = new KeyStore.PasswordProtection(pw);
        ks.setEntry(ALIAS, ske, kspp);

        File ksFile = File.createTempFile("test", ".test");
        try (FileOutputStream fos = new FileOutputStream(ksFile)) {
            ks.store(fos, pw);
            fos.flush();
        }

        
        try (FileInputStream fis = new FileInputStream(ksFile)) {
            KeyStore ks2 = KeyStore.getInstance(keystoreType);
            ks2.load(fis, pw);
            KeyStore.Entry entry = ks2.getEntry(ALIAS, kspp);
            SecretKey keyIn = ((KeyStore.SecretKeyEntry)entry).getSecretKey();
            if (Arrays.equals(key.getEncoded(), keyIn.getEncoded())) {
                System.err.println("OK: worked just fine with " + keystoreType +
                                   " keystore");
            } else {
                System.err.println("ERROR: keys are NOT equal after storing in "
                                   + keystoreType + " keystore");
            }
        }
    }
}
