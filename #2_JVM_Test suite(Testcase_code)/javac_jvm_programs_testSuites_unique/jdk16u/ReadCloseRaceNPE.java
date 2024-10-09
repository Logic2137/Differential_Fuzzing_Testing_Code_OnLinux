


import java.io.CharArrayReader;
import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class ReadCloseRaceNPE {

    private static final int BUF_SIZE = 1000;
    private static final long TIMEOUT_MS = 3000;

    private static final List<Exception> failures = new ArrayList<>();

    private static void testReader(final Supplier<Reader> readerSupplier)
            throws InterruptedException {
        AtomicReference<Reader> readerRef =
            new AtomicReference<>(readerSupplier.get());

        AtomicBoolean isFinished = new AtomicBoolean();

        Runnable readTask = () -> {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() - startTime < TIMEOUT_MS) {
                try {
                    readerRef.get().read();
                } catch (Exception e) {
                    if (!(e instanceof IOException)) {
                        failures.add(e);
                        break;
                    }
                    readerRef.set(readerSupplier.get());
                }
            }
            isFinished.set(true);
        };

        Runnable closeTask = () -> {
            while (!isFinished.get()) {
                try {
                    readerRef.get().close();
                } catch (Exception e) {
                    if (!(e instanceof IOException)) {
                        e.printStackTrace();
                    }
                }
            }
        };

        Thread readThread = new Thread(readTask);
        Thread closeThread = new Thread(closeTask);

        readThread.start();
        closeThread.start();
        readThread.join();
        closeThread.join();
    }

    public static void main(String[] args) throws Throwable {
        final String s = "Two riders were approaching.\\n";

        Supplier<Reader> charPushbackReaderSupplier = () -> {
            char buf[] = new char[s.length()];
            s.getChars(0, s.length(), buf, 0);
            CharArrayReader in = new CharArrayReader(buf);
            return new PushbackReader(in, BUF_SIZE);
        };

        testReader(charPushbackReaderSupplier);

        Supplier<Reader> stringPushbackReaderSupplier = () -> {
            StringReader in = new StringReader(s);
            return new PushbackReader(in, BUF_SIZE);
        };

        testReader(stringPushbackReaderSupplier);

        if (!failures.isEmpty()) {
            failures.stream().forEach((x) -> ((Exception) x).printStackTrace());
            throw new RuntimeException("PushbackReaderNPE failed");
        }
    }
}
