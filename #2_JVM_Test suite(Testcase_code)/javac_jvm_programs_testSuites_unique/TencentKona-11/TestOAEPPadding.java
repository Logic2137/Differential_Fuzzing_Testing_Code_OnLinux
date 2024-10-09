



import java.util.Arrays;

import java.security.Security;
import java.security.Provider;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.MGF1ParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.PSource;


public class TestOAEPPadding {
    private static RSAPrivateKey privateKey;
    private static RSAPublicKey publicKey;
    static Provider cp;
    static boolean failed = false;

    public static void main(String args[]) throws Exception {
        cp = Security.getProvider("SunJCE");
        System.out.println("Testing provider " + cp.getName() + "...");
        Provider kfp = Security.getProvider("SunRsaSign");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA", kfp);
        kpg.initialize(2048);
        KeyPair kp = kpg.generateKeyPair();
        privateKey = (RSAPrivateKey)kp.getPrivate();
        publicKey = (RSAPublicKey)kp.getPublic();

        
        
        test(new OAEPParameterSpec("MD5", "MGF1",
                MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("MD5", "MGF1",
                MGF1ParameterSpec.SHA224, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("MD5", "MGF1",
                MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("MD5", "MGF1",
                MGF1ParameterSpec.SHA384, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("MD5", "MGF1",
                MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT));
        
        test(new OAEPParameterSpec("SHA1", "MGF1",
                MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA1", "MGF1",
                MGF1ParameterSpec.SHA224, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA1", "MGF1",
                MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA1", "MGF1",
                MGF1ParameterSpec.SHA384, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA1", "MGF1",
                MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT));
        
        test(null);
        
        test(new OAEPParameterSpec("SHA-224", "MGF1",
                MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-224", "MGF1",
                MGF1ParameterSpec.SHA224, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-224", "MGF1",
                MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-224", "MGF1",
                MGF1ParameterSpec.SHA384, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-224", "MGF1",
                MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT));
        
        test(new OAEPParameterSpec("SHA-256", "MGF1",
                MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-256", "MGF1",
                MGF1ParameterSpec.SHA224, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-256", "MGF1",
                MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-256", "MGF1",
                MGF1ParameterSpec.SHA384, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-256", "MGF1",
                MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT));
        
        test(new OAEPParameterSpec("SHA-384", "MGF1",
                MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-384", "MGF1",
                MGF1ParameterSpec.SHA224, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-384", "MGF1",
                MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-384", "MGF1",
                MGF1ParameterSpec.SHA384, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-384", "MGF1",
                MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT));
        
        test(new OAEPParameterSpec("SHA-512", "MGF1",
                MGF1ParameterSpec.SHA1, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-512", "MGF1",
                MGF1ParameterSpec.SHA224, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-512", "MGF1",
                MGF1ParameterSpec.SHA256, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-512", "MGF1",
                MGF1ParameterSpec.SHA384, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-512", "MGF1",
                MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT));
        
        test(new OAEPParameterSpec("SHA-512/224", "MGF1",
                MGF1ParameterSpec.SHA224, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-512/224", "MGF1",
                MGF1ParameterSpec.SHA512_224, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-512/256", "MGF1",
                MGF1ParameterSpec.SHA384, PSource.PSpecified.DEFAULT));
        test(new OAEPParameterSpec("SHA-512/256", "MGF1",
                MGF1ParameterSpec.SHA512, PSource.PSpecified.DEFAULT));

        if (failed) {
            throw new Exception("Test failed");
        }
    }

    
    static void test(OAEPParameterSpec spec) throws Exception {
        int dlen = 0;
        String algo;

        
        if (spec == null) {
            dlen = 20;
            algo = "Default";
        } else {
            
            algo = spec.getDigestAlgorithm();
            if (algo.equals("MD5")) {
                dlen = 16;
            } else if (algo.equals("SHA1")) {
                dlen = 20;
            } else if (algo.equals("SHA-224") || algo.equals("SHA-512/224")) {
                dlen = 28;
            } else if (algo.equals("SHA-256") || algo.equals("SHA-512/256")) {
                dlen = 32;
            } else if (algo.equals("SHA-384")) {
                dlen = 48;
            } else if (algo.equals("SHA-512")) {
                dlen = 64;
            }
        }

        
        int max = ((publicKey.getModulus().bitLength() / 8) - (2 * dlen) - 2);

        
        try {
            testEncryptDecrypt(spec, 1);
        } catch (Exception e) {
            System.out.println(algo + " failed with data length of 1");
            e.printStackTrace();
            failed = true;
        }

        
        try {
            testEncryptDecrypt(spec, max);
        } catch (Exception e) {
            System.out.println(algo + " failed with data length of " + max);
            e.printStackTrace();
            failed = true;
        }

        
        try {
            testEncryptDecrypt(spec, max + 1);
            throw new Exception();
        } catch (IllegalBlockSizeException ie) {
                
        } catch (Exception e) {
            System.err.println(algo + " failed with data length of " +
                    (max + 1));
            e.printStackTrace();
            failed = true;

        }
    }

    private static void testEncryptDecrypt(OAEPParameterSpec spec,
            int dataLength) throws Exception {

        System.out.print("Testing OAEP with hash ");
        if (spec != null) {
            System.out.print(spec.getDigestAlgorithm() + " and MGF " +
                ((MGF1ParameterSpec)spec.getMGFParameters()).
                    getDigestAlgorithm());
        } else {
            System.out.print("Default");
        }
        System.out.println(", " + dataLength + " bytes");

        Cipher c = Cipher.getInstance("RSA/ECB/OAEPPadding", cp);
        if (spec != null) {
            c.init(Cipher.ENCRYPT_MODE, publicKey, spec);
        } else {
            c.init(Cipher.ENCRYPT_MODE, publicKey);
        }

        byte[] data = new byte[dataLength];
        byte[] enc = c.doFinal(data);
        if (spec != null) {
            c.init(Cipher.DECRYPT_MODE, privateKey, spec);
        } else {
            c.init(Cipher.DECRYPT_MODE, privateKey);
        }
        byte[] dec = c.doFinal(enc);
        if (Arrays.equals(data, dec) == false) {
            throw new Exception("Data does not match");
        }
    }
}
