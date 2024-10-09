import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.TimeUnit;

public class NegativeTimeout {

    public static void main(String[] args) throws Exception {
        FutureTask<Void> task = new FutureTask<>(() -> null);
        try {
            task.get(Long.MIN_VALUE, TimeUnit.NANOSECONDS);
        } catch (TimeoutException success) {
        }
    }
}
