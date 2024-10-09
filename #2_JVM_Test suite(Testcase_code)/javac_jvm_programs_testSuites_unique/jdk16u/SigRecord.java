

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.*;
import java.util.ArrayList;
import java.util.List;

public final class SigRecord {

    static final String TEST_SRC = System.getProperty("test.src", ".");

    
    static String toHexString(byte[] array) {
        StringBuilder sb = new StringBuilder(array.length * 2);
        for (byte b : array) {
            
            if ((b >= 0x00) && (b < 0x10)) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(b & 0xff));
        }
        return sb.toString();
    }

    
    static byte[] toByteArray(String s) {
        byte[] bytes = new byte[s.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(s.substring(index, index + 2), 16);
            bytes[i] = (byte) v;
        }
        return bytes;
    }

    public static final class SigVector {
        
        final String mdAlg;

        
        final String msg;

        
        final String sig;

        public SigVector(String mdAlg, String msg, String sig) {
            if (mdAlg == null || mdAlg.isEmpty()) {
                throw new IllegalArgumentException("Digest algo must be specified");
            }
            if (msg == null || mdAlg.isEmpty()) {
                throw new IllegalArgumentException("Message must be specified");
            }
            if (sig == null || mdAlg.isEmpty()) {
                throw new IllegalArgumentException("Signature must be specified");
            }
            this.mdAlg = mdAlg;
            this.msg = msg;
            this.sig = sig;
        }

        @Override
        public String toString() {
            return (mdAlg + ": msg=" + msg + ": sig=" + sig);
        }
    }

    final String id;
    
    final RSAPrivateKeySpec privKeySpec;

    
    final RSAPublicKeySpec pubKeySpec;

    
    final List<SigVector> testVectors;

    SigRecord(String mod, String pubExp, String privExp, List<SigVector> testVectors) {
        if (mod == null || mod.isEmpty()) {
            throw new IllegalArgumentException("Modulus n must be specified");
        }
        if (pubExp == null || pubExp.isEmpty()) {
            throw new IllegalArgumentException("Public Exponent e must be specified");
        }
        if (privExp == null || privExp.isEmpty()) {
            throw new IllegalArgumentException("Private Exponent d must be specified");
        }
        if (testVectors == null || (testVectors.size() == 0)) {
            throw new IllegalArgumentException("One or more test vectors must be specified");
        }

        BigInteger n = new BigInteger(1, toByteArray(mod));
        BigInteger e = new BigInteger(1, toByteArray(pubExp));
        BigInteger d = new BigInteger(1, toByteArray(privExp));
        this.id = ("n=" + mod + ", e=" + pubExp);
        this.pubKeySpec = new RSAPublicKeySpec(n, e);
        this.privKeySpec = new RSAPrivateKeySpec(n, d);
        this.testVectors = testVectors;
    }

    
    public static List<SigRecord> read(String filename)
            throws IOException {

        List<SigRecord> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(new FileInputStream(
                        TEST_SRC + File.separator + filename)))) {
            String line;
            String mod = null;
            String pubExp = null;
            String privExp = null;
            List<SigVector> testVectors = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("n =")) {
                    mod = line.split("=")[1].trim();
                } else if (line.startsWith("e =")) {
                    pubExp = line.split("=")[1].trim();
                } else if (line.startsWith("d =")) {
                    privExp = line.split("=")[1].trim();

                    
                    String mdAlg = null;
                    String msg = null;
                    String sig = null;
                    boolean sigVectorDone = false;
                    while ((line = br.readLine()) != null) {
                        
                        
                        if (line.startsWith("SHAAlg =")) {
                            mdAlg = line.split(" = ")[1].trim();
                        } else if (line.startsWith("Msg =")) {
                            msg = line.split(" = ")[1].trim();
                        } else if (line.startsWith("S =")) {
                            sig = line.split(" = ")[1].trim();
                        } else if (line.startsWith("[mod")) {
                            sigVectorDone = true;
                        }

                        if ((mdAlg != null) && (msg != null) && (sig != null)) {
                            
                            testVectors.add(new SigVector(mdAlg, msg, sig));
                            mdAlg = msg = sig = null;
                        }
                        if (sigVectorDone) {
                            break;
                        }
                    }
                    
                    data.add(new SigRecord(mod, pubExp, privExp, testVectors));
                    mod = pubExp = privExp = null;
                    testVectors = new ArrayList<>();
                }
            }

            if (data.isEmpty()) {
                throw new RuntimeException("Nothing read from file "
                        + filename);
            }
        }
        return data;
    }

    @Override
    public String toString() {
        return (id + ", " + testVectors.size() + " test vectors");
    }
}
