

import java.io.File;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;



public class CastError {
    public static void main(String[] args) throws Exception {
        KeyStore ks = KeyStore.getInstance(
                new File(System.getProperty("test.src"),
                        "../tools/jarsigner/JarSigning.keystore"),
                "bbbbbb".toCharArray());

        PrivateKey pk = (PrivateKey) ks.getKey("c", "bbbbbb".toCharArray());
        Certificate cert = ks.getCertificate("c");

        ks = KeyStore.getInstance("Windows-MY");
        ks.load(null, null);

        ks.setKeyEntry("8143913", pk, null, new Certificate[]{cert});
        ks.deleteEntry("8143913");
    }
}
