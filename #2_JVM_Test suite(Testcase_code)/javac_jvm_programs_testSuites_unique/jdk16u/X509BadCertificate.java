



import java.io.File;
import java.io.FileInputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.cert.CertificateException;

public class X509BadCertificate {

    public static void main(String[] args) throws Exception {
        test("bad-cert-1.pem");
        test("bad-cert-2.pem");
    }

    
    static void test(String filename) throws Exception {
        try {
            System.out.println("Parse file " + filename);
            File f = new File(System.getProperty("test.src", "."), filename);
            try (FileInputStream fis = new FileInputStream(f)) {
                CertificateFactory cf = CertificateFactory.getInstance("X509");
                X509Certificate cert = (X509Certificate)
                cf.generateCertificate(fis);
            }
            throw new Exception("Test failed: " +
                "expected CertificateParsingException was not thrown");
        } catch (CertificateException e) {
            System.out.println("Test passed: expected exception was thrown: " +
                e.toString());
        }
    }
}
