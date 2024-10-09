


import java.security.*;
import java.util.*;

public class TestSHAClone {

    
    
    
    
    
    private static final String[] ALGOS = {
        "SHA", "SHA-224", "SHA-256", "SHA-384", "SHA-512"
    };

    private static byte[] input1 = {
        (byte)0x1, (byte)0x2,  (byte)0x3
    };

    private static byte[] input2 = {
        (byte)0x4, (byte)0x5,  (byte)0x6
    };

    private MessageDigest md;

    private TestSHAClone(String algo, Provider p) throws Exception {
        md = MessageDigest.getInstance(algo, p);
    }

    private void run() throws Exception {
        md.update(input1);
        MessageDigest md2;
        try {
            md2 = (MessageDigest) md.clone();
        } catch (CloneNotSupportedException cnse) {
            System.out.println(md.getAlgorithm() + ": clone unsupported");
            return;
        }
        md.update(input2);
        md2.update(input2);
        if (!Arrays.equals(md.digest(), md2.digest())) {
            throw new Exception(md.getAlgorithm() + ": comparison failed");
        } else {
            System.out.println(md.getAlgorithm() + ": passed");
        }
    }


    public static void main(String[] argv) throws Exception {
        Provider p = Security.getProvider("SUN");
        for (int i=0; i<ALGOS.length; i++) {
            TestSHAClone test = new TestSHAClone(ALGOS[i], p);
            test.run();
        }
    }
}
