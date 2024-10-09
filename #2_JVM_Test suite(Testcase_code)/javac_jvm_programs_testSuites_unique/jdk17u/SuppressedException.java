import java.io.BufferedOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class SuppressedException {

    private static final String CLOSE_MESSAGE = "Close exception";

    private static final String FLUSH_MESSAGE = "Flush exception";

    private static final String SAME_MESSAGE = "Same exception";

    public static void main(String[] args) throws java.io.IOException {
        SuppressedException test = new SuppressedException();
        test.test();
    }

    private FilterOutputStream createOutputStream(OutputStream out, boolean isBuffered) {
        return isBuffered ? new BufferedOutputStream(out) : new FilterOutputStream(out);
    }

    private void test() {
        int failures = 0;
        FilterOutputStream buf;
        boolean[] isBuffered = new boolean[] { false, true };
        for (boolean buffered : isBuffered) {
            System.err.println("\n>>> Buffered: " + buffered + " <<<");
            System.err.flush();
            try {
                buf = createOutputStream(new OutputStreamFailsWithException(), buffered);
                buf.close();
                System.err.println("\nNo IOException thrown for same exception");
                failures++;
            } catch (IOException expected) {
                if (!expected.getMessage().equals(SAME_MESSAGE)) {
                    System.err.println("\nIOException with unexpected message thrown");
                    expected.printStackTrace();
                    failures++;
                }
            } catch (IllegalArgumentException unexpected) {
                System.err.println("\nUnexpected IllegalArgumentException thrown");
                unexpected.printStackTrace();
                failures++;
            }
            try {
                buf = createOutputStream(new OutputStreamFailsWithException(false, false), buffered);
                buf.close();
            } catch (IOException e) {
                System.err.println("\nUnexpected IOException thrown");
                e.printStackTrace();
                failures++;
            }
            try {
                buf = createOutputStream(new OutputStreamFailsWithException(true, false), buffered);
                buf.close();
            } catch (IOException e) {
                if (!e.getMessage().equals(CLOSE_MESSAGE)) {
                    System.err.println("\nIOException with unexpected message thrown");
                    e.printStackTrace();
                    failures++;
                }
            }
            try {
                buf = createOutputStream(new OutputStreamFailsWithException(false, true), buffered);
                buf.close();
            } catch (IOException e) {
                if (!e.getMessage().equals(FLUSH_MESSAGE)) {
                    System.err.println("\nIOException with unexpected message thrown");
                    e.printStackTrace();
                    failures++;
                }
            }
            try {
                buf = createOutputStream(new OutputStreamFailsWithException(true, true), buffered);
                buf.close();
            } catch (IOException e) {
                if (!e.getMessage().equals(CLOSE_MESSAGE)) {
                    System.err.println("\nIOException with unexpected message thrown");
                    e.printStackTrace();
                    failures++;
                }
                Throwable[] suppressed = e.getSuppressed();
                if (suppressed == null) {
                    System.err.println("\nExpected suppressed exception not present");
                    e.printStackTrace();
                    failures++;
                } else if (suppressed.length != 1) {
                    System.err.println("\nUnexpected number of suppressed exceptions");
                    e.printStackTrace();
                    failures++;
                } else if (!(suppressed[0] instanceof IOException)) {
                    System.err.println("\nSuppressed exception is not an IOException");
                    e.printStackTrace();
                    failures++;
                } else if (!suppressed[0].getMessage().equals(FLUSH_MESSAGE)) {
                    System.err.println("\nIOException with unexpected message thrown");
                    e.printStackTrace();
                    failures++;
                }
            }
        }
        if (failures > 0) {
            throw new RuntimeException("Test failed with " + failures + " errors");
        } else {
            System.out.println("Test succeeded.");
        }
    }

    class OutputStreamFailsWithException extends OutputStream {

        private final IOException sameException = new IOException(SAME_MESSAGE);

        private final Boolean throwSeparateCloseException;

        private final Boolean throwSeparateFlushException;

        OutputStreamFailsWithException() {
            throwSeparateCloseException = null;
            throwSeparateFlushException = null;
        }

        OutputStreamFailsWithException(boolean throwCloseException, boolean throwFlushException) {
            throwSeparateCloseException = throwCloseException;
            throwSeparateFlushException = throwFlushException;
        }

        @Override
        public void write(int i) throws IOException {
            throw new UnsupportedOperationException("");
        }

        @Override
        public void flush() throws IOException {
            System.out.println("flush()");
            if (throwSeparateFlushException == null) {
                throw sameException;
            } else if (throwSeparateFlushException) {
                throw new IOException(FLUSH_MESSAGE);
            }
        }

        @Override
        public void close() throws IOException {
            System.out.println("close()");
            if (throwSeparateCloseException == null) {
                throw sameException;
            } else if (throwSeparateCloseException) {
                throw new IOException(CLOSE_MESSAGE);
            }
        }
    }
}
