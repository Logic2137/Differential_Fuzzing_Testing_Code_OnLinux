



import java.text.NumberFormat;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import java.util.Locale;

public class Bug6278616 {

    static final int[] ints = {
        Integer.MIN_VALUE, -1, 0, 1, Integer.MAX_VALUE
    };

    static final long[] longs = {
        Long.MIN_VALUE, -1, 0, 1, Long.MAX_VALUE
    };

    public static void main(String[] args) {
        NumberFormat nf = NumberFormat.getInstance();

        for (int j = 0; j < ints.length; j++) {
            String s_i = nf.format(ints[j]);
            String s_ai = nf.format(new AtomicInteger(ints[j]));
            if (!s_i.equals(s_ai)) {
                throw new RuntimeException("format(AtomicInteger " + s_ai +
                                           ") doesn't equal format(Integer " +
                                           s_i + ")");
            }
        }

        for (int j = 0; j < longs.length; j++) {
            String s_l = nf.format(longs[j]);
            String s_al = nf.format(new AtomicLong(longs[j]));
            if (!s_l.equals(s_al)) {
                throw new RuntimeException("format(AtomicLong " + s_al +
                                           ") doesn't equal format(Long " +
                                           s_l + ")");
            }
        }
    }
}
