



import java.io.CharArrayReader;

public class OverflowInSkip {
    public static void main(String[] args) throws Exception {
        char[] a = "_123456789_123456789_123456789_123456789"
                .toCharArray(); 
        try (CharArrayReader car = new CharArrayReader(a)) {
            long small = 33;
            long big = Long.MAX_VALUE;

            long smallSkip = car.skip(small);
            if (smallSkip != small)
                throw new Exception("Expected to skip " + small
                        + " chars, but skipped " + smallSkip);

            long expSkip = a.length - small;
            long bigSkip = car.skip(big);
            if (bigSkip != expSkip)
                throw new Exception("Expected to skip " + expSkip
                        + " chars, but skipped " + bigSkip);
        }
    }
}
