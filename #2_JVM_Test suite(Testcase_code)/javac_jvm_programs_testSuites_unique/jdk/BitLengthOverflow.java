


import java.math.BigInteger;
import java.util.function.Supplier;

public class BitLengthOverflow {
    private static void test(Supplier<BigInteger> s) {
        try {
            BigInteger x = s.get();
            System.out.println("Surprisingly passed with correct bitLength() " +
                               x.bitLength());
        } catch (ArithmeticException e) {
            
            System.out.println("Overflow reported by ArithmeticException, as expected");
        } catch (OutOfMemoryError e) {
            
            System.err.println("BitLengthOverflow skipped: OutOfMemoryError");
            System.err.println("Run jtreg with -javaoption:-Xmx8g");
        }
    }

    public static void main(String[] args) {
        test(() -> {
            
            BigInteger x = BigInteger.ONE.shiftLeft(Integer.MAX_VALUE);
            if (x.bitLength() != (1L << 31)) {
                throw new RuntimeException("Incorrect bitLength() " +
                                           x.bitLength());
            }
            return x;
        });
        test(() -> {
            BigInteger a = BigInteger.ONE.shiftLeft(1073742825);
            BigInteger b = BigInteger.ONE.shiftLeft(1073742825);
            return a.multiply(b);
        });
    }
}
