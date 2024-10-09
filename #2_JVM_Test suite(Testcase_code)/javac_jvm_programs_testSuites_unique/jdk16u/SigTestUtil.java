

package jdk.test.lib;

import java.security.*;
import java.security.spec.*;
import java.util.*;


public class SigTestUtil {

    public enum SignatureType {
        RSA("RSA"),
        RSASSA_PSS("RSASSA-PSS")
        ;

        private String keyAlg;

        SignatureType(String keyAlg) {
            this.keyAlg = keyAlg;
        }
        @Override
        public String toString() {
            return keyAlg;
        }
    }

    
    
    private static final String[] DIGEST_ALGS = {
        "SHA3-512",
        "SHA-512",
        "SHA3-384",
        "SHA-384",
        "SHA3-256",
        "SHA-256",
        "SHA-512/256",
        "SHA3-224",
        "SHA-224",
        "SHA-512/224",
        "SHA-1",
        "MD2", "MD5" 
    };

    
    
    private static final int PKCS1_5_INDEX_768 = 0; 
    private static final int PKCS1_5_INDEX_512 = 4; 
    private static final int PKCS1_5_INDEX_END = DIGEST_ALGS.length;
    private static final int PSS_INDEX_2048 = 0; 
    private static final int PSS_INDEX_1024 = 2; 
    private static final int PSS_INDEX_768 = 4; 
    private static final int PSS_INDEX_512 = 7; 
    private static final int PSS_INDEX_END = DIGEST_ALGS.length - 2;

    public static Iterable<String> getDigestAlgorithms(SignatureType type,
            int keysize) throws RuntimeException {

        
        List<String> result = new ArrayList<>(Arrays.asList(DIGEST_ALGS));
        int index = 0;
        switch (type) {
        case RSA:
            if (keysize >= 768) {
                index = PKCS1_5_INDEX_768;
            } else if (keysize >= 512) {
                index = PKCS1_5_INDEX_512;
            } else {
                throw new RuntimeException("Keysize too small: " + keysize);
            }
            result = result.subList(index, PKCS1_5_INDEX_END);
            break;
        case RSASSA_PSS:
            if (keysize >= 2048) {
                index = PSS_INDEX_2048;
            } else if (keysize >= 1024) {
                index = PSS_INDEX_1024;
            } else if (keysize >= 768) {
                index = PSS_INDEX_768;
            } else if (keysize >= 512) {
                index = PSS_INDEX_512;
            } else {
                throw new RuntimeException("Keysize too small: " + keysize);
            }
            result = result.subList(index, PSS_INDEX_END);
            break;
        default:
            
            throw new RuntimeException("Unsupported signature type: " + type);
        }
        return result;
    }

    public static AlgorithmParameterSpec generateDefaultParameter(
            SignatureType type, String mdAlg) throws RuntimeException {
        
        switch (type) {
        case RSASSA_PSS:
            try {
                MessageDigest md = MessageDigest.getInstance(mdAlg);
                return new PSSParameterSpec(mdAlg, "MGF1",
                    new MGF1ParameterSpec(mdAlg), md.getDigestLength(),
                    PSSParameterSpec.TRAILER_FIELD_BC);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        default:
            return null;
        }
    }

    public static String generateSigAlg(SignatureType type,
            String mdAlg) throws RuntimeException {
        switch (type) {
        case RSA:
            if (mdAlg.startsWith("SHA-")) {
                mdAlg = mdAlg.substring(0, 3) + mdAlg.substring(4);
            }
            return mdAlg + "with" + type.toString();
        case RSASSA_PSS:
            return type.toString();
        default:
            throw new RuntimeException("Unsupported signature type " + type );
        }
    }

}
