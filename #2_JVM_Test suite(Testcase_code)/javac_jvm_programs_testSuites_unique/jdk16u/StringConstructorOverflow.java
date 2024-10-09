


import java.math.BigInteger;

public class StringConstructorOverflow {

    
    private static String makeLongHexString() {
        StringBuilder sb = new StringBuilder();
        sb.append('1');
        for (int i = 0; i < (1 << 30) - 1; i++) {
            sb.append('0');
        }
        sb.append('1');
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            BigInteger bi = new BigInteger(makeLongHexString(), 16);
            if (bi.compareTo(BigInteger.ONE) <= 0) {
                throw new RuntimeException("Incorrect result " + bi.toString());
            }
        } catch (ArithmeticException e) {
            
            System.out.println("Overflow is reported by ArithmeticException, as expected");
        } catch (OutOfMemoryError e) {
            
            System.err.println("StringConstructorOverflow skipped: OutOfMemoryError");
            System.err.println("Run jtreg with -javaoption:-Xmx8g");
        }
    }
}
