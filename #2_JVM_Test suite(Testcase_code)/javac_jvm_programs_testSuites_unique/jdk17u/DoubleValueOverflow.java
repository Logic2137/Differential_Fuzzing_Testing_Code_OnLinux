import java.math.BigInteger;

public class DoubleValueOverflow {

    public static void main(String[] args) {
        try {
            BigInteger x = BigInteger.valueOf(2).shiftLeft(Integer.MAX_VALUE);
            if (x.doubleValue() != Double.POSITIVE_INFINITY) {
                throw new RuntimeException("Incorrect doubleValue() " + x.doubleValue());
            }
            System.out.println("Passed with correct result");
        } catch (ArithmeticException e) {
            System.out.println("Overflow is reported by ArithmeticException, as expected");
        } catch (OutOfMemoryError e) {
            System.err.println("DoubleValueOverflow skipped: OutOfMemoryError");
            System.err.println("Run jtreg with -javaoption:-Xmx8g");
        }
    }
}
