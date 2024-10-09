



import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.sun.tools.javac.util.Bits;

public class BitsTest {

    final static int[] samples = { 0, 1, 7, 16, 19, 31, 32, 33, 63, 64 };
    final static int LENGTH = samples[samples.length - 1] + 50;

    public static void main(String... args) throws Exception {

        testIncl();
        testInclRange();
        testDup();
        testClear();
        testExcl();
        testExcludeFrom();
        testBinOps();
        testNextBit();

    }


    
    static void testIncl() {
        for (int a : samples) {
            for (int b : samples) {
                Bits bits = new Bits();
                bits.incl(a);
                if (a != b)
                    bits.incl(b);
                for (int i = 0; i < LENGTH; i++)
                    assert bits.isMember(i) == (i == a || i == b);
            }
        }
    }


    
    static void testExcl() {
        for (int a : samples) {
            for (int b : samples) {
                Bits bits = new Bits();
                bits.inclRange(0, LENGTH);
                bits.excl(a);
                if (a != b)
                    bits.excl(b);
                for (int i = 0; i < LENGTH; i++)
                    assert !bits.isMember(i) == (i == a || i == b);
            }
        }
    }


    
    static void testInclRange() {
        for (int i = 0; i < samples.length; i++) {
            for (int j = i; j < samples.length; j++)
                testInclRangeHelper(samples[i], samples[j]);
        }
    }


    
    static void testInclRangeHelper(int from, int to) {
        Bits bits = new Bits();
        bits.inclRange(from, to);
        for (int i = 0; i < LENGTH; i++)
            assert bits.isMember(i) == (from <= i && i < to);
    }


    
    static void testDup() {
        Bits bits = sampleBits();
        Bits dupBits = bits.dup();
        assertEquals(LENGTH, bits, dupBits);
    }


    
    static void testClear() {
        Bits bits = sampleBits();
        bits.clear();
        for (int i = 0; i < LENGTH; i++)
            assert !bits.isMember(i);
    }


    
    static void testExcludeFrom() {
        Bits bits = sampleBits();

        int half = samples.length / 2;
        Set<Integer> expected = new HashSet<Integer>();
        for (int i : Arrays.copyOf(samples, half))
            expected.add(i);

        bits.excludeFrom(samples[half]);

        for (int i = 0; i < LENGTH; i++)
            assert bits.isMember(i) == expected.contains(i);
    }


    
    static void testBinOps() {
        int[] a = { 1630757163, -592623705 };
        int[] b = { 1062404889, 1969380693 };

        int[] or   = { a[0] | b[0],  a[1] | b[1] };
        int[] and  = { a[0] & b[0],  a[1] & b[1] };
        int[] xor  = { a[0] ^ b[0],  a[1] ^ b[1] };
        int[] diff = { a[0] & ~b[0], a[1] & ~b[1] };

        assertEquals(64, fromInts(a).orSet  (fromInts(b)), fromInts(or));
        assertEquals(64, fromInts(a).andSet (fromInts(b)), fromInts(and));
        assertEquals(64, fromInts(a).xorSet (fromInts(b)), fromInts(xor));
        assertEquals(64, fromInts(a).diffSet(fromInts(b)), fromInts(diff));

    }


    
    static Bits fromInts(int[] ints) {
        Bits bits = new Bits();
        for (int bit = 0; bit < ints.length * 32; bit++)
            if ((ints[bit / 32] & (1 << (bit % 32))) != 0)
                bits.incl(bit);
        return bits;
    }


    
    static void assertEquals(int len, Bits a, Bits b) {
        for (int i = 0; i < len; i++)
            assert a.isMember(i) == b.isMember(i);
    }


    
    static void testNextBit() {
        Bits bits = sampleBits();

        int index = 0;
        for (int bit = 0; bit < LENGTH; bit++) {

            int expected;

            
            if (index < samples.length) {
                expected = samples[index];
                if (bit == samples[index])
                    index++;
            } else {
                expected = -1;
            }

            assert bits.nextBit(bit) == expected;
        }
    }


    
    static Bits sampleBits() {
        Bits bits = new Bits();
        for (int i : samples)
            bits.incl(i);
        return bits;
    }

}
