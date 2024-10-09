


import java.util.Arrays;
import java.security.*;

public class ResetAfterException {

    public static void main(String[] args) throws Exception {

        byte[] data = "data to be signed".getBytes();
        byte[] shortBuffer = new byte[2];

        Provider[] provs = Security.getProviders();
        boolean failed = false;

        for (Provider p : provs) {
            Signature sig;
            try {
                sig = Signature.getInstance("SHA256withRSA", p);
            } catch (NoSuchAlgorithmException nsae) {
                
                continue;
            }

            boolean res = true;
            System.out.println("Testing Provider: " + p.getName());
            KeyPairGenerator keyGen = null;
            try {
                
                
                
                keyGen = KeyPairGenerator.getInstance("RSA", p);
            } catch (NoSuchAlgorithmException nsae) {
                keyGen = KeyPairGenerator.getInstance("RSA");
            }
            if (keyGen == null) {
                throw new RuntimeException("Error: No support for RSA KeyPairGenerator");
            }
            keyGen.initialize(1024);
            KeyPair keyPair = keyGen.generateKeyPair();

            sig.initSign(keyPair.getPrivate());
            sig.update(data);
            byte[] signature = sig.sign();
            
            try {
                sig.update(data);
                
                int len = sig.sign(shortBuffer, 0, shortBuffer.length);
                System.out.println("FAIL: Should throw SE with short buffer");
                res = false;
            } catch (SignatureException e) {
                
                System.out.println("Expected Ex for short output buffer: " + e);
            }
            
            sig.update(data);
            byte[] signature2 = sig.sign();
            if (!Arrays.equals(signature, signature2)) {
                System.out.println("FAIL: Generated different signature");
                res = false;
            } else {
                System.out.println("Generated same signature");
            }

            
            sig.initVerify(keyPair.getPublic());
            sig.update(data);
            try {
                
                res = sig.verify(signature);
            } catch (SignatureException e) {
                System.out.println("FAIL: Valid signature rejected");
                e.printStackTrace();
                res = false;
            }

            try {
                sig.update(data);
                
                if (sig.verify(shortBuffer)) {
                    System.out.println("FAIL: Invalid signature verified");
                    res = false;
                } else {
                    System.out.println("Invalid signature rejected");
                }
            } catch (SignatureException e) {
                
                System.out.println("Expected Ex for short output buffer: " + e);
            }
            
            sig.update(data);
            try {
                
                res = sig.verify(signature);
                if (!res) {
                    System.out.println("FAIL: Valid signature is rejected");
                } else {
                    System.out.println("Valid signature is accepted");
                }
            } catch (GeneralSecurityException e) {
                System.out.println("FAIL: Valid signature is rejected");
                e.printStackTrace();
                res = false;
            }
            failed |= !res;
        }
        if (failed) {
            throw new RuntimeException("One or more test failed");
        } else {
            System.out.println("Test Passed");
        }
   }
}
