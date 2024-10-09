




import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.math.*;

import java.util.*;

public class TestGCMKeyAndIvCheck {

    private static final byte[] AAD = new byte[5];
    private static final byte[] PT = new byte[18];

    private static void checkISE(Cipher c) throws Exception {
        
        try {
            c.updateAAD(AAD);
            throw new Exception("Should throw ISE for updateAAD()");
        } catch (IllegalStateException ise) {
            
        }

        try {
            c.update(PT);
            throw new Exception("Should throw ISE for update()");
        } catch (IllegalStateException ise) {
            
        }
        try {
            c.doFinal(PT);
            throw new Exception("Should throw ISE for doFinal()");
        } catch (IllegalStateException ise) {
            
        }
    }

    public void test() throws Exception {
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding", "SunJCE");

        SecretKey key = new SecretKeySpec(new byte[16], "AES");
        
        c.init(Cipher.ENCRYPT_MODE, key);
        c.updateAAD(AAD);
        byte[] ctPlusTag = c.doFinal(PT);

        
        checkISE(c);

        
        AlgorithmParameters params = c.getParameters();
        if (params == null) {
            throw new Exception("getParameters() should not return null");
        }
        GCMParameterSpec spec = params.getParameterSpec(GCMParameterSpec.class);
        if (spec.getTLen() != (ctPlusTag.length - PT.length)*8) {
            throw new Exception("Parameters contains incorrect TLen value");
        }
        if (!Arrays.equals(spec.getIV(), c.getIV())) {
            throw new Exception("Parameters contains incorrect IV value");
        }

        
        c.init(Cipher.DECRYPT_MODE, key, params);
        c.updateAAD(AAD);
        byte[] recovered = c.doFinal(ctPlusTag);
        if (!Arrays.equals(recovered, PT)) {
            throw new Exception("decryption result mismatch");
        }

        
        try {
            c.init(Cipher.ENCRYPT_MODE, key, params);
            throw new Exception("Should throw exception when same key+iv is used");
        } catch (InvalidAlgorithmParameterException iape) {
            
        }

        
        c.init(Cipher.ENCRYPT_MODE, key);
        c.doFinal(PT);

        
        byte[] iv = c.getIV();
        if (Arrays.equals(spec.getIV(), iv)) {
            throw new Exception("IV should be different now");
        }

        
        c.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(128, new byte[30]));
        c.updateAAD(AAD);
        c.doFinal(PT);
        
        checkISE(c);

        
        
        c.init(Cipher.DECRYPT_MODE, key, params);
        c.updateAAD(AAD);
        recovered = c.doFinal(ctPlusTag);

        c.updateAAD(AAD);
        recovered = c.doFinal(ctPlusTag);
        if (!Arrays.equals(recovered, PT)) {
            throw new Exception("decryption result mismatch");
        }

        
        c.init(Cipher.DECRYPT_MODE, key, params);
        c.updateAAD(AAD);
        recovered = c.doFinal(ctPlusTag);

        
        
        try {
            c.init(Cipher.DECRYPT_MODE, key);
            throw new Exception("Should throw IKE for dec w/o params");
        } catch (InvalidKeyException ike) {
            
        }

        
        
        try {
            c.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            throw new Exception("Should throw IAPE");
        } catch (InvalidAlgorithmParameterException iape) {
            
        }
        try {
            c.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            throw new Exception("Should throw IAPE");
        } catch (InvalidAlgorithmParameterException iape) {
            
        }

        System.out.println("Test Passed!");
    }

    public static void main (String[] args) throws Exception {
        TestGCMKeyAndIvCheck t = new TestGCMKeyAndIvCheck();
        t.test();
    }
}

