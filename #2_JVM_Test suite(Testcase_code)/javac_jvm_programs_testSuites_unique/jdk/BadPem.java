



import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.PrintStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Base64;

import sun.security.provider.X509Factory;
import java.security.cert.CertificateFactory;
import java.io.ByteArrayInputStream;

public class BadPem {

    public static void main(String[] args) throws Exception {
        String ks = System.getProperty("test.src", ".")
                + "/../../../../javax/net/ssl/etc/keystore";
        String pass = "passphrase";
        String alias = "dummy";

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(ks), pass.toCharArray());
        byte[] cert = keyStore.getCertificate(alias).getEncoded();

        
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        PrintStream pout = new PrintStream(bout);
        byte[] CRLF = new byte[] {'\r', '\n'};
        pout.println(X509Factory.BEGIN_CERT);
        for (int i=0; i<cert.length; i += 48) {
            int blockLen = (cert.length > i + 48) ? 48 : (cert.length - i);
            pout.println("!" + Base64.getEncoder()
                    .encodeToString(Arrays.copyOfRange(cert, i, i + blockLen)));
        }
        pout.println(X509Factory.END_CERT);

        try {
            cf.generateCertificate(new ByteArrayInputStream(bout.toByteArray()));
            throw new Exception("Should fail");
        } catch (CertificateException e) {
            
        }

        
        bout.reset();
        pout.println(X509Factory.BEGIN_CERT + "  ");
        pout.println(Base64.getMimeEncoder().encodeToString(cert));
        pout.println(X509Factory.END_CERT + "    ");

        cf.generateCertificate(new ByteArrayInputStream(bout.toByteArray()));
    }
}

