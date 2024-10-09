

import sun.misc.Unsafe;

public class UnsafeAccess {
    private static final Unsafe U = Unsafe.getUnsafe();

    static Class cls = Object.class;
    static long off = U.ARRAY_OBJECT_BASE_OFFSET;

    static Object testUnsafeAccess(Object o, boolean isObjArray) {
        if (o != null && cls.isInstance(o)) { 
            return helperUnsafeAccess(o, isObjArray);
        }
        return null;
    }

    static Object helperUnsafeAccess(Object o, boolean isObjArray) {
        if (isObjArray) {
            U.putObject(o, off, new Object());
        }
        return o;
    }

    static Object testUnsafeLoadStore(Object o, boolean isObjArray) {
        if (o != null && cls.isInstance(o)) { 
            return helperUnsafeLoadStore(o, isObjArray);
        }
        return null;
    }

    static Object helperUnsafeLoadStore(Object o, boolean isObjArray) {
        if (isObjArray) {
            Object o1 = U.getObject(o, off);
            U.compareAndSwapObject(o, off, o1, new Object());
        }
        return o;
    }

    public static void main(String[] args) {
        Object[] objArray = new Object[10];
        int[]    intArray = new    int[10];

        for (int i = 0; i < 20_000; i++) {
            helperUnsafeAccess(objArray, true);
        }
        for (int i = 0; i < 20_000; i++) {
            testUnsafeAccess(intArray, false);
        }

        for (int i = 0; i < 20_000; i++) {
            helperUnsafeLoadStore(objArray, true);
        }
        for (int i = 0; i < 20_000; i++) {
            testUnsafeLoadStore(intArray, false);
        }

        System.out.println("TEST PASSED");
    }
}
