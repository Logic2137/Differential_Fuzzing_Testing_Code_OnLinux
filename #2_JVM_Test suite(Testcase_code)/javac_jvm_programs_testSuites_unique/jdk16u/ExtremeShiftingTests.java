


import java.math.BigInteger;
import static java.math.BigInteger.*;

public class ExtremeShiftingTests {
    public static void main(String... args) {
        BigInteger bi = ONE.shiftLeft(Integer.MIN_VALUE);
        if (!bi.equals(ZERO))
            throw new RuntimeException("1 << " + Integer.MIN_VALUE);

        bi = ZERO.shiftLeft(Integer.MIN_VALUE);
        if (!bi.equals(ZERO))
            throw new RuntimeException("0 << " + Integer.MIN_VALUE);

        bi = BigInteger.valueOf(-1);
        bi = bi.shiftLeft(Integer.MIN_VALUE);
        if (!bi.equals(BigInteger.valueOf(-1)))
            throw new RuntimeException("-1 << " + Integer.MIN_VALUE);

        try {
            ONE.shiftRight(Integer.MIN_VALUE);
            throw new RuntimeException("1 >> " + Integer.MIN_VALUE);
        } catch (ArithmeticException ae) {
            ; 
        }

        bi = ZERO.shiftRight(Integer.MIN_VALUE);
        if (!bi.equals(ZERO))
            throw new RuntimeException("0 >> " + Integer.MIN_VALUE);

        try {
            BigInteger.valueOf(-1).shiftRight(Integer.MIN_VALUE);
            throw new RuntimeException("-1 >> " + Integer.MIN_VALUE);
        } catch (ArithmeticException ae) {
            ; 
        }

    }
}
