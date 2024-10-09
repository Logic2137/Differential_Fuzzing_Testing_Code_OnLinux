import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.file.*;
import static java.nio.file.StandardOpenOption.*;

public class InterruptDeadlock {

    static class Reader extends Thread {

        final FileChannel fc;

        volatile Exception exception;

        Reader(FileChannel fc) {
            this.fc = fc;
        }

        @Override
        public void run() {
            ByteBuffer bb = ByteBuffer.allocate(1024);
            try {
                long pos = 0L;
                for (; ; ) {
                    bb.clear();
                    int n = fc.read(bb, pos);
                    if (n > 0)
                        pos += n;
                    if (pos >= fc.size())
                        pos = 0L;
                }
            } catch (ClosedChannelException x) {
                System.out.println(x.getClass() + " (expected)");
            } catch (Exception unexpected) {
                this.exception = unexpected;
            }
        }

        Exception exception() {
            return exception;
        }

        static Reader startReader(FileChannel fc) {
            Reader r = new Reader(fc);
            r.start();
            return r;
        }
    }

    private static final int READER_COUNT = 4;

    public static void main(String[] args) throws Exception {
        Path file = Paths.get("data.txt");
        try (FileChannel fc = FileChannel.open(file, CREATE, TRUNCATE_EXISTING, WRITE)) {
            fc.position(1024L * 1024L);
            fc.write(ByteBuffer.wrap(new byte[1]));
        }
        Reader[] readers = new Reader[READER_COUNT];
        for (int i = 1; i <= 20; i++) {
            System.out.format("Iteration: %s%n", i);
            try (FileChannel fc = FileChannel.open(file)) {
                boolean failed = false;
                for (int j = 0; j < READER_COUNT; j++) {
                    readers[j] = Reader.startReader(fc);
                }
                Thread.sleep(100);
                for (Reader r : readers) {
                    r.interrupt();
                }
                for (Reader r : readers) {
                    try {
                        r.join(10000);
                        Exception e = r.exception();
                        if (e != null) {
                            System.err.println("Reader thread failed with: " + e);
                            failed = true;
                        }
                    } catch (InterruptedException x) {
                        System.err.println("Reader thread did not terminte");
                        failed = true;
                    }
                }
                if (fc.isOpen()) {
                    System.err.println("FileChannel was not closed");
                    failed = true;
                }
                if (failed)
                    throw new RuntimeException("Test failed - see log for details");
            }
        }
    }
}
