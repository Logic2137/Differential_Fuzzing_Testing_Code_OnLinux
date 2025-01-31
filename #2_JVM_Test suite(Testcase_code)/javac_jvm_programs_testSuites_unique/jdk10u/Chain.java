

import java.security.Signature;
import java.security.SignedObject;
import java.security.KeyPairGenerator;
import java.security.KeyPair;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;


public class Chain {

    static enum KeyAlg {
        RSA("RSA"),
        DSA("DSA"),
        EC("EC");

        final String name;

        KeyAlg(String alg) {
            this.name = alg;
        }
    }

    static enum Provider {
        Default("default"),
        SunRsaSign("SunRsaSign"),
        Sun("SUN"),
        SunEC("SunEC"),
        SunJSSE("SunJSSE"),
        SunMSCAPI("SunMSCAPI");

        final String name;

        Provider(String name) {
            this.name = name;
        }
    }

    static enum SigAlg {
        MD2withRSA("MD2withRSA"),
        MD5withRSA("md5withRSA"),

        SHA1withDSA("SHA1withDSA"),
        SHA224withDSA("SHA224withDSA"),
        SHA256withDSA("SHA256withDSA"),

        SHA1withRSA("Sha1withrSA"),
        SHA224withRSA("SHA224withRSA"),
        SHA256withRSA("SHA256withRSA"),
        SHA384withRSA("SHA384withRSA"),
        SHA512withRSA("SHA512withRSA"),

        SHA1withECDSA("SHA1withECDSA"),
        SHA256withECDSA("SHA256withECDSA"),
        SHA224withECDSA("SHA224withECDSA"),
        SHA384withECDSA("SHA384withECDSA"),
        SHA512withECDSA("SHA512withECDSA"),

        MD5andSHA1withRSA("MD5andSHA1withRSA");

        final String name;

        SigAlg(String name) {
            this.name = name;
        }
    }

    static class Test {
        final Provider provider;
        final KeyAlg keyAlg;
        final SigAlg sigAlg;
        final int keySize;

        Test(SigAlg sigAlg, KeyAlg keyAlg, Provider provider) {
            this(sigAlg, keyAlg, provider, -1);
        }

        Test(SigAlg sigAlg, KeyAlg keyAlg, Provider provider, int keySize) {
            this.provider = provider;
            this.keyAlg = keyAlg;
            this.sigAlg = sigAlg;
            this.keySize = keySize;
        }
    }

    private static final Test[] tests = {
        new Test(SigAlg.SHA1withDSA, KeyAlg.DSA, Provider.Default, 1024),
        new Test(SigAlg.MD2withRSA, KeyAlg.RSA, Provider.Default),
        new Test(SigAlg.MD5withRSA, KeyAlg.RSA, Provider.Default),
        new Test(SigAlg.SHA1withRSA, KeyAlg.RSA, Provider.Default),
        new Test(SigAlg.SHA1withDSA, KeyAlg.DSA, Provider.Sun, 1024),
        new Test(SigAlg.SHA224withDSA, KeyAlg.DSA, Provider.Sun, 2048),
        new Test(SigAlg.SHA256withDSA, KeyAlg.DSA, Provider.Sun, 2048),
    };

    private static final String str = "to-be-signed";
    private static final int N = 3;

    public static void main(String argv[]) {
        boolean result = Arrays.stream(tests).allMatch((test) -> runTest(test));
        if(result) {
            System.out.println("All tests passed");
        } else {
            throw new RuntimeException("Some tests failed");
        }
    }

    static boolean runTest(Test test) {
        System.out.format("Test: provider = %s, signature algorithm = %s, "
                + "key algorithm = %s\n",
                test.provider, test.sigAlg, test.keyAlg);
        try {
            
            PrivateKey[] privKeys = new PrivateKey[N];
            PublicKey[] pubKeys = new PublicKey[N];
            PublicKey[] anotherPubKeys = new PublicKey[N];
            Signature signature;
            KeyPairGenerator kpg;
            if (test.provider != Provider.Default) {
                signature = Signature.getInstance(test.sigAlg.name,
                        test.provider.name);
                kpg = KeyPairGenerator.getInstance(
                    test.keyAlg.name, test.provider.name);
            } else {
                signature = Signature.getInstance(test.sigAlg.name);
                kpg = KeyPairGenerator.getInstance(test.keyAlg.name);
            }
            for (int j=0; j < N; j++) {
                if (test.keySize != -1) {
                    kpg.initialize(test.keySize);
                }
                KeyPair kp = kpg.genKeyPair();
                KeyPair anotherKp = kpg.genKeyPair();
                privKeys[j] = kp.getPrivate();
                pubKeys[j] = kp.getPublic();
                anotherPubKeys[j] = anotherKp.getPublic();

                if (Arrays.equals(pubKeys[j].getEncoded(),
                        anotherPubKeys[j].getEncoded())) {
                    System.out.println("Failed: it should not get "
                            + "the same pair of public key");
                    return false;
                }
            }

            
            SignedObject[] objects = new SignedObject[N];
            objects[0] = new SignedObject(str, privKeys[0], signature);
            for (int j = 1; j < N; j++) {
                objects[j] = new SignedObject(objects[j - 1], privKeys[j],
                        signature);
            }

            
            int n = objects.length - 1;
            SignedObject object = objects[n];
            do {
                if (!object.verify(pubKeys[n], signature)) {
                    System.out.println("Failed: verification failed, n = " + n);
                    return false;
                }

                if (object.verify(anotherPubKeys[n], signature)) {
                    System.out.println("Failed: verification should not "
                            + "succeed with wrong public key, n = " + n);
                    return false;
                }

                object = (SignedObject) object.getObject();
                n--;
            } while (n > 0);

            System.out.println("signed data: " + object.getObject());
            if (!str.equals(object.getObject())) {
                System.out.println("Failed: signed data is not equal to "
                        + "original one");
                return false;
            }

            System.out.println("Test passed");
            return true;
        } catch (NoSuchProviderException nspe) {
            if (test.provider == Provider.SunMSCAPI
                    && !System.getProperty("os.name").startsWith("Windows")) {
                System.out.println("SunMSCAPI is available only on Windows: "
                        + nspe);
                return true;
            }
            System.out.println("Unexpected exception: " + nspe);
            return false;
        } catch (Exception e) {
            System.out.println("Unexpected exception: " + e);
            e.printStackTrace(System.out);
            return false;
        }
    }
}

