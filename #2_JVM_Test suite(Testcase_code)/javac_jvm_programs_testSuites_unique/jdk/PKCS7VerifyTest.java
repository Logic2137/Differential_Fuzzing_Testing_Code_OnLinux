


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import sun.security.pkcs.PKCS7;
import sun.security.pkcs.SignerInfo;

public class PKCS7VerifyTest {

    static final String TESTSRC = System.getProperty("test.src", ".");
    static final String FS = File.separator;
    static final String FILEPATH = TESTSRC + FS + "jarsigner" + FS + "META-INF"
            + FS;

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            throw new RuntimeException("usage: java JarVerify <file1> <file2>");
        }

        Security.setProperty("jdk.jar.disabledAlgorithms", "");

        
        
        
        
        
        
        

        PKCS7 pkcs7;
        byte[] data;

        
        
        byte[] base64Bytes = Files.readAllBytes(Paths.get(FILEPATH + args[0]));
        pkcs7 = new PKCS7(new ByteArrayInputStream(
                Base64.getMimeDecoder().decode(base64Bytes)));
        if (args.length < 2) {
            data = null;
        } else {
            data = Files.readAllBytes(Paths.get(FILEPATH + args[1]));

        }

        SignerInfo[] signerInfos = pkcs7.verify(data);

        if (signerInfos == null) {
            throw new RuntimeException("no signers verify");
        }
        System.out.println("Verifying SignerInfos:");
        for (SignerInfo signerInfo : signerInfos) {
            System.out.println(signerInfo.toString());
        }

        X509Certificate certs[] = pkcs7.getCertificates();

        HashMap<String, X509Certificate> certTable = new HashMap(certs.length);
        for (X509Certificate cert : certs) {
            certTable.put(cert.getSubjectDN().toString(), cert);
        }

        
        for (Map.Entry<String, X509Certificate> entry : certTable.entrySet()) {

            X509Certificate cert = entry.getValue();
            X509Certificate issuerCert = certTable
                    .get(cert.getIssuerDN().toString());

            System.out.println("Subject: " + cert.getSubjectDN());
            if (issuerCert == null) {
                System.out.println("Issuer certificate not found");
            } else {
                System.out.println("Issuer:  " + cert.getIssuerDN());
                cert.verify(issuerCert.getPublicKey());
                System.out.println("Cert verifies.");
            }
            System.out.println();
        }
    }

}
