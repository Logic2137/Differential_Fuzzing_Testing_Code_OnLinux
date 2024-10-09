import java.math.BigInteger;

public class DivisionOverflow {

    public static void main(String[] args) {
        try {
            BigInteger a = BigInteger.ONE.shiftLeft(2147483646);
            BigInteger b = BigInteger.ONE.shiftLeft(1568);
            BigInteger[] qr = a.divideAndRemainder(b);
            BigInteger q = qr[0];
            BigInteger r = qr[1];
            if (!r.equals(BigInteger.ZERO)) {
                throw new RuntimeException("Incorrect signum() of remainder " + r.signum());
            }
            if (q.bitLength() != 2147482079) {
                throw new RuntimeException("Incorrect bitLength() of quotient " + q.bitLength());
            }
            System.out.println("Division of large values passed without overflow.");
        } catch (OutOfMemoryError e) {
            System.err.println("DivisionOverflow skipped: OutOfMemoryError");
            System.err.println("Run jtreg with -javaoption:-Xmx8g");
        }
    }
}
