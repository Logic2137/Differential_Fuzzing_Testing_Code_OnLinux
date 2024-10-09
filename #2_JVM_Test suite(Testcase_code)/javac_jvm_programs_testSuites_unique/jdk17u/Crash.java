import sun.misc.Unsafe;
import java.lang.reflect.Field;

public class Crash {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe");
        f.setAccessible(true);
        Unsafe u = (Unsafe) f.get(null);
        u.setMemory(0, 42, (byte) 0xFF);
    }
}
