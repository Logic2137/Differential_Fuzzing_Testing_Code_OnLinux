
package compiler.intrinsics.bigInteger;

import java.math.BigInteger;

public class TestMultiplyToLenReturnProfile {

    static BigInteger m(BigInteger i1, BigInteger i2) {
        BigInteger res = BigInteger.valueOf(0);
        for (int i = 0; i < 100; i++) {
            res.add(i1.multiply(i2));
        }
        return res;
    }

    static public void main(String[] args) {
        BigInteger v = BigInteger.valueOf(Integer.MAX_VALUE).pow(2);
        for (int i = 0; i < 20000; i++) {
            m(v, v.add(BigInteger.valueOf(1)));
        }
    }
}
