import java.lang.ref.*;

public class TestParallelRefprocSanity {

    static final long TARGET_MB = Long.getLong("target", 10_000);

    static volatile Object sink;

    public static void main(String[] args) throws Exception {
        long count = TARGET_MB * 1024 * 1024 / 32;
        for (long c = 0; c < count; c++) {
            sink = new WeakReference<Object>(new Object());
        }
    }
}
