import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateFactory;
import java.security.cert.CertificateRevokedException;
import java.security.cert.PKIXParameters;
import java.security.cert.PKIXRevocationChecker;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;

public class ValidatePathWithParams {

    private static final String FS = System.getProperty("file.separator");

    private static final String CACERTS_STORE = System.getProperty("test.jdk") + FS + "lib" + FS + "security" + FS + "cacerts";

    private final String[] trustedRootCerts;

    private Date validationDate = null;

    private Status expectedStatus = Status.UNKNOWN;

    private Date expectedRevDate = null;

    private final CertPathValidator certPathValidator;

    private final PKIXRevocationChecker certPathChecker;

    private final CertificateFactory cf;

    public static enum Status {

        UNKNOWN, GOOD, REVOKED, EXPIRED
    }

    public ValidatePathWithParams(String[] additionalTrustRoots) throws IOException, CertificateException, NoSuchAlgorithmException {
        cf = CertificateFactory.getInstance("X509");
        certPathValidator = CertPathValidator.getInstance("PKIX");
        certPathChecker = (PKIXRevocationChecker) certPathValidator.getRevocationChecker();
        if ((additionalTrustRoots == null) || (additionalTrustRoots[0] == null)) {
            trustedRootCerts = null;
        } else {
            trustedRootCerts = additionalTrustRoots.clone();
        }
    }

    public void validate(String[] certsToValidate, Status st, String revDate, PrintStream out) throws IOException, CertificateException, InvalidAlgorithmParameterException, ParseException, NoSuchAlgorithmException, KeyStoreException {
        expectedStatus = st;
        if (expectedStatus == Status.REVOKED) {
            if (revDate != null) {
                expectedRevDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", Locale.US).parse(revDate);
            }
        }
        Status certStatus = null;
        Date revocationDate = null;
        logSettings(out);
        try {
            doCertPathValidate(certsToValidate, out);
            certStatus = Status.GOOD;
        } catch (IOException ioe) {
            out.println("WARNING: Network setup issue, skip this test");
            ioe.printStackTrace(System.err);
            return;
        } catch (CertPathValidatorException cpve) {
            out.println("Received exception: " + cpve);
            if (cpve.getCause() instanceof IOException) {
                out.println("WARNING: CertPathValidatorException caused by IO" + " error, skip this test");
                return;
            }
            if (cpve.getReason() == CertPathValidatorException.BasicReason.ALGORITHM_CONSTRAINED) {
                out.println("WARNING: CertPathValidatorException caused by" + " restricted algorithm, skip this test");
                return;
            }
            if (cpve.getReason() == CertPathValidatorException.BasicReason.REVOKED || cpve.getCause() instanceof CertificateRevokedException) {
                certStatus = Status.REVOKED;
                if (cpve.getCause() instanceof CertificateRevokedException) {
                    CertificateRevokedException cre = (CertificateRevokedException) cpve.getCause();
                    revocationDate = cre.getRevocationDate();
                }
            } else if (cpve.getReason() == CertPathValidatorException.BasicReason.EXPIRED || cpve.getCause() instanceof CertificateExpiredException) {
                certStatus = Status.EXPIRED;
            } else {
                throw new RuntimeException("TEST FAILED: couldn't determine EE certificate status", cpve);
            }
        }
        out.println("Expected Certificate status: " + expectedStatus);
        out.println("Certificate status after validation: " + certStatus.name());
        if (expectedStatus != Status.EXPIRED && certStatus == Status.EXPIRED) {
            out.println("WARNING: Certificate expired, skip the test");
            return;
        }
        if (certStatus != expectedStatus) {
            throw new RuntimeException("TEST FAILED: unexpected status of EE certificate");
        }
        if (certStatus == Status.REVOKED) {
            if (revocationDate != null) {
                out.println("Certificate revocation date:" + revocationDate.toString());
                if (expectedRevDate != null) {
                    out.println("Expected revocation date:" + expectedRevDate.toString());
                    if (!expectedRevDate.equals(revocationDate)) {
                        throw new RuntimeException("TEST FAILED: unexpected revocation date");
                    }
                }
            } else {
                throw new RuntimeException("TEST FAILED: no revocation date");
            }
        }
    }

    private void logSettings(PrintStream out) {
        out.println();
        out.println("=====================================================");
        out.println("CONFIGURATION");
        out.println("=====================================================");
        out.println("http.proxyHost :" + System.getProperty("http.proxyHost"));
        out.println("http.proxyPort :" + System.getProperty("http.proxyPort"));
        out.println("https.proxyHost :" + System.getProperty("https.proxyHost"));
        out.println("https.proxyPort :" + System.getProperty("https.proxyPort"));
        out.println("https.socksProxyHost :" + System.getProperty("https.socksProxyHost"));
        out.println("https.socksProxyPort :" + System.getProperty("https.socksProxyPort"));
        out.println("jdk.certpath.disabledAlgorithms :" + Security.getProperty("jdk.certpath.disabledAlgorithms"));
        out.println("Revocation options :" + certPathChecker.getOptions());
        out.println("OCSP responder set :" + certPathChecker.getOcspResponder());
        out.println("Trusted root set: " + (trustedRootCerts != null));
        if (validationDate != null) {
            out.println("Validation Date:" + validationDate.toString());
        }
        out.println("Expected EE Status:" + expectedStatus.name());
        if (expectedStatus == Status.REVOKED && expectedRevDate != null) {
            out.println("Expected EE Revocation Date:" + expectedRevDate.toString());
        }
        out.println("=====================================================");
    }

    private void doCertPathValidate(String[] certsToValidate, PrintStream out) throws IOException, CertificateException, InvalidAlgorithmParameterException, ParseException, NoSuchAlgorithmException, CertPathValidatorException, KeyStoreException {
        if (certsToValidate == null) {
            throw new RuntimeException("Require atleast one cert to validate");
        }
        ArrayList<X509Certificate> certs = new ArrayList<>();
        for (String cert : certsToValidate) {
            if (cert != null) {
                certs.add(getCertificate(cert));
            }
        }
        CertPath certPath = (CertPath) cf.generateCertPath(certs);
        KeyStore cacerts = KeyStore.getInstance("JKS");
        try (FileInputStream fis = new FileInputStream(CACERTS_STORE)) {
            cacerts.load(fis, "changeit".toCharArray());
        } catch (IOException | NoSuchAlgorithmException | CertificateException ex) {
            throw new RuntimeException(ex);
        }
        if (trustedRootCerts != null) {
            for (int i = 0; i < trustedRootCerts.length; i++) {
                X509Certificate rootCACert = getCertificate(trustedRootCerts[i]);
                cacerts.setCertificateEntry("tempca" + i, rootCACert);
            }
        }
        PKIXParameters params;
        params = new PKIXParameters(cacerts);
        params.addCertPathChecker(certPathChecker);
        params.setDate(validationDate);
        certPathValidator.validate(certPath, params);
        out.println("Successful CertPath validation");
    }

    private X509Certificate getCertificate(String encodedCert) throws IOException, CertificateException {
        ByteArrayInputStream is = new ByteArrayInputStream(encodedCert.getBytes());
        X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
        return cert;
    }

    public static void setDisabledAlgorithms(String algos) {
        Security.setProperty("jdk.certpath.disabledAlgorithms", algos);
    }

    public void enableOCSPCheck() {
        certPathChecker.setOptions(EnumSet.of(PKIXRevocationChecker.Option.NO_FALLBACK));
    }

    public void enableCRLCheck() {
        certPathChecker.setOptions(EnumSet.of(PKIXRevocationChecker.Option.PREFER_CRLS, PKIXRevocationChecker.Option.NO_FALLBACK));
    }

    public void setOCSPResponderURL(String url) throws URISyntaxException {
        certPathChecker.setOcspResponder(new URI(url));
    }

    public void setValidationDate(String vDate) throws ParseException {
        validationDate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US).parse(vDate);
    }

    public void resetValidationDate() {
        validationDate = null;
    }
}
