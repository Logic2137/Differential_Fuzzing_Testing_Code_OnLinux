import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Random;

public class MontgomeryMultiplyTest {

    static final MethodHandles.Lookup lookup = MethodHandles.lookup();

    static final MethodHandle montgomeryMultiplyHandle, montgomerySquareHandle;

    static final MethodHandle bigIntegerConstructorHandle;

    static final Field bigIntegerMagField;

    static {
        try {
            Method m = BigInteger.class.getDeclaredMethod("montgomeryMultiply", int[].class, int[].class, int[].class, int.class, long.class, int[].class);
            m.setAccessible(true);
            montgomeryMultiplyHandle = lookup.unreflect(m);
            m = BigInteger.class.getDeclaredMethod("montgomerySquare", int[].class, int[].class, int.class, long.class, int[].class);
            m.setAccessible(true);
            montgomerySquareHandle = lookup.unreflect(m);
            Constructor c = BigInteger.class.getDeclaredConstructor(int.class, int[].class);
            c.setAccessible(true);
            bigIntegerConstructorHandle = lookup.unreflectConstructor(c);
            bigIntegerMagField = BigInteger.class.getDeclaredField("mag");
            bigIntegerMagField.setAccessible(true);
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }

    int[] montgomeryMultiply(int[] a, int[] b, int[] n, int len, long inv, int[] product) throws Throwable {
        int[] result = (a == b) ? (int[]) montgomerySquareHandle.invokeExact(a, n, len, inv, product) : (int[]) montgomeryMultiplyHandle.invokeExact(a, b, n, len, inv, product);
        return Arrays.copyOf(result, len);
    }

    BigInteger newBigInteger(int[] val) throws Throwable {
        return (BigInteger) bigIntegerConstructorHandle.invokeExact(1, val);
    }

    int[] mag(BigInteger n) {
        try {
            return (int[]) bigIntegerMagField.get(n);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    BigInteger montgomeryMultiply(BigInteger a, BigInteger b, BigInteger N, int len, BigInteger n_prime) throws Throwable {
        BigInteger T = a.multiply(b);
        BigInteger R = BigInteger.ONE.shiftLeft(len * 32);
        BigInteger mask = R.subtract(BigInteger.ONE);
        BigInteger m = (T.and(mask)).multiply(n_prime);
        m = m.and(mask);
        T = T.add(m.multiply(N));
        T = T.shiftRight(len * 32);
        if (T.compareTo(N) > 0) {
            T = T.subtract(N);
        }
        return T;
    }

    BigInteger montgomeryMultiply(int[] a_words, int[] b_words, int[] n_words, int len, BigInteger inv) throws Throwable {
        BigInteger t = montgomeryMultiply(newBigInteger(a_words), newBigInteger(b_words), newBigInteger(n_words), len, inv);
        return t;
    }

    void check(int[] a_words, int[] b_words, int[] n_words, int len, BigInteger inv) throws Throwable {
        BigInteger n = newBigInteger(n_words);
        BigInteger slow = montgomeryMultiply(a_words, b_words, n_words, len, inv);
        BigInteger fast = newBigInteger(montgomeryMultiply(a_words, b_words, n_words, len, inv.longValue(), null));
        if (!slow.mod(n).equals(fast.mod(n))) {
            throw new RuntimeException();
        }
    }

    Random rnd = new Random(0);

    int[] random_val(int bits) {
        int len = (bits + 63) / 64;
        int[] val = new int[len * 2];
        for (int i = 0; i < val.length; i++) val[i] = rnd.nextInt();
        int leadingZeros = 64 - (bits & 64);
        if (leadingZeros >= 32) {
            val[0] = 0;
            val[1] &= ~(-1l << (leadingZeros & 31));
        } else {
            val[0] &= ~(-1l << leadingZeros);
        }
        return val;
    }

    void testOneLength(int lenInBits, int lenInInts) throws Throwable {
        BigInteger mod = new BigInteger(lenInBits, 2, rnd);
        BigInteger r = BigInteger.ONE.shiftLeft(lenInInts * 32);
        BigInteger n_prime = mod.modInverse(r).negate();
        int[] n = mag(mod);
        if (n.length < lenInInts) {
            int[] x = new int[lenInInts];
            System.arraycopy(n, 0, x, lenInInts - n.length, n.length);
            n = x;
        }
        for (int i = 0; i < 10000; i++) {
            check(random_val(lenInBits), random_val(lenInBits), n, lenInInts, n_prime);
            int[] tmp = random_val(lenInBits);
            check(tmp, tmp, n, lenInInts, n_prime);
        }
    }

    void testResultValues() throws Throwable {
        testOneLength(1024, 32);
        testOneLength(1025, 34);
        for (int j = 10; j > 0; j--) {
            int lenInBits = rnd.nextInt(2048) + 64;
            int lenInInts = (lenInBits + 63) / 64 * 2;
            testOneLength(lenInBits, lenInInts);
        }
    }

    void testOneMontgomeryMultiplyCheck(int[] a, int[] b, int[] n, int len, long inv, int[] product, Class klass) {
        try {
            montgomeryMultiply(a, b, n, len, inv, product);
        } catch (Throwable ex) {
            if (klass.isAssignableFrom(ex.getClass()))
                return;
            throw new RuntimeException(klass + " expected, " + ex + " was thrown");
        }
        throw new RuntimeException(klass + " expected, was not thrown");
    }

    void testOneMontgomeryMultiplyCheck(int[] a, int[] b, BigInteger n, int len, BigInteger inv, Class klass) {
        testOneMontgomeryMultiplyCheck(a, b, mag(n), len, inv.longValue(), null, klass);
    }

    void testOneMontgomeryMultiplyCheck(int[] a, int[] b, BigInteger n, int len, BigInteger inv, int[] product, Class klass) {
        testOneMontgomeryMultiplyCheck(a, b, mag(n), len, inv.longValue(), product, klass);
    }

    void testMontgomeryMultiplyChecks() {
        int[] blah = random_val(40);
        int[] small = random_val(39);
        BigInteger mod = new BigInteger(40 * 32, 2, rnd);
        BigInteger r = BigInteger.ONE.shiftLeft(40 * 32);
        BigInteger n_prime = mod.modInverse(r).negate();
        testOneMontgomeryMultiplyCheck(blah, blah, mod, 41, n_prime, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(blah, blah, mod, 0, n_prime, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(blah, blah, mod, -1, n_prime, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(blah, blah.clone(), mod, 41, n_prime, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(blah, blah.clone(), mod, 0, n_prime, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(blah, blah.clone(), mod, 0, n_prime, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(small, small, mod, 39, n_prime, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(small, small, mod, 0, n_prime, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(small, small, mod, -1, n_prime, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(small, small.clone(), mod, 39, n_prime, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(small, small.clone(), mod, 0, n_prime, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(small, small.clone(), mod, -1, n_prime, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(blah, blah, mod, 40, n_prime, small, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(blah, blah.clone(), mod, 40, n_prime, small, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(small, blah, mod, 40, n_prime, blah, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(blah, small, mod, 40, n_prime, blah, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(blah, blah, mod, 40, n_prime, small, IllegalArgumentException.class);
        testOneMontgomeryMultiplyCheck(small, small, mod, 40, n_prime, blah, IllegalArgumentException.class);
    }

    public static void main(String[] args) {
        try {
            new MontgomeryMultiplyTest().testMontgomeryMultiplyChecks();
            new MontgomeryMultiplyTest().testResultValues();
        } catch (Throwable ex) {
            throw new RuntimeException(ex);
        }
    }
}
