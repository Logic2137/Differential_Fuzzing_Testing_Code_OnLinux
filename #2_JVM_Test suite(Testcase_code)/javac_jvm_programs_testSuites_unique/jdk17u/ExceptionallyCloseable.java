import java.io.Closeable;
import java.io.IOException;

public interface ExceptionallyCloseable extends Closeable {

    public default void closeExceptionally(Throwable cause) throws IOException {
        close();
    }

    public static void close(Throwable t, Closeable c) throws IOException {
        if (c instanceof ExceptionallyCloseable) {
            ((ExceptionallyCloseable) c).closeExceptionally(t);
        } else {
            c.close();
        }
    }
}
