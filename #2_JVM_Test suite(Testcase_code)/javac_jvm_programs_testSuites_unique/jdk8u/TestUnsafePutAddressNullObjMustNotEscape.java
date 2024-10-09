import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class TestUnsafePutAddressNullObjMustNotEscape {

    public static Unsafe usafe;

    public static long mem;

    public static long checksum;

    public static void main(String[] args) throws Exception {
        System.out.println("EXECUTING test.");
        {
            System.out.println("Acquiring sun.misc.Unsafe.theUnsafe using reflection.");
            getUnsafe();
            System.out.println("Allocating raw memory.");
            mem = (usafe.allocateMemory(1024) + 8L) & ~7L;
            System.out.println("Triggering JIT compilation of the test method");
            triggerJitCompilationOfTestMethod();
        }
        System.out.println("SUCCESSFULLY passed test.");
    }

    public static void triggerJitCompilationOfTestMethod() {
        long sum = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int ii = 50000; ii >= 0; ii--) {
            sum = testMethod();
        }
        checksum = sum;
    }

    public static class IDGen {

        private static long id;

        public long nextId() {
            return id++;
        }
    }

    public static long testMethod() {
        IDGen gen = new IDGen();
        usafe.putAddress(mem, 0L);
        return gen.nextId();
    }

    private static void getUnsafe() throws Exception {
        Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
        field.setAccessible(true);
        usafe = (sun.misc.Unsafe) field.get(null);
    }
}
