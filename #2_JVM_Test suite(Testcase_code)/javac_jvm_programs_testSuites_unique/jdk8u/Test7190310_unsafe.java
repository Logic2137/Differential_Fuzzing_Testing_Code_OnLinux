import java.lang.ref.*;
import java.lang.reflect.*;
import sun.misc.Unsafe;

public class Test7190310_unsafe {

    static class TestObject {

        public String toString() {
            return "TestObject";
        }
    }

    private static TestObject str = new TestObject();

    private static final WeakReference ref = new WeakReference(str);

    private TestObject obj;

    public static void main(String[] args) throws Exception {
        Class c = Test7190310_unsafe.class.getClassLoader().loadClass("sun.misc.Unsafe");
        Field f = c.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe unsafe = (Unsafe) f.get(c);
        f = Reference.class.getDeclaredField("referent");
        f.setAccessible(true);
        long referent_offset = unsafe.objectFieldOffset(f);
        Test7190310_unsafe t = new Test7190310_unsafe();
        TestObject o = new TestObject();
        t.obj = o;
        System.err.println("Warmup");
        Object obj = null;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 11000; i++) {
            obj = getRef0(ref);
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 11000; i++) {
            obj = getRef1(unsafe, ref, referent_offset);
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 11000; i++) {
            obj = getRef2(unsafe, ref, referent_offset);
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 11000; i++) {
            obj = getRef3(unsafe, ref, referent_offset);
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 11000; i++) {
            obj = getRef4(unsafe, t, referent_offset);
        }
        System.err.println("Verification");
        if (!verifyGet(referent_offset, unsafe)) {
            System.exit(97);
        }
        obj = getRef3(unsafe, t, referent_offset);
        if (obj != o) {
            System.out.println("FAILED: unsafe.getObject(Object, " + referent_offset + ") " + obj + " != " + o);
            System.exit(97);
        }
        obj = getRef4(unsafe, t, referent_offset);
        if (obj != o) {
            System.out.println("FAILED: unsafe.getObject(Test7190310, " + referent_offset + ") " + obj + " != " + o);
            System.exit(97);
        }
    }

    static boolean verifyGet(long referent_offset, Unsafe unsafe) throws Exception {
        System.out.println("referent: " + str);
        Object obj = getRef0(ref);
        if (obj != str) {
            System.out.println("FAILED: weakRef.get() " + obj + " != " + str);
            return false;
        }
        obj = getRef1(unsafe, ref, referent_offset);
        if (obj != str) {
            System.out.println("FAILED: unsafe.getObject(weakRef, " + referent_offset + ") " + obj + " != " + str);
            return false;
        }
        obj = getRef2(unsafe, ref, referent_offset);
        if (obj != str) {
            System.out.println("FAILED: unsafe.getObject(abstRef, " + referent_offset + ") " + obj + " != " + str);
            return false;
        }
        obj = getRef3(unsafe, ref, referent_offset);
        if (obj != str) {
            System.out.println("FAILED: unsafe.getObject(Object, " + referent_offset + ") " + obj + " != " + str);
            return false;
        }
        return true;
    }

    static Object getRef0(WeakReference ref) throws Exception {
        return ref.get();
    }

    static Object getRef1(Unsafe unsafe, WeakReference ref, long referent_offset) throws Exception {
        return unsafe.getObject(ref, referent_offset);
    }

    static Object getRef2(Unsafe unsafe, Reference ref, long referent_offset) throws Exception {
        return unsafe.getObject(ref, referent_offset);
    }

    static Object getRef3(Unsafe unsafe, Object ref, long referent_offset) throws Exception {
        return unsafe.getObject(ref, referent_offset);
    }

    static Object getRef4(Unsafe unsafe, Test7190310_unsafe ref, long referent_offset) throws Exception {
        return unsafe.getObject(ref, referent_offset);
    }
}
