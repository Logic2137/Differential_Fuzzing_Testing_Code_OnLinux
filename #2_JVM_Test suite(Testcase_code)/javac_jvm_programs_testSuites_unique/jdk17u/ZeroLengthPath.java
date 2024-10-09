import java.io.ByteArrayInputStream;
import java.security.cert.*;
import java.util.Collections;

public class ZeroLengthPath {

    private static final String ANCHOR = "-----BEGIN CERTIFICATE-----\n" + "MIIBFzCBwgIBATANBgkqhkiG9w0BAQQFADAXMRUwEwYDVQQDEwxUcnVzdCBBbmNo\n" + "b3IwHhcNMDIxMTA3MTE1NzAzWhcNMjIxMTA3MTE1NzAzWjAXMRUwEwYDVQQDEwxU\n" + "cnVzdCBBbmNob3IwXDANBgkqhkiG9w0BAQEFAANLADBIAkEA9uCj12hwDgC1n9go\n" + "0ozQAVMM+DfX0vpKOemyGNp+ycSLfAq3pxBcUKbQhjSRL7YjPkEL8XC6pRLwyEoF\n" + "osWweQIDAQABMA0GCSqGSIb3DQEBBAUAA0EAzZta5M1qbbozj7jWnNyTgB4HUpzv\n" + "4eP0VYQb1pQY1/xEMczaRt+RuoIDnHCq5a1vOiwk6ZbdG6GlJKx9lj0oMQ==\n" + "-----END CERTIFICATE-----";

    public static void main(String[] args) throws Exception {
        ByteArrayInputStream is = new ByteArrayInputStream(ANCHOR.getBytes());
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
        X509CertSelector xcs = new X509CertSelector();
        xcs.setSubject(cert.getSubjectX500Principal().getName());
        PKIXBuilderParameters p = new PKIXBuilderParameters(Collections.singleton(new TrustAnchor(cert, null)), xcs);
        CertPathBuilder cpb = CertPathBuilder.getInstance("PKIX");
        CertPath cp = buildCertPath(cpb, p);
        validateCertPath(cp, p);
    }

    private static CertPath buildCertPath(CertPathBuilder cpb, PKIXBuilderParameters params) throws Exception {
        CertPathBuilderResult res = cpb.build(params);
        if (res.getCertPath().getCertificates().size() != 0) {
            throw new Exception("built path is not zero-length");
        }
        return res.getCertPath();
    }

    private static void validateCertPath(CertPath cp, PKIXParameters params) throws Exception {
        CertPathValidator cpv = CertPathValidator.getInstance("PKIX");
        CertPathValidatorResult cpvr = cpv.validate(cp, params);
    }
}
