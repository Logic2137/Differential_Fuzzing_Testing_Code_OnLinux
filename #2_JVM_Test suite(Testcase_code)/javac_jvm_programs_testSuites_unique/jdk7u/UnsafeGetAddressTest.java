



import sun.misc.Unsafe;
import java.lang.reflect.*;

public class UnsafeGetAddressTest {
    private static Unsafe unsafe;

    public static void main(String[] args) throws Exception {
        Class c = UnsafeGetAddressTest.class.getClassLoader().loadClass("sun.misc.Unsafe");
        Field f = c.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        unsafe = (Unsafe)f.get(c);

        long address = unsafe.allocateMemory(unsafe.addressSize());
        unsafe.putAddress(address, 0x0000000080000000L);
        
        
        
        result = unsafe.getAddress(address);
        System.out.printf("1: was 0x%x, expected 0x%x\n", result,
                0x0000000080000000L);
        for (int i = 0; i < 1000000; i++) {
            result = unsafe.getAddress(address);
        }

        
        System.out.printf("2: was 0x%x, expected 0x%x\n", result,
                0x0000000080000000L);
        if (result != 0x0000000080000000L) {
            System.out.println("Test Failed");
            System.exit(97);
        } else {
            System.out.println("Test Passed");
        }
    }
    static volatile long result;
}

