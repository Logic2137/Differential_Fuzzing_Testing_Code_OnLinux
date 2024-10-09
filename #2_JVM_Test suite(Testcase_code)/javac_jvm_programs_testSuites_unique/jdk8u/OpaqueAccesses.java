
package compiler.unsafe;

import sun.misc.Unsafe;
import java.lang.reflect.Field;

public class OpaqueAccesses {

    private static final Unsafe UNSAFE = Unsafe.getUnsafe();

    private static final Object INSTANCE = new OpaqueAccesses();

    private static final Object[] ARRAY = new Object[10];

    private static final long F_OFFSET;

    private static final long E_OFFSET;

    static {
        try {
            Field field = OpaqueAccesses.class.getDeclaredField("f");
            F_OFFSET = UNSAFE.objectFieldOffset(field);
            E_OFFSET = UNSAFE.arrayBaseOffset(ARRAY.getClass());
        } catch (NoSuchFieldException e) {
            throw new Error(e);
        }
    }

    private Object f = new Object();

    private long l1, l2;

    static Object testFixedOffsetField(Object o) {
        return UNSAFE.getObject(o, F_OFFSET);
    }

    static int testFixedOffsetHeader0(Object o) {
        return UNSAFE.getInt(o, 0);
    }

    static int testFixedOffsetHeader4(Object o) {
        return UNSAFE.getInt(o, 4);
    }

    static int testFixedOffsetHeader8(Object o) {
        return UNSAFE.getInt(o, 8);
    }

    static int testFixedOffsetHeader12(Object o) {
        return UNSAFE.getInt(o, 12);
    }

    static int testFixedOffsetHeader16(Object o) {
        return UNSAFE.getInt(o, 16);
    }

    static Object testFixedBase(long off) {
        return UNSAFE.getObject(INSTANCE, off);
    }

    static Object testOpaque(Object o, long off) {
        return UNSAFE.getObject(o, off);
    }

    static int testFixedOffsetHeaderArray0(Object[] arr) {
        return UNSAFE.getInt(arr, 0);
    }

    static int testFixedOffsetHeaderArray4(Object[] arr) {
        return UNSAFE.getInt(arr, 4);
    }

    static int testFixedOffsetHeaderArray8(Object[] arr) {
        return UNSAFE.getInt(arr, 8);
    }

    static int testFixedOffsetHeaderArray12(Object[] arr) {
        return UNSAFE.getInt(arr, 12);
    }

    static int testFixedOffsetHeaderArray16(Object[] arr) {
        return UNSAFE.getInt(arr, 16);
    }

    static Object testFixedOffsetArray(Object[] arr) {
        return UNSAFE.getObject(arr, E_OFFSET);
    }

    static Object testFixedBaseArray(long off) {
        return UNSAFE.getObject(ARRAY, off);
    }

    static Object testOpaqueArray(Object[] o, long off) {
        return UNSAFE.getObject(o, off);
    }

    static final long ADDR = UNSAFE.allocateMemory(10);

    static boolean flag;

    static int testMixedAccess() {
        flag = !flag;
        Object o = (flag ? INSTANCE : null);
        long off = (flag ? F_OFFSET : ADDR);
        return UNSAFE.getInt(o, off);
    }

    public static void main(String[] args) {
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 20_000; i++) {
            testFixedOffsetField(INSTANCE);
            testFixedOffsetHeader0(INSTANCE);
            testFixedOffsetHeader4(INSTANCE);
            testFixedOffsetHeader8(INSTANCE);
            testFixedOffsetHeader12(INSTANCE);
            testFixedOffsetHeader16(INSTANCE);
            testFixedBase(F_OFFSET);
            testOpaque(INSTANCE, F_OFFSET);
            testMixedAccess();
            testFixedOffsetHeaderArray0(ARRAY);
            testFixedOffsetHeaderArray4(ARRAY);
            testFixedOffsetHeaderArray8(ARRAY);
            testFixedOffsetHeaderArray12(ARRAY);
            testFixedOffsetHeaderArray16(ARRAY);
            testFixedOffsetArray(ARRAY);
            testFixedBaseArray(E_OFFSET);
            testOpaqueArray(ARRAY, E_OFFSET);
        }
        System.out.println("TEST PASSED");
    }
}
