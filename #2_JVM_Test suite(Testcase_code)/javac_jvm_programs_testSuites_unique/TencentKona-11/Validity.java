




import java.io.ByteArrayInputStream;
import java.security.cert.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class Validity {

    
    static String CACertStr =
        "-----BEGIN CERTIFICATE-----\n" +
        "MIIBvTCCASYCCQCQRiTo4lBCFjANBgkqhkiG9w0BAQUFADAjMRAwDgYDVQQLDAdU\n" +
        "ZXN0T3JnMQ8wDQYDVQQDDAZUZXN0Q0EwHhcNMTQwMjI2MjEzMzU1WhcNMjQwMjI2\n" +
        "MjEzMzU1WjAjMRAwDgYDVQQLDAdUZXN0T3JnMQ8wDQYDVQQDDAZUZXN0Q0EwgZ8w\n" +
        "DQYJKoZIhvcNAQEBBQADgY0AMIGJAoGBAOtKS4ZrsM3ansd61ZxitcrN0w184I+A\n" +
        "z0kyrSP1eMtlam+cC2U91NpTz11FYV4XUfBhqqxaXW043AWTUer8pS90Pt4sCrUX\n" +
        "COx1+QA1M3ZhbZ4sTM7XQ90JbGaBJ/sEza9mlQP7hQ2yQO/hATKbP6J5qvgG2sT2\n" +
        "S2WYjEgwNwmFAgMBAAEwDQYJKoZIhvcNAQEFBQADgYEAQ/CXEpnx2WY4LJtv4jwE\n" +
        "4jIVirur3pdzV5oBhPyqqHMsyhQBkukCfX7uD7L5wN1+xuM81DfANpIxlnUfybp5\n" +
        "CpjcmktLpmyK4kJ6XnSd2blbLOIpsr9x6FqxPxpVDlyw/ySHYrIG/GZdsLHgmzGn\n" +
        "B06jeYzH8OLf879VxAxSsPc=\n" +
        "-----END CERTIFICATE-----";

    
    static String EECertStr =
        "-----BEGIN CERTIFICATE-----\n" +
        "MIIBtjCCAR8CAQQwDQYJKoZIhvcNAQEFBQAwIzEQMA4GA1UECwwHVGVzdE9yZzEP\n" +
        "MA0GA1UEAwwGVGVzdENBMB4XDTE0MDIyNjIyNTUxMloXDTI1MDIyNTIyNTUxMlow\n" +
        "JDEQMA4GA1UECwwHVGVzdE9yZzEQMA4GA1UEAwwHVGVzdEVFMDCBnzANBgkqhkiG\n" +
        "9w0BAQEFAAOBjQAwgYkCgYEAt8xz9W3ruCTHjSOtTX6cxsUZ0nRP6EavEfzgcOYh\n" +
        "CXGA0gr+viSHq3c2vQBxiRny2hm5rLcqpPo+2OxZtw/ajxfyrV6d/r8YyQLBvyl3\n" +
        "xdCZdOkG1DCM1oFAQDaSRt9wN5Zm5kyg7uMig5Y4L45fP9Yee4x6Xyh36qYbsR89\n" +
        "rFMCAwEAATANBgkqhkiG9w0BAQUFAAOBgQDZrPqSo08va1m9TOWOztTuWilGdjK/\n" +
        "2Ed2WXg8utIpy6uAV+NaOYtHQ7ULQBVRNmwg9nKghbVbh+E/xpoihjl1x7OXass4\n" +
        "TbwXA5GKFIFpNtDvATQ/QQZoCuCzw1FW/mH0Q7UEQ/9/iJdDad6ebkapeMwtj/8B\n" +
        "s2IZV7s85CEOXw==\n" +
        "-----END CERTIFICATE-----";

    public static void main(String[] args) throws Exception {

        String[] certStrs = {EECertStr};
        String[] trustedCertStrs = {CACertStr};
        runTest(certStrs, trustedCertStrs);

        System.out.println("Test passed.");
    }

    private static void runTest(String[] certStrs,
                                String[] trustedCertStrs)
            throws Exception {

        CertificateFactory cf = CertificateFactory.getInstance("X509");

        
        ArrayList<X509Certificate> certs = new ArrayList<>();
        for (String certStr : certStrs) {
            certs.add(generateCert(certStr, cf));
        }
        CertPath cp = cf.generateCertPath(certs);

        
        
        Set<TrustAnchor> trustAnchors = new HashSet<>();
        for (String trustedCertStr : trustedCertStrs) {
            TrustAnchor ta = new TrustAnchor(generateCert(trustedCertStr, cf),
                                             null);
            trustAnchors.add(ta);
        }
        PKIXParameters params = new PKIXParameters(trustAnchors);
        params.setDate(new Date(114, 3, 1));   
        params.setRevocationEnabled(false);

        
        CertPathValidator cpv = CertPathValidator.getInstance("PKIX");
        cpv.validate(cp, params);
        System.out.println("CertPath validation successful.");
    }

    private static X509Certificate generateCert(String certStr,
                                                CertificateFactory cf)
            throws Exception {
        ByteArrayInputStream stream
                = new ByteArrayInputStream(certStr.getBytes());
        return (X509Certificate) cf.generateCertificate(stream);

    }
}
