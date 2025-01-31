



import java.security.*;
import java.security.Provider.Service;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class SupportsParameter {

    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DSA");
        kpg.initialize(512);
        KeyPair kp = kpg.generateKeyPair();
        PublicKey dsaPublicKey = kp.getPublic();
        PrivateKey dsaPrivateKey = kp.getPrivate();

        PublicKey myPublicKey = new MyPublicKey();
        PrivateKey myPrivateKey = new MyPrivateKey();
        SecretKey mySecretKey = new MySecretKey();

        Provider p = new MyProvider();
        Service s;

        
        s = p.getService("Signature", "DSA0");
        checkSupports(s, null, true);
        checkSupports(s, dsaPublicKey, true);
        checkSupports(s, dsaPrivateKey, true);
        checkSupports(s, myPublicKey, true);
        checkSupports(s, myPrivateKey, true);
        checkSupports(s, mySecretKey, true);

        
        s = p.getService("Signature", "DSA");
        checkSupports(s, dsaPublicKey, true);
        checkSupports(s, dsaPrivateKey, true);
        checkSupports(s, myPublicKey, false);
        checkSupports(s, myPrivateKey, false);
        checkSupports(s, mySecretKey, false);

        
        s = p.getService("Signature", "DSA2");
        checkSupports(s, dsaPublicKey, true);
        checkSupports(s, dsaPrivateKey, true);
        checkSupports(s, myPublicKey, false);
        checkSupports(s, myPrivateKey, false);
        checkSupports(s, mySecretKey, false);

        
        s = p.getService("Signature", "DSA3");
        checkSupports(s, dsaPublicKey, true);
        checkSupports(s, dsaPrivateKey, false);
        checkSupports(s, myPublicKey, true);
        checkSupports(s, myPrivateKey, true);
        checkSupports(s, mySecretKey, false);

        
        s = p.getService("Cipher", "DES");
        checkSupports(s, dsaPublicKey, false);
        checkSupports(s, dsaPrivateKey, false);
        checkSupports(s, myPublicKey, false);
        checkSupports(s, myPrivateKey, false);
        checkSupports(s, mySecretKey, true);
        Key secretKeySpec = new SecretKeySpec(new byte[8], "DES");
        checkSupports(s, secretKeySpec, true);

    }

    private static void checkSupports(Service s, Key key, boolean r) throws Exception {
        if (s.supportsParameter(key) != r) {
            throw new Exception("Result mismatch");
        }
        System.out.println("Passed");
    }

    private static class MyProvider extends Provider {
        MyProvider() {
            super("MyProvider", "1.0", "MyProvider");

            put("Signature.DSA0", "foo.DSA0");

            put("Signature.DSA", "foo.DSA");
            put("Signature.DSA SupportedKeyClasses",
                "java.security.interfaces.DSAPublicKey" +
                "|java.security.interfaces.DSAPrivateKey");

            put("Signature.DSA2", "foo.DSA2");
            put("Signature.DSA2 SupportedKeyFormats", "X.509|PKCS#8");

            put("Signature.DSA3", "foo.DSA3");
            put("Signature.DSA3 SupportedKeyClasses",
                "SupportsParameter$MyPrivateKey" +
                "|SupportsParameter$MyPublicKey" +
                "|java.security.interfaces.DSAPublicKey");

            put("Cipher.DES", "foo.DES");
            put("Cipher.DES SupportedKeyFormats", "RAW");
            put("Cipher.DES SupportedKeyClasses", "SupportsParameter$MySecretKey");
        }
    }

    private static class MyKey implements Key {
        public String getAlgorithm() { return "FOO"; }

        public String getFormat() { return null; }

        public byte[] getEncoded() { return null; }
    }

    private static class MyPrivateKey extends MyKey implements PrivateKey { }

    private static class MyPublicKey extends MyKey implements PublicKey {}

    private static class MySecretKey extends MyKey implements SecretKey {
        public String getAlgorithm() { return "DES"; }
    }

}
