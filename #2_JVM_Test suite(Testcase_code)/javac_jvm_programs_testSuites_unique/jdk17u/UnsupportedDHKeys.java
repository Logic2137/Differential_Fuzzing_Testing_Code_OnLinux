import java.math.BigInteger;
import java.security.*;
import javax.crypto.*;
import javax.crypto.interfaces.*;
import javax.crypto.spec.*;

public class UnsupportedDHKeys {

    private enum UnsupportedKeySize {

        dhp513(513),
        dhp769(769),
        dhp895(895),
        dhp1023(1023),
        dhp1535(1535),
        dhp2047(2047),
        dhp2176(2176),
        dhp3008(3008),
        dhp4032(4032),
        dhp5120(5120),
        dhp6400(6400),
        dhp7680(7680),
        dhp8191(8191),
        dhp8128(8128),
        dhp8260(8260);

        final int primeSize;

        UnsupportedKeySize(int primeSize) {
            this.primeSize = primeSize;
        }
    }

    public static void main(String[] args) throws Exception {
        for (UnsupportedKeySize keySize : UnsupportedKeySize.values()) {
            try {
                System.out.println("Checking " + keySize.primeSize + " ...");
                KeyPairGenerator kpg = KeyPairGenerator.getInstance("DH", "SunJCE");
                kpg.initialize(keySize.primeSize);
                throw new Exception("Should not support " + keySize.primeSize);
            } catch (InvalidParameterException ipe) {
                System.out.println("\tOk, unsupported");
            }
        }
    }
}
