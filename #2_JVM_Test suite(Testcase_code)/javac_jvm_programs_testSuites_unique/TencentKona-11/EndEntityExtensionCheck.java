



import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXBuilderParameters;
import java.security.cert.PKIXCertPathChecker;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import sun.security.validator.TrustStoreUtil;
import sun.security.validator.Validator;


public class EndEntityExtensionCheck {

    
    private static final String CA =
        "-----BEGIN CERTIFICATE-----\n" +
        "MIICgDCCAj2gAwIBAgIEC18hWjALBgcqhkjOOAQDBQAwETEPMA0GA1UEAxMGVGVz\n" +
        "dENBMB4XDTE1MDQwNzIyMzUyMFoXDTI1MDQwNjIyMzUyMFowETEPMA0GA1UEAxMG\n" +
        "VGVzdENBMIIBuDCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2\n" +
        "EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdr\n" +
        "mVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXz\n" +
        "rith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+Gghdab\n" +
        "Pd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6Ewo\n" +
        "FhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhR\n" +
        "kImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYUAAoGBAJOWy2hVy4iNwsi/idWG\n" +
        "oksr9IZxQIFR2YavoUmD+rIgfYUpiCihzftDLMMaNYqp9PPxuOyoIPGPbwmKpAs5\n" +
        "nq6gLwH2lSsN+EwyV2SJ0J26PHiMuRNZWWfKR3cpEqbQVb0CmvqSpj8zYfamPzp7\n" +
        "eXSWwahzgLCGJM3SgCfDFC0uoyEwHzAdBgNVHQ4EFgQU7tLD8FnWM+r6jBr+mCXs\n" +
        "8G5yBpgwCwYHKoZIzjgEAwUAAzAAMC0CFQCHCtzC3S0ST0EZBucikVui4WXD8QIU\n" +
        "L3Oxy6989/FhZlZWJlhqc1ungEQ=\n" +
        "-----END CERTIFICATE-----";

    
    private static final String EE =
        "-----BEGIN CERTIFICATE-----\n" +
        "MIICrTCCAmugAwIBAgIELjciKzALBgcqhkjOOAQDBQAwETEPMA0GA1UEAxMGVGVz\n" +
        "dENBMB4XDTE1MDQwNzIzMDA1OFoXDTE1MDcwNjIzMDA1OFowETEPMA0GA1UEAxMG\n" +
        "VGVzdEVFMIIBtzCCASwGByqGSM44BAEwggEfAoGBAP1/U4EddRIpUt9KnC7s5Of2\n" +
        "EbdSPO9EAMMeP4C2USZpRV1AIlH7WT2NWPq/xfW6MPbLm1Vs14E7gB00b/JmYLdr\n" +
        "mVClpJ+f6AR7ECLCT7up1/63xhv4O1fnxqimFQ8E+4P208UewwI1VBNaFpEy9nXz\n" +
        "rith1yrv8iIDGZ3RSAHHAhUAl2BQjxUjC8yykrmCouuEC/BYHPUCgYEA9+Gghdab\n" +
        "Pd7LvKtcNrhXuXmUr7v6OuqC+VdMCz0HgmdRWVeOutRZT+ZxBxCBgLRJFnEj6Ewo\n" +
        "FhO3zwkyjMim4TwWeotUfI0o4KOuHiuzpnWRbqN/C/ohNWLx+2J6ASQ7zKTxvqhR\n" +
        "kImog9/hWuWfBpKLZl6Ae1UlZAFMO/7PSSoDgYQAAoGAN97otrAJEuUg/O97vScI\n" +
        "01xs1jqTz5o0PGpKiDDJNB3tCCUbLqXoBQBvSefQ8vYL3mmlEJLxlwfbajRmJQp0\n" +
        "tUy5SUCZHk3MdoKxSvrqYnVpYwJHFXKWs6lAawxfuWbkm9SREuepOWnVzy2ecf5z\n" +
        "hvy9mgEBfi4E9Cy8Byq2TpyjUDBOMAwGAyoDBAEB/wQCAAAwHwYDVR0jBBgwFoAU\n" +
        "7tLD8FnWM+r6jBr+mCXs8G5yBpgwHQYDVR0OBBYEFNRVqt5F+EAuJ5x1IZLDkoMs\n" +
        "mDj4MAsGByqGSM44BAMFAAMvADAsAhQyNGhxIp5IshN1zqLs4pUY214IMAIUMmTL\n" +
        "3ZMpMAjITbuHHlFNUqZ7A9s=\n" +
        "-----END CERTIFICATE-----";

    public static void main(String[] args) throws Exception {
        X509Certificate[] chain = createChain();

        
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(null, null);
        ks.setCertificateEntry("testca", chain[chain.length - 1]);

        Validator v = Validator.getInstance(Validator.TYPE_SIMPLE,
                                            Validator.VAR_TLS_CLIENT,
                                            TrustStoreUtil.getTrustedCerts(ks));
        try {
            v.validate(chain);
            throw new Exception("Chain should not have validated " +
                                "successfully.");
        } catch (CertificateException ex) {
            
            
            
        }

        
        TrustAnchor ta = new TrustAnchor(chain[chain.length - 1], null);
        Set<TrustAnchor> tas = new HashSet<>();
        tas.add(ta);
        PKIXBuilderParameters params = new PKIXBuilderParameters(tas, null);
        params.setDate(new Date(115, 5, 1));   
        params.setRevocationEnabled(false);

        v = Validator.getInstance(Validator.TYPE_PKIX,
                                  Validator.VAR_TLS_CLIENT,
                                  params);
        try {
            v.validate(chain);
            throw new Exception("Chain should not have validated " +
                                "successfully.");
        } catch (CertificateException ex) {
            
            
            
        }

        
        params = new PKIXBuilderParameters(tas, null);
        params.addCertPathChecker(new CustomChecker());
        params.setDate(new Date(115, 5, 1));   
        params.setRevocationEnabled(false);

        v = Validator.getInstance(Validator.TYPE_PKIX,
                                  Validator.VAR_TLS_CLIENT,
                                  params);
        v.validate(chain); 

        System.out.println("Tests passed.");
    }

    public static X509Certificate[] createChain() throws Exception {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        X509Certificate ee = (X509Certificate)
            cf.generateCertificate((new ByteArrayInputStream(EE.getBytes())));
        X509Certificate ca = (X509Certificate)
            cf.generateCertificate((new ByteArrayInputStream(CA.getBytes())));

        X509Certificate[] chain = {ee, ca};
        return chain;
    }

    
    static class CustomChecker extends PKIXCertPathChecker {

        @Override
        public void init(boolean forward) throws CertPathValidatorException {
            
        }

        @Override
        public boolean isForwardCheckingSupported() {
            return false;
        }

        @Override
        public Set<String> getSupportedExtensions() {
            Set<String> exts = new HashSet<>();
            exts.add("1.2.3.4");
            return exts;
        }

        @Override
        public void check(Certificate cert,
                          Collection<String> unresolvedCritExts)
                throws CertPathValidatorException {
            X509Certificate currCert = (X509Certificate)cert;
            
            if (currCert.getBasicConstraints() == -1) {
                if (unresolvedCritExts != null &&
                        !unresolvedCritExts.isEmpty()) {
                    unresolvedCritExts.remove("1.2.3.4");
                }
            }
        }

    }
}
