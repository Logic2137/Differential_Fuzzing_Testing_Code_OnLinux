import java.util.Arrays;

public class TestCopyOfBrokenAntiDependency {

    public static void main(String[] args) {
        for (int i = 0; i < 20_000; i++) {
            test(100);
        }
    }

    private static Object test(int length) {
        Object[] src = new Object[length];
        final Object[] dst = Arrays.copyOf(src, 10);
        final Object[] dst2 = Arrays.copyOf(dst, 100);
        final Object v = dst[0];
        return v;
    }
}
