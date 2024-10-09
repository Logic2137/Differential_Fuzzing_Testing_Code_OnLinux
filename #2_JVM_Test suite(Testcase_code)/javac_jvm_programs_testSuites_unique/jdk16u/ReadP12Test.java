

import static java.lang.System.out;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Enumeration;



public class ReadP12Test {

    private final static String IN_KEYSTORE_TYPE = "pkcs12";
    private final static String IN_STORE_PASS = "pass";

    public static void main(String args[]) throws Exception {

        ReadP12Test jstest = new ReadP12Test();
        String testCase = "";
        try {
            testCase = "ReadP12_IE_Chain";
            jstest.readTest("ie_chain.pfx.data");

            testCase = "ReadP12_IE_Self";
            jstest.readTest("ie_self.pfx.data");

            testCase = "ReadP12_JDK_Chain";
            jstest.readTest("jdk_chain.p12.data");

            testCase = "ReadP12_JDK_Self";
            jstest.readTest("jdk_self.p12.data");

            testCase = "ReadP12_Mozilla_Chain";
            jstest.readTest("mozilla_chain.p12.data");

            testCase = "ReadP12_Mozilla_Self";
            jstest.readTest("mozilla_self.p12.data");

            testCase = "ReadP12_Mozilla_TwoEntries";
            jstest.readTest("mozilla_twoentries.p12.data");

            testCase = "ReadP12_Netscape_Chain";
            jstest.readTest("netscape_chain.p12.data");

            testCase = "ReadP12_Netscape_Self";
            jstest.readTest("netscape_self.p12.data");

            testCase = "ReadP12_Netscape_TwoEntries";
            jstest.readTest("netscape_twoentries.p12.data");

            testCase = "ReadP12_openssl";
            jstest.readTest("openssl.p12.data");

        } catch (Exception e) {
            System.err.println(testCase + ": failed with execption: "
                    + e.getMessage());
            throw e;

        }
        out.println(testCase + ": Pass!!");
    }

    private void readTest(String inKeyStore) throws Exception {

        KeyStore inputKeyStore;

        
        String dir = System.getProperty("test.src", ".");
        String keystorePath = dir + File.separator + "certs" + File.separator
                + "readP12";
        inputKeyStore = KeyStore.getInstance(IN_KEYSTORE_TYPE);
        
        
        byte[] input = Files.readAllBytes(Paths.get(keystorePath, inKeyStore));
        ByteArrayInputStream arrayIn = new ByteArrayInputStream(Base64
                .getMimeDecoder().decode(input));
        inputKeyStore.load(arrayIn, IN_STORE_PASS.toCharArray());
        out.println("Initialize KeyStore : " + inKeyStore + " success");

        out.println("getProvider : " + inputKeyStore.getProvider());
        out.println("getType : " + inputKeyStore.getType());
        out.println("getDefaultType : " + KeyStore.getDefaultType());

        int idx = 0;
        Enumeration<String> e = inputKeyStore.aliases();
        String alias;
        while (e.hasMoreElements()) {
            alias = e.nextElement();
            out.println("Alias " + idx + " : " + alias);
            if (inputKeyStore.containsAlias(alias) == false) {
                throw new RuntimeException("Alias not found");
            }

            out.println("getCreationDate : "
                    + inputKeyStore.getCreationDate(alias));

            X509Certificate cert = (X509Certificate) inputKeyStore
                    .getCertificate(alias);
            out.println("getCertificate : " + cert.getSubjectDN());
            String retAlias = inputKeyStore.getCertificateAlias(cert);
            if (!retAlias.equals(alias)) {
                throw new RuntimeException("Alias mismatch");
            }
            out.println("getCertificateAlias : " + retAlias);

            Certificate[] certs = inputKeyStore.getCertificateChain(alias);
            for (int i = 0; i < certs.length; i++) {
                out.println("getCertificateChain " + i + " : "
                        + ((X509Certificate) certs[i]).getSubjectDN());
            }

            boolean isCertEntry = inputKeyStore.isCertificateEntry(alias);
            
            if (isCertEntry == true) {
                throw new RuntimeException(
                        "inputKeystore should not be certEntry because test keystore only contain key pair entries.");
            }

            boolean isKeyEntry = inputKeyStore.isKeyEntry(alias);
            if (isKeyEntry) {
                Key key = inputKeyStore.getKey(alias,
                        IN_STORE_PASS.toCharArray());
                out.println("Key : " + key.toString());
            } else {
                throw new RuntimeException("Entry type unknown\n");
            }
            idx++;
        }

        int size = inputKeyStore.size();
        if (idx != size) {
            throw new RuntimeException("Size not match");
        }

    }
}
