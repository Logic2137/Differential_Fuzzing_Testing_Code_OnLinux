import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.*;
import java.security.cert.Certificate;
import java.util.Date;
import java.util.Enumeration;

public class OneProbeOneNot {

    public static final void main(String[] args) throws Exception {
        Files.write(Path.of("ks"), "".getBytes());
        Security.insertProviderAt(new P1(), 1);
        Security.insertProviderAt(new P2(), 2);
        KeyStore ks = KeyStore.getInstance(new File("ks"), (char[]) null);
        System.out.println(ks.getProvider().getName());
        System.out.println(ks.getType());
    }

    public static class P1 extends Provider {

        public P1() {
            super("P1", "P1", "P1");
            putService(new Service(this, "KeyStore", "Oops", K1.class.getName(), null, null));
        }
    }

    public static class P2 extends Provider {

        public P2() {
            super("P2", "P2", "P2");
            putService(new Service(this, "KeyStore", "Oops", K2.class.getName(), null, null));
        }
    }

    public static class K1 extends KeyStoreSpi {

        public Key engineGetKey(String a, char[] p) {
            return null;
        }

        public Certificate[] engineGetCertificateChain(String a) {
            return null;
        }

        public Certificate engineGetCertificate(String a) {
            return null;
        }

        public Date engineGetCreationDate(String a) {
            return null;
        }

        public void engineSetKeyEntry(String a, Key k, char[] p, Certificate[] c) {
        }

        public void engineSetKeyEntry(String a, byte[] k, Certificate[] c) {
        }

        public void engineSetCertificateEntry(String a, Certificate c) {
        }

        public void engineDeleteEntry(String a) {
        }

        public Enumeration<String> engineAliases() {
            return null;
        }

        public boolean engineContainsAlias(String a) {
            return false;
        }

        public int engineSize() {
            return 0;
        }

        public boolean engineIsKeyEntry(String a) {
            return false;
        }

        public boolean engineIsCertificateEntry(String a) {
            return false;
        }

        public String engineGetCertificateAlias(Certificate c) {
            return null;
        }

        public void engineStore(OutputStream stream, char[] password) {
        }

        public void engineLoad(InputStream stream, char[] password) {
        }
    }

    public static class K2 extends K1 {

        public boolean engineProbe(InputStream s) {
            return true;
        }
    }
}
