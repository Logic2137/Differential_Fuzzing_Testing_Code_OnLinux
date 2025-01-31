import java.io.*;
import java.net.SocketException;
import java.util.*;
import java.security.Security;
import java.security.cert.*;
import java.security.cert.CertPathValidatorException.BasicReason;

public class CircularCRLTwoLevelRevoked {

    static String selfSignedCertStr = "-----BEGIN CERTIFICATE-----\n" + "MIICPjCCAaegAwIBAgIBADANBgkqhkiG9w0BAQQFADAfMQswCQYDVQQGEwJVUzEQ\n" + "MA4GA1UEChMHRXhhbXBsZTAeFw0wOTA0MjcwMjI0MzJaFw0zMDA0MDcwMjI0MzJa\n" + "MB8xCzAJBgNVBAYTAlVTMRAwDgYDVQQKEwdFeGFtcGxlMIGfMA0GCSqGSIb3DQEB\n" + "AQUAA4GNADCBiQKBgQC4OTag24sTxL2tXTNuvpmUEtdxrYAZoFsslFQ60T+WD9wQ\n" + "Jeiw87FSPsR2vxRuv0j8DNm2a4h7LNNIFcLurfNldbz5pvgZ7VqdbbUMPE9qP85n\n" + "jgDl4woyRTSUeRI4A7O0CO6NpES21dtbdhroWQrEkHxpnrDPxsxrz5gf2m3gqwID\n" + "AQABo4GJMIGGMB0GA1UdDgQWBBSCJd0hpl5PdAD9IZS+Hzng4lXLGzBHBgNVHSME\n" + "QDA+gBSCJd0hpl5PdAD9IZS+Hzng4lXLG6EjpCEwHzELMAkGA1UEBhMCVVMxEDAO\n" + "BgNVBAoTB0V4YW1wbGWCAQAwDwYDVR0TAQH/BAUwAwEB/zALBgNVHQ8EBAMCAgQw\n" + "DQYJKoZIhvcNAQEEBQADgYEAluy6HIjWcq009lTLmhp+Np6dxU78pInBK8RZkza0\n" + "484qGaxFGD3UGyZkI5uWmsH2XuMbuox5khfIq6781gmkPBHXBIEtJN8eLusOHEye\n" + "iE8h7WI+N3qa6Pj56WionMrioqC/3X+b06o147bbhx8U0vkYv/HyPaITOFfMXTdz\n" + "Vjw=\n" + "-----END CERTIFICATE-----";

    static String subCaCertStr = "-----BEGIN CERTIFICATE-----\n" + "MIICUDCCAbmgAwIBAgIBAzANBgkqhkiG9w0BAQQFADAfMQswCQYDVQQGEwJVUzEQ\n" + "MA4GA1UEChMHRXhhbXBsZTAeFw0wOTA0MjcwMjI0MzRaFw0yOTAxMTIwMjI0MzRa\n" + "MDExCzAJBgNVBAYTAlVTMRAwDgYDVQQKEwdFeGFtcGxlMRAwDgYDVQQLEwdDbGFz\n" + "cy0xMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCiAJnAQW2ad3ZMKUhSJVZj\n" + "8pBqxTcHSTwAVguQkDglsN/OIwUpvR5Jgp3lpRWUEt6idEp0FZzORpvtjt3pr5MG\n" + "Eg2CDptekC5BSPS+fIAIKlncB3HwOiFFhH6b3wTydDCdEd2fvsi4QMOSVrIYMeA8\n" + "P/mCz6kRhfUQPE0CMmOUewIDAQABo4GJMIGGMB0GA1UdDgQWBBT0/nNP8WpyxmYr\n" + "IBp4tN8y08jw2jBHBgNVHSMEQDA+gBSCJd0hpl5PdAD9IZS+Hzng4lXLG6EjpCEw\n" + "HzELMAkGA1UEBhMCVVMxEDAOBgNVBAoTB0V4YW1wbGWCAQAwDwYDVR0TAQH/BAUw\n" + "AwEB/zALBgNVHQ8EBAMCAgQwDQYJKoZIhvcNAQEEBQADgYEAS9PzI6B39R/U9fRj\n" + "UExzN1FXNP5awnAPtiv34kSCL6n6MryqkfG+8aaAOdZsSjmTylNFaF7cW/Xp1VBF\n" + "hq0bg/SbEAbK7+UwL8GSC3crhULHLbh+1iFdVTEwxCw5YmB8ji3BaZ/WKW/PkjCZ\n" + "7cXP6VDeZMG6oRQ4hbOcixoFPXo=\n" + "-----END CERTIFICATE-----";

    static String targetCertStr = "-----BEGIN CERTIFICATE-----\n" + "MIICNzCCAaCgAwIBAgIBBDANBgkqhkiG9w0BAQQFADAxMQswCQYDVQQGEwJVUzEQ\n" + "MA4GA1UEChMHRXhhbXBsZTEQMA4GA1UECxMHQ2xhc3MtMTAeFw0wOTA0MjcwMjI0\n" + "MzhaFw0yOTAxMTIwMjI0MzhaMEExCzAJBgNVBAYTAlVTMRAwDgYDVQQKEwdFeGFt\n" + "cGxlMRAwDgYDVQQLEwdDbGFzcy0xMQ4wDAYDVQQDEwVTdXNhbjCBnzANBgkqhkiG\n" + "9w0BAQEFAAOBjQAwgYkCgYEAyPKlfep+EIIUOpZF3xtYUhAx79qEqe2RPRcH2YeR\n" + "1ogM8+AZMdcXoiuDl4CFLzQwRv1DSKUZAPdPbROLVDsUn+IGvgn2jnE7ZQEUtQQJ\n" + "+rorcasE7bo5MBPuno/0oQRi/4MZn6lX3qB13ZUHAvZH96oCF6C3Ro19LAwav1Lo\n" + "FRcCAwEAAaNPME0wCwYDVR0PBAQDAgPoMB0GA1UdDgQWBBTCUH1tqQk96Pocr8Is\n" + "tDKMoIRQljAfBgNVHSMEGDAWgBT0/nNP8WpyxmYrIBp4tN8y08jw2jANBgkqhkiG\n" + "9w0BAQQFAAOBgQB3YXuTA+QfaImQ2aN/e27Nv5a/FMml6y6t0+pzt5hUYG2W0C2f\n" + "5Hdmf3whNCA7zE5RVDQP0iuGBPgjvrABuN98Vimv2eTV+N5aYTak0Aav/OuR5Lpi\n" + "tYhXMMg5gSmT+JDARba4CX+Ap1oAaNe9Mtv8L6FWdvBqfzzifDHWavdIWA==\n" + "-----END CERTIFICATE-----";

    static String topCrlIssuerCertStr = "-----BEGIN CERTIFICATE-----\n" + "MIICKzCCAZSgAwIBAgIBAjANBgkqhkiG9w0BAQQFADAfMQswCQYDVQQGEwJVUzEQ\n" + "MA4GA1UEChMHRXhhbXBsZTAeFw0wOTA0MjcwMjI0MzNaFw0yOTAxMTIwMjI0MzNa\n" + "MB8xCzAJBgNVBAYTAlVTMRAwDgYDVQQKEwdFeGFtcGxlMIGfMA0GCSqGSIb3DQEB\n" + "AQUAA4GNADCBiQKBgQDMJeBMBybHykI/YpwUJ4O9euqDSLb1kpWpceBS8TVqvgBC\n" + "SgUJWtFZL0i6bdvF6mMdlbuBkGzhXqHiVAi96/zRLbUC9F8SMEJ6MuD+YhQ0ZFTQ\n" + "atKy8zf8O9XzztelLJ26Gqb7QPV133WY3haAqHtCXOhEKkCN16NOYNC37DTaJwID\n" + "AQABo3cwdTAdBgNVHQ4EFgQULXSWzXzUOIpOJpzbSCpW42IJUugwRwYDVR0jBEAw\n" + "PoAUgiXdIaZeT3QA/SGUvh854OJVyxuhI6QhMB8xCzAJBgNVBAYTAlVTMRAwDgYD\n" + "VQQKEwdFeGFtcGxlggEAMAsGA1UdDwQEAwIBAjANBgkqhkiG9w0BAQQFAAOBgQAY\n" + "eMnf5AHSNlyUlzXk8o2S0h4gCuvKX6C3kFfKuZcWvFAbx4yQOWLS2s15/nzR4+AP\n" + "FGX3lgJjROyAh7fGedTQK+NFWwkM2ag1g3hXktnlnT1qHohi0w31nVBJxXEDO/Ck\n" + "uJTpJGt8XxxbFaw5v7cHy7XuTAeU/sekvjEiNHW00Q==\n" + "-----END CERTIFICATE-----";

    static String subCrlIssuerCertStr = "-----BEGIN CERTIFICATE-----\n" + "MIICPTCCAaagAwIBAgIBBDANBgkqhkiG9w0BAQQFADAfMQswCQYDVQQGEwJVUzEQ\n" + "MA4GA1UEChMHRXhhbXBsZTAeFw0wOTA0MjcwMjI0MzRaFw0yOTAxMTIwMjI0MzRa\n" + "MDExCzAJBgNVBAYTAlVTMRAwDgYDVQQKEwdFeGFtcGxlMRAwDgYDVQQLEwdDbGFz\n" + "cy0xMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDWUtDQx2MB/7arDiquMJyd\n" + "LWwSg6p8sg5z6wKrC1v47MT4DBhFX+0RUgTMUdQgYpgxGpczn+6y4zfV76064S0N\n" + "4L/IQ+SunTW1w4yRGjB+xkyyJmWAqijG1nr+Dgkv5nxPI+9Er5lHcoVWVMEcvvRm\n" + "6jIBQdldVlSgv+VgUnFm5wIDAQABo3cwdTAdBgNVHQ4EFgQUkV3Qqtk7gIot9n60\n" + "jX6dloxrfMEwRwYDVR0jBEAwPoAUgiXdIaZeT3QA/SGUvh854OJVyxuhI6QhMB8x\n" + "CzAJBgNVBAYTAlVTMRAwDgYDVQQKEwdFeGFtcGxlggEAMAsGA1UdDwQEAwIBAjAN\n" + "BgkqhkiG9w0BAQQFAAOBgQADu4GM8EdmIKhC7FRvk5jF90zfvZ38wbXBzCjKI4jX\n" + "QJrhne1bfyeNNm5c1w+VKidT+XzBzBGH7ZqYzoZmzRIfcbLKX2brEBKiukeeAyL3\n" + "bctQtbp19tX+uu2dQberD188AAysKTkHcJUV+rRsTwVJ9vcYKxoRxKk8DhH7ZS3M\n" + "rg==\n" + "-----END CERTIFICATE-----";

    static String topCrlStr = "-----BEGIN X509 CRL-----\n" + "MIIBGzCBhQIBATANBgkqhkiG9w0BAQQFADAfMQswCQYDVQQGEwJVUzEQMA4GA1UE\n" + "ChMHRXhhbXBsZRcNMDkwNDI3MDIzODA0WhcNMjgwNjI2MDIzODA0WjAiMCACAQUX\n" + "DTA5MDQyNzAyMzgwMFowDDAKBgNVHRUEAwoBBKAOMAwwCgYDVR0UBAMCAQIwDQYJ\n" + "KoZIhvcNAQEEBQADgYEAoarfzXEtw3ZDi4f9U8eSvRIipHSyxOrJC7HR/hM5VhmY\n" + "CErChny6x9lBVg9s57tfD/P9PSzBLusCcHwHMAbMOEcTltVVKUWZnnbumpywlYyg\n" + "oKLrE9+yCOkYUOpiRlz43/3vkEL5hjIKMcDSZnPKBZi1h16Yj2hPe9GMibNip54=\n" + "-----END X509 CRL-----";

    static String subCrlStr = "-----BEGIN X509 CRL-----\n" + "MIIBLTCBlwIBATANBgkqhkiG9w0BAQQFADAxMQswCQYDVQQGEwJVUzEQMA4GA1UE\n" + "ChMHRXhhbXBsZTEQMA4GA1UECxMHQ2xhc3MtMRcNMDkwNDI3MDIzODA0WhcNMjgw\n" + "NjI2MDIzODA0WjAiMCACAQQXDTA5MDQyNzAyMzgwMVowDDAKBgNVHRUEAwoBBKAO\n" + "MAwwCgYDVR0UBAMCAQIwDQYJKoZIhvcNAQEEBQADgYEAeS+POqYEIHIIJcsLxuUr\n" + "aJFzQ/ujH0QmnyMNEL3Uavyq4VQuAahF+w6aTPb5UBzms0uX8NAvD2vNoUJvmJOX\n" + "nGKuq4Q1DFj82E7/9d25nXdWGOmFvFCRVO+St2Xe5n8CJuZNBiz388FDSIOiFSCa\n" + "ARGr6Qu68MYGtLMC6ZqP3u0=\n" + "-----END X509 CRL-----";

    private static CertPath generateCertificatePath() throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream is;
        is = new ByteArrayInputStream(targetCertStr.getBytes());
        Certificate targetCert = cf.generateCertificate(is);
        is = new ByteArrayInputStream(subCaCertStr.getBytes());
        Certificate subCaCert = cf.generateCertificate(is);
        is = new ByteArrayInputStream(selfSignedCertStr.getBytes());
        Certificate selfSignedCert = cf.generateCertificate(is);
        List<Certificate> list = Arrays.asList(new Certificate[] { targetCert, subCaCert, selfSignedCert });
        return cf.generateCertPath(list);
    }

    private static Set<TrustAnchor> generateTrustAnchors() throws CertificateException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream is = new ByteArrayInputStream(selfSignedCertStr.getBytes());
        Certificate selfSignedCert = cf.generateCertificate(is);
        TrustAnchor anchor = new TrustAnchor((X509Certificate) selfSignedCert, null);
        return Collections.singleton(anchor);
    }

    private static CertStore generateCertificateStore() throws Exception {
        Collection entries = new HashSet();
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        ByteArrayInputStream is = new ByteArrayInputStream(topCrlStr.getBytes());
        Collection mixes = cf.generateCRLs(is);
        entries.addAll(mixes);
        is = new ByteArrayInputStream(subCrlStr.getBytes());
        mixes = cf.generateCRLs(is);
        entries.addAll(mixes);
        is = new ByteArrayInputStream(topCrlIssuerCertStr.getBytes());
        mixes = cf.generateCertificates(is);
        entries.addAll(mixes);
        is = new ByteArrayInputStream(subCrlIssuerCertStr.getBytes());
        mixes = cf.generateCertificates(is);
        entries.addAll(mixes);
        return CertStore.getInstance("Collection", new CollectionCertStoreParameters(entries));
    }

    public static void main(String[] args) throws Exception {
        Security.setProperty("jdk.certpath.disabledAlgorithms", "MD2, RSA keySize < 1024");
        CertPath path = generateCertificatePath();
        Set<TrustAnchor> anchors = generateTrustAnchors();
        CertStore crls = generateCertificateStore();
        PKIXParameters params = new PKIXParameters(anchors);
        params.addCertStore(crls);
        params.setRevocationEnabled(true);
        params.setDate(new Date(109, 5, 1));
        Security.setProperty("ocsp.enable", "false");
        System.setProperty("com.sun.security.enableCRLDP", "true");
        CertPathValidator validator = CertPathValidator.getInstance("PKIX");
        try {
            validator.validate(path, params);
            throw new Exception("unexpected status, should be REVOKED");
        } catch (CertPathValidatorException cpve) {
            if (cpve.getReason() != BasicReason.REVOKED) {
                throw new Exception("unexpect exception, should be a REVOKED CPVE", cpve);
            }
        }
    }
}
