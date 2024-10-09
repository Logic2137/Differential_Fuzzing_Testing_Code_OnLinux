import java.lang.reflect.Field;

public class UnsafeVolatileGuardTest {

    volatile static private int a;

    static private int b;

    static final sun.misc.Unsafe UNSAFE;

    static final Object BASE;

    static final long OFFSET;

    static {
        try {
            Field uf = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            uf.setAccessible(true);
            UNSAFE = (sun.misc.Unsafe) uf.get(null);
            Field f = UnsafeVolatileGuardTest.class.getDeclaredField("a");
            BASE = UNSAFE.staticFieldBase(f);
            OFFSET = UNSAFE.staticFieldOffset(f);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static void test() {
        int tt = b;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        while (UNSAFE.getIntVolatile(BASE, OFFSET) == 0) {
        }
        if (b == 0) {
            System.err.println("wrong value of b");
            System.exit(1);
        }
    }

    public static void main(String[] args) throws Exception {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 10; i++) {
            new Thread(UnsafeVolatileGuardTest::test).start();
        }
        b = 1;
        a = 1;
    }
}
