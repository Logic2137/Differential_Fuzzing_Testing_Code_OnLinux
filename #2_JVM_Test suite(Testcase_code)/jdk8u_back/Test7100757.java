import java.util.*;

public class Test7100757 {

    public static final int NBITS = 256;

    public static void main(String[] args) {
        BitSet bs = new BitSet(NBITS);
        Random rnd = new Random();
        long[] ra = new long[(NBITS + 63) / 64];
        for (int l = 0; l < 5000000; l++) {
            for (int r = 0; r < ra.length; r++) {
                ra[r] = rnd.nextLong();
            }
            test(ra, bs);
        }
    }

    static void test(long[] ra, BitSet bs) {
        bs.clear();
        int bits_set = 0;
        for (int i = 0, t = 0, b = 0; i < NBITS; i++) {
            long bit = 1L << b++;
            if ((ra[t] & bit) != 0) {
                bs.set(i);
                bits_set++;
            }
            if (b == 64) {
                t++;
                b = 0;
            }
        }
        int check_bits = bs.cardinality();
        if (check_bits != bits_set) {
            String bs_str = bs.toString();
            System.err.printf("cardinality bits: %d != %d  bs: %s\n", check_bits, bits_set, bs_str);
            System.exit(97);
        }
        check_bits = 0;
        for (int i = bs.nextSetBit(0); i >= 0; i = bs.nextSetBit(i + 1)) {
            check_bits++;
        }
        if (check_bits != bits_set) {
            String bs_str = bs.toString();
            System.err.printf("nextSetBit bits: %d != %d  bs: %s\n", check_bits, bits_set, bs_str);
            System.exit(97);
        }
        for (int i = bs.length(); i > 0; i = bs.length()) {
            bs.clear(i - 1);
        }
        check_bits = bs.cardinality();
        if (check_bits != 0) {
            String bs_str = bs.toString();
            System.err.printf("after clear bits: %d != 0  bs: %s\n", check_bits, bs_str);
            System.exit(97);
        }
    }
}
