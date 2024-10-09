

import java.io.FileInputStream;
import java.security.cert.CertificateFactory;

public class PemEncoding {
    public static void main(String[] args) throws Exception {
        try (FileInputStream fis = new FileInputStream(args[0])) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            System.out.println(cf.generateCertificate(fis));
        }
    }
}
