


import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.*;
import javax.crypto.spec.*;

public class MacClone {

    public static void main(String[] args) throws Exception {

        String[] algos = { "HmacMD5", "HmacSHA1", "HmacSHA224", "HmacSHA256",
                           "HmacSHA384", "HmacSHA512" };
        KeyGenerator kgen = KeyGenerator.getInstance("DES");
        SecretKey skey = kgen.generateKey();
        for (String algo : algos) {
            doTest(algo, skey, null);
        }

        String[] algos2 = { "HmacPBESHA1", "PBEWithHmacSHA1",
                            "PBEWithHmacSHA224", "PBEWithHmacSHA256",
                            "PBEWithHmacSHA384", "PBEWithHmacSHA512" };
        skey = new SecretKeySpec("whatever".getBytes(), "PBE");
        PBEParameterSpec params =
            new PBEParameterSpec("1234567890".getBytes(), 500);
        for (String algo : algos2) {
            doTest(algo, skey, params);
        }
        System.out.println("Test Passed");
    }

    private static void doTest(String algo, SecretKey skey,
        AlgorithmParameterSpec params) throws Exception {
        
        
        
        Mac mac = Mac.getInstance(algo, "SunJCE");
        Mac macClone = (Mac)mac.clone();
        System.out.println(macClone.getProvider().toString());
        System.out.println(macClone.getAlgorithm());
        boolean thrown = false;
        try {
            macClone.update((byte)0x12);
        } catch (IllegalStateException ise) {
            thrown = true;
        }
        if (!thrown) {
            throw new Exception("Expected IllegalStateException not thrown");
        }

        
        
        
        mac = Mac.getInstance(algo, "SunJCE");
        mac.init(skey, params);
        macClone = (Mac)mac.clone();
        System.out.println(macClone.getProvider().toString());
        System.out.println(macClone.getAlgorithm());
        mac.update((byte)0x12);
        macClone.update((byte)0x12);
        byte[] macFinal = mac.doFinal();
        byte[] macCloneFinal = macClone.doFinal();
        if (!java.util.Arrays.equals(macFinal, macCloneFinal)) {
            throw new Exception("ERROR: MAC result of init clone is different");
        } else System.out.println("MAC check#1 passed");

        
        
        
        mac.update((byte)0x12);
        macClone = (Mac)mac.clone();
        mac.update((byte)0x34);
        macClone.update((byte)0x34);
        macFinal = mac.doFinal();
        macCloneFinal = macClone.doFinal();
        if (!java.util.Arrays.equals(macFinal, macCloneFinal)) {
            throw new Exception("ERROR: MAC result of updated clone is different");
        } else System.out.println("MAC check#2 passed");
    }
}
