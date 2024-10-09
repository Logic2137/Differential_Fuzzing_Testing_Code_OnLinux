import java.security.*;
import java.security.cert.*;
import javax.crypto.*;
import javax.net.ssl.*;
import javax.security.auth.login.*;
import java.lang.reflect.*;
import java.util.Arrays;

public class GetInstanceNullsEmpties {

    private static final Provider SUN = Security.getProvider("SUN");

    private static void checkNewMethods(Class<?> clazz, int expected) throws Exception {
        long found = Arrays.stream(clazz.getMethods()).filter(name -> name.getName().equals("getInstance")).count();
        if (found != expected) {
            throw new Exception("Number of getInstance() mismatch: " + expected + " expected, " + found + " found");
        }
    }

    public static void main(String[] args) throws Exception {
        testAlgorithmParameterGenerator();
        testAlgorithmParameters();
        testCertificateFactory();
        testCertPathBuilder();
        testCertPathValidator();
        testCertStore();
        testKeyFactory();
        testKeyPairGenerator();
        testKeyStore();
        testMessageDigest();
        testPolicy();
        testSecureRandom();
        testSignature();
        testCipher();
        testExemptionMechanism();
        testKeyAgreement();
        testKeyGenerator();
        testMac();
        testSecretKeyFactory();
        testKeyManagerFactory();
        testSSLContext();
        testTrustManagerFactory();
        testConfiguration();
        System.out.println("\nTEST PASSED!");
    }

    private static Method getInstance(Class clazz, Class... args) throws Exception {
        boolean firstPrinted = false;
        System.out.print("\n" + clazz.getName() + "(");
        for (Class c : args) {
            System.out.print(firstPrinted ? ", " + c.getName() : c.getName());
            firstPrinted = true;
        }
        System.out.println("):");
        return clazz.getMethod("getInstance", args);
    }

    private static void run(Method m, Class expectedException, Object... args) throws Exception {
        try {
            m.invoke(null, args);
            throw new Exception("Didn't throw exception");
        } catch (InvocationTargetException ite) {
            Throwable root = ite.getCause();
            if (root instanceof Exception) {
                Exception e = (Exception) root;
                if (expectedException.isInstance(e)) {
                    System.out.print("OK ");
                    return;
                } else {
                    System.out.println("Unexpected InvocationTargetException!");
                    throw e;
                }
            }
            throw ite;
        }
    }

    private static final Class STRING = String.class;

    private static final Class PROVIDER = Provider.class;

    private static void testAlgorithmParameterGenerator() throws Exception {
        Class clazz = AlgorithmParameterGenerator.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testAlgorithmParameters() throws Exception {
        Class clazz = AlgorithmParameters.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testCertPathBuilder() throws Exception {
        Class clazz = CertPathBuilder.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testCertPathValidator() throws Exception {
        Class clazz = CertPathValidator.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testCertStore() throws Exception {
        Class clazz = CertStore.class;
        Method m;
        CertStoreParameters csp = () -> null;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING, CertStoreParameters.class);
        run(m, NullPointerException.class, (Object) null, csp);
        run(m, NoSuchAlgorithmException.class, "", csp);
        m = getInstance(clazz, STRING, CertStoreParameters.class, STRING);
        run(m, NullPointerException.class, null, csp, "SUN");
        run(m, NoSuchAlgorithmException.class, "", csp, "SUN");
        run(m, IllegalArgumentException.class, "FOO", csp, null);
        run(m, IllegalArgumentException.class, "FOO", csp, "");
        m = getInstance(clazz, STRING, CertStoreParameters.class, PROVIDER);
        run(m, NullPointerException.class, null, csp, SUN);
        run(m, NoSuchAlgorithmException.class, "", csp, SUN);
        run(m, IllegalArgumentException.class, "FOO", csp, null);
    }

    private static void testCertificateFactory() throws Exception {
        Class clazz = CertificateFactory.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, CertificateException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, CertificateException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, CertificateException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testCipher() throws Exception {
        Class clazz = Cipher.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NoSuchAlgorithmException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NoSuchAlgorithmException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NoSuchAlgorithmException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testConfiguration() throws Exception {
        Class clazz = Configuration.class;
        Method m;
        Configuration.Parameters cp = new Configuration.Parameters() {
        };
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING, Configuration.Parameters.class);
        run(m, NullPointerException.class, (Object) null, cp);
        run(m, NoSuchAlgorithmException.class, "", cp);
        m = getInstance(clazz, STRING, Configuration.Parameters.class, STRING);
        run(m, NullPointerException.class, null, cp, "SUN");
        run(m, NoSuchAlgorithmException.class, "", cp, "SUN");
        run(m, IllegalArgumentException.class, "FOO", cp, null);
        run(m, IllegalArgumentException.class, "FOO", cp, "");
        m = getInstance(clazz, STRING, Configuration.Parameters.class, PROVIDER);
        run(m, NullPointerException.class, null, cp, SUN);
        run(m, NoSuchAlgorithmException.class, "", cp, SUN);
        run(m, IllegalArgumentException.class, "FOO", cp, null);
    }

    private static void testExemptionMechanism() throws Exception {
        Class clazz = ExemptionMechanism.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testKeyAgreement() throws Exception {
        Class clazz = KeyAgreement.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testKeyFactory() throws Exception {
        Class clazz = KeyFactory.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testKeyGenerator() throws Exception {
        Class clazz = KeyGenerator.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testKeyManagerFactory() throws Exception {
        Class clazz = KeyManagerFactory.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testKeyPairGenerator() throws Exception {
        Class clazz = KeyPairGenerator.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testKeyStore() throws Exception {
        Class clazz = KeyStore.class;
        Method m;
        checkNewMethods(clazz, 5);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, KeyStoreException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, KeyStoreException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, KeyStoreException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testMac() throws Exception {
        Class clazz = Mac.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testMessageDigest() throws Exception {
        Class clazz = MessageDigest.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testPolicy() throws Exception {
        Class clazz = Policy.class;
        Method m;
        Policy.Parameters pp = new Policy.Parameters() {
        };
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING, Policy.Parameters.class);
        run(m, NullPointerException.class, (Object) null, pp);
        run(m, NoSuchAlgorithmException.class, "", pp);
        m = getInstance(clazz, STRING, Policy.Parameters.class, STRING);
        run(m, NullPointerException.class, null, pp, "SUN");
        run(m, NoSuchAlgorithmException.class, "", pp, "SUN");
        run(m, IllegalArgumentException.class, "FOO", pp, null);
        run(m, IllegalArgumentException.class, "FOO", pp, "");
        m = getInstance(clazz, STRING, Policy.Parameters.class, PROVIDER);
        run(m, NullPointerException.class, null, pp, SUN);
        run(m, NoSuchAlgorithmException.class, "", pp, SUN);
        run(m, IllegalArgumentException.class, "FOO", pp, null);
    }

    private static void testSSLContext() throws Exception {
        Class clazz = SSLContext.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testSecretKeyFactory() throws Exception {
        Class clazz = SecretKeyFactory.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testSecureRandom() throws Exception {
        Class clazz = SecureRandom.class;
        Method m;
        SecureRandomParameters srp = new SecureRandomParameters() {
        };
        checkNewMethods(clazz, 6);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
        m = getInstance(clazz, STRING, SecureRandomParameters.class);
        run(m, NullPointerException.class, (Object) null, srp);
        run(m, NoSuchAlgorithmException.class, "", srp);
        m = getInstance(clazz, STRING, SecureRandomParameters.class, STRING);
        run(m, NullPointerException.class, null, srp, "SUN");
        run(m, NoSuchAlgorithmException.class, "", srp, "SUN");
        run(m, IllegalArgumentException.class, "FOO", srp, null);
        run(m, IllegalArgumentException.class, "FOO", srp, "");
        m = getInstance(clazz, STRING, SecureRandomParameters.class, PROVIDER);
        run(m, NullPointerException.class, null, srp, SUN);
        run(m, NoSuchAlgorithmException.class, "", srp, SUN);
        run(m, IllegalArgumentException.class, "FOO", srp, null);
    }

    private static void testSignature() throws Exception {
        Class clazz = Signature.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }

    private static void testTrustManagerFactory() throws Exception {
        Class clazz = TrustManagerFactory.class;
        Method m;
        checkNewMethods(clazz, 3);
        m = getInstance(clazz, STRING);
        run(m, NullPointerException.class, (Object) null);
        run(m, NoSuchAlgorithmException.class, "");
        m = getInstance(clazz, STRING, STRING);
        run(m, NullPointerException.class, null, "SUN");
        run(m, NoSuchAlgorithmException.class, "", "SUN");
        run(m, IllegalArgumentException.class, "FOO", null);
        run(m, IllegalArgumentException.class, "FOO", "");
        m = getInstance(clazz, STRING, PROVIDER);
        run(m, NullPointerException.class, null, SUN);
        run(m, NoSuchAlgorithmException.class, "", SUN);
        run(m, IllegalArgumentException.class, "FOO", null);
    }
}
