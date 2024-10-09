import java.util.concurrent.ExecutorService;

public abstract class MissingReturnConstraintTest {

    void f(ExecutorService s) {
        s.submit(() -> run(() -> {
        }));
    }

    abstract <E extends Throwable> void run(ThrowableRunnable<E> action) throws E;

    public interface ThrowableRunnable<T extends Throwable> {

        void run() throws T;
    }
}
