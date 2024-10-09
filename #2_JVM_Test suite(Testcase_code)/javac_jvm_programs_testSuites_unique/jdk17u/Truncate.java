import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.concurrent.Callable;

public class Truncate {

    static final long INITIAL_FILE_SIZE = 32000L;

    static final long TRUNCATED_FILE_SIZE = 512L;

    public static void main(String[] args) throws Exception {
        File blah = File.createTempFile("blah", null);
        blah.deleteOnExit();
        final FileChannel fc = new RandomAccessFile(blah, "rw").getChannel();
        fc.position(INITIAL_FILE_SIZE).write(ByteBuffer.wrap("THE END".getBytes()));
        final MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_WRITE, 0, fc.size());
        boolean truncated;
        try {
            fc.truncate(TRUNCATED_FILE_SIZE);
            truncated = true;
        } catch (IOException ioe) {
            truncated = false;
        }
        if (truncated) {
            execute(new Callable<Void>() {

                public Void call() {
                    mbb.get((int) TRUNCATED_FILE_SIZE + 1);
                    mbb.put((int) TRUNCATED_FILE_SIZE + 2, (byte) 123);
                    return null;
                }
            });
            execute(new Callable<Void>() {

                public Void call() throws IOException {
                    mbb.load();
                    return null;
                }
            });
        }
        fc.close();
    }

    static void execute(final Callable<?> c) {
        Runnable r = new Runnable() {

            public void run() {
                try {
                    Object ignore = c.call();
                } catch (Exception ignore) {
                }
            }
        };
        Thread t = new Thread(r);
        t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException ignore) {
        }
    }
}
