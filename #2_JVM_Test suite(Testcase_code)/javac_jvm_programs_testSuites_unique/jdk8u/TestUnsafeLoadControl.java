import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class TestUnsafeLoadControl {

    private static final Unsafe UNSAFE;

    static {
        try {
            Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            unsafeField.setAccessible(true);
            UNSAFE = (Unsafe) unsafeField.get(null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static int val;

    static void test1(int[] a, boolean[] flags, boolean flag, long j) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10; i++) {
            if (flags[i]) {
                if (flag) {
                    long address = (j << 2) + UNSAFE.ARRAY_INT_BASE_OFFSET;
                    int v = UNSAFE.getInt(a, address);
                    val = v;
                }
            }
        }
    }

    static int test2(int[] a, boolean[] flags, boolean flag, long j) {
        int sum = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10; i++) {
            if (flags[i]) {
                if (flag) {
                    long address = (j << 2) + UNSAFE.ARRAY_INT_BASE_OFFSET;
                    int v = UNSAFE.getInt(a, address);
                    if (v == 0) {
                        sum++;
                    }
                }
            }
        }
        return sum;
    }

    static public void main(String[] args) {
        boolean[] flags = new boolean[10];
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < flags.length; i++) {
            flags[i] = true;
        }
        int[] array = new int[10];
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 20000; i++) {
            test1(array, flags, true, 0);
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < flags.length; i++) {
            flags[i] = false;
        }
        test1(array, flags, true, Long.MAX_VALUE / 4);
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < flags.length; i++) {
            flags[i] = true;
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 20000; i++) {
            test2(array, flags, true, 0);
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < flags.length; i++) {
            flags[i] = false;
        }
        test2(array, flags, true, Long.MAX_VALUE / 4);
    }
}
