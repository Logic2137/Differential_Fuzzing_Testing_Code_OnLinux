import java.io.InputStream;
import java.security.KeyStore;
import java.security.Provider;
import java.security.Security;
import java.security.cert.CRL;
import java.security.cert.CRLException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactorySpi;
import java.util.Collection;
import java.util.Enumeration;

public class IterateWindowsRootStore {

    public static class TestFactory extends CertificateFactorySpi {

        @Override
        public Certificate engineGenerateCertificate(InputStream inStream) throws CertificateException {
            throw new CertificateException("unimplemented");
        }

        @Override
        public Collection<? extends Certificate> engineGenerateCertificates(InputStream inStream) throws CertificateException {
            throw new CertificateException("unimplemented");
        }

        @Override
        public CRL engineGenerateCRL(InputStream inStream) throws CRLException {
            throw new CRLException("unimplemented");
        }

        @Override
        public Collection<? extends CRL> engineGenerateCRLs(InputStream inStream) throws CRLException {
            throw new CRLException("unimplemented");
        }
    }

    public static class TestProvider extends Provider {

        private static final long serialVersionUID = 1L;

        public TestProvider() {
            super("TestProvider", 0.1, "Test provider for IterateWindowsRootStore");
            this.put("CertificateFactory.X.509", "IterateWindowsRootStore$TestFactory");
            this.put("Alg.Alias.CertificateFactory.X509", "X.509");
        }
    }

    public static void main(String[] args) throws Exception {
        boolean providerPrepended = false;
        String testprovider = System.getProperty("sun.security.mscapi.testprovider");
        if (testprovider != null && !testprovider.isEmpty()) {
            try {
                System.out.println("Trying to prepend external JCE provider " + testprovider);
                Class<?> providerclass = Class.forName(testprovider);
                Object provider = providerclass.newInstance();
                Security.insertProviderAt((Provider) provider, 1);
            } catch (Exception e) {
                System.out.println("Could not load JCE provider " + testprovider + ". Exception is:");
                e.printStackTrace(System.out);
            }
            providerPrepended = true;
            System.out.println("Sucessfully prepended JCE provider " + testprovider);
        }
        if (!providerPrepended) {
            System.out.println("Trying to prepend dummy JCE provider");
            Security.insertProviderAt(new TestProvider(), 1);
            System.out.println("Sucessfully prepended dummy JCE provider");
        }
        KeyStore keyStore = KeyStore.getInstance("Windows-ROOT", "SunMSCAPI");
        keyStore.load(null, null);
        Enumeration<String> aliases = keyStore.aliases();
        while (aliases.hasMoreElements()) {
            String alias = aliases.nextElement();
            System.out.print("Reading certificate for alias: " + alias + "...");
            keyStore.getCertificate(alias);
            System.out.println(" done.");
        }
    }
}
