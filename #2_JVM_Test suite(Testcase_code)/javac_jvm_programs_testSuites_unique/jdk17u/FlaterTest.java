import java.nio.*;
import java.util.*;
import java.util.zip.*;

public class FlaterTest extends Thread {

    private static final int DATA_LEN = 1024 * 128;

    private static ByteBuffer dataDirect;

    private static ByteBuffer dataHeap;

    private static final boolean debug = false;

    private static Set<Flater> flaters = Collections.synchronizedSet(new HashSet<>());

    static void createData() {
        ByteBuffer bb = ByteBuffer.allocateDirect(DATA_LEN * 8);
        for (int i = 0; i < DATA_LEN * 8; i += 8) {
            bb.putDouble(i, Math.random());
        }
        dataDirect = bb;
        final ByteBuffer hb = ByteBuffer.allocate(bb.capacity());
        hb.duplicate().put(bb.duplicate());
        dataHeap = hb;
        if (debug)
            System.out.println("data length is " + bb.capacity());
    }

    private static int getDeflatedLength() {
        Deflater deflater = new Deflater();
        deflater.setInput(dataDirect.duplicate());
        deflater.finish();
        byte[] out = new byte[dataDirect.capacity()];
        int rc = deflater.deflate(out);
        deflater.end();
        if (debug)
            System.out.println("deflatedLength is " + rc);
        return rc;
    }

    private static void validate(ByteBuffer buf, int offset, int len) throws Exception {
        for (int i = 0; i < len; i++) {
            if (buf.get(i) != dataDirect.get(offset + i)) {
                throw new Exception("mismatch at " + (offset + i));
            }
        }
    }

    public static void realMain(String[] args) {
        int numThreads = args.length > 0 ? Integer.parseInt(args[0]) : 5;
        createData();
        for (int srcMode = 0; srcMode <= 2; srcMode++) {
            for (int dstMode = 0; dstMode <= 2; dstMode++) {
                new FlaterTest().go(numThreads, srcMode, dstMode);
            }
        }
    }

    private synchronized void go(int numThreads, int srcMode, int dstMode) {
        int deflatedLength = getDeflatedLength();
        long time = System.currentTimeMillis();
        for (int i = 0; i < numThreads; i++) {
            Flater f = new Flater(deflatedLength, srcMode, dstMode);
            flaters.add(f);
            f.start();
        }
        synchronized (flaters) {
            while (flaters.size() != 0) {
                try {
                    flaters.wait();
                } catch (InterruptedException ex) {
                    unexpected(ex);
                }
            }
        }
        time = System.currentTimeMillis() - time;
        System.out.println("Time needed for " + numThreads + " threads to deflate/inflate: " + time + " ms (srcMode=" + srcMode + ",dstMode=" + dstMode + ")");
    }

    static class Flater extends Thread {

        private final int deflatedLength;

        private final int srcMode, dstMode;

        private Flater(int length, int srcMode, int dstMode) {
            this.deflatedLength = length;
            this.srcMode = srcMode;
            this.dstMode = dstMode;
        }

        public void run() {
            if (debug)
                System.out.println(getName() + " starting run()");
            try {
                ByteBuffer deflated = DeflateData(deflatedLength);
                InflateData(deflated);
            } catch (Throwable t) {
                t.printStackTrace();
                fail(getName() + " failed");
            } finally {
                synchronized (flaters) {
                    flaters.remove(this);
                    if (flaters.isEmpty()) {
                        flaters.notifyAll();
                    }
                }
            }
        }

        private ByteBuffer DeflateData(int length) {
            Deflater deflater = new Deflater();
            if (srcMode == 0) {
                deflater.setInput(dataHeap.array());
            } else if (srcMode == 1) {
                deflater.setInput(dataHeap.duplicate());
            } else {
                assert srcMode == 2;
                deflater.setInput(dataDirect.duplicate());
            }
            deflater.finish();
            ByteBuffer out = dstMode == 2 ? ByteBuffer.allocateDirect(length) : ByteBuffer.allocate(length);
            int deflated;
            if (dstMode == 0) {
                deflated = deflater.deflate(out.array(), 0, length);
                out.position(deflated);
            } else {
                deflater.deflate(out);
            }
            out.flip();
            return out;
        }

        private void InflateData(ByteBuffer bytes) throws Throwable {
            Inflater inflater = new Inflater();
            if (dstMode == 0) {
                inflater.setInput(bytes.array(), 0, bytes.remaining());
            } else {
                inflater.setInput(bytes);
            }
            if (inflater.getRemaining() == 0) {
                throw new Exception("Nothing to inflate (bytes=" + bytes + ")");
            }
            int len = 1024 * 8;
            int offset = 0;
            ByteBuffer buf = srcMode == 2 ? ByteBuffer.allocateDirect(len) : ByteBuffer.allocate(len);
            while (inflater.getRemaining() > 0) {
                buf.clear();
                int inflated;
                if (srcMode == 0) {
                    inflated = inflater.inflate(buf.array(), 0, buf.remaining());
                } else {
                    inflated = inflater.inflate(buf);
                }
                if (inflated == 0) {
                    throw new Exception("Nothing inflated (dst=" + buf + ",offset=" + offset + ",rem=" + inflater.getRemaining() + ",srcMode=" + srcMode + ",dstMode=" + dstMode + ")");
                }
                validate(buf, offset, inflated);
                offset += inflated;
            }
        }
    }

    static volatile int passed = 0, failed = 0;

    static void pass() {
        passed++;
    }

    static void fail() {
        failed++;
        Thread.dumpStack();
    }

    static void fail(String msg) {
        System.out.println(msg);
        fail();
    }

    static void unexpected(Throwable t) {
        failed++;
        t.printStackTrace();
    }

    static void check(boolean cond) {
        if (cond)
            pass();
        else
            fail();
    }

    static void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y))
            pass();
        else
            fail(x + " not equal to " + y);
    }

    public static void main(String[] args) throws Throwable {
        try {
            realMain(args);
        } catch (Throwable t) {
            unexpected(t);
        }
        System.out.println("\nPassed = " + passed + " failed = " + failed);
        if (failed > 0)
            throw new AssertionError("Some tests failed");
    }
}
