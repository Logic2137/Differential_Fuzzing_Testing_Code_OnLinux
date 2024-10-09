


import java.math.BigInteger;

public class BitLengthOverflow {

    public static void main(String[] args) {
        try {
            BigInteger x = BigInteger.ONE.shiftLeft(Integer.MAX_VALUE); 
            if (x.bitLength() != (1L << 31)) {
                throw new RuntimeException("Incorrect bitLength() " + x.bitLength());
            }
            System.out.println("Surprisingly passed with correct bitLength() " + x.bitLength());
        } catch (ArithmeticException e) {
            
            System.out.println("Overflow is reported by ArithmeticException, as expected");
        } catch (OutOfMemoryError e) {
            
            System.err.println("BitLengthOverflow skipped: OutOfMemoryError");
            System.err.println("Run jtreg with -javaoption:-Xmx8g");
        }
    }
}
