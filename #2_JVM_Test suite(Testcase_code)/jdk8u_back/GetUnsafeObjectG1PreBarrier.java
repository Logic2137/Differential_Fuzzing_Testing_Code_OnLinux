import java.lang.reflect.Field;
import sun.misc.Unsafe;

public class GetUnsafeObjectG1PreBarrier {

    private static final Unsafe unsafe;

    private static final int N = 100_000;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    public Object a;

    public static void main(String[] args) throws Throwable {
        new GetUnsafeObjectG1PreBarrier();
    }

    public GetUnsafeObjectG1PreBarrier() throws Throwable {
        doit();
    }

    private void doit() throws Throwable {
        Field field = GetUnsafeObjectG1PreBarrier.class.getField("a");
        long fieldOffset = unsafe.objectFieldOffset(field);
        for (int i = 0; i < N; i++) {
            readField(this, fieldOffset);
        }
    }

    private void readField(Object o, long fieldOffset) {
        unsafe.getObject(o, fieldOffset);
    }
}
