import java.io.*;
import java.util.*;
import java.util.zip.*;

public class TotalInOut {

    static final int BUF_SIZE = 1 * 1024 * 1024;

    static void realMain(String[] args) throws Throwable {
        long dataSize = 128L * 1024L * 1024L;
        if (args.length > 0 && "large".equals(args[0]))
            dataSize = 5L * 1024L * 1024L * 1024L;
        Deflater deflater = new Deflater();
        Inflater inflater = new Inflater();
        byte[] dataIn = new byte[BUF_SIZE];
        byte[] dataOut = new byte[BUF_SIZE];
        byte[] tmp = new byte[BUF_SIZE];
        Random r = new Random();
        r.nextBytes(dataIn);
        long bytesReadDef = 0;
        long bytesWrittenDef = 0;
        long bytesReadInf = 0;
        long bytesWrittenInf = 0;
        deflater.setInput(dataIn, 0, dataIn.length);
        while (bytesReadDef < dataSize || bytesWrittenInf < dataSize) {
            int len = r.nextInt(BUF_SIZE / 2) + BUF_SIZE / 2;
            if (deflater.needsInput()) {
                bytesReadDef += dataIn.length;
                check(bytesReadDef == deflater.getBytesRead());
                deflater.setInput(dataIn, 0, dataIn.length);
            }
            int n = deflater.deflate(tmp, 0, len);
            bytesWrittenDef += n;
            check(bytesWrittenDef == deflater.getBytesWritten());
            inflater.setInput(tmp, 0, n);
            bytesReadInf += n;
            while (!inflater.needsInput()) {
                bytesWrittenInf += inflater.inflate(dataOut, 0, dataOut.length);
                check(bytesWrittenInf == inflater.getBytesWritten());
            }
            check(bytesReadInf == inflater.getBytesRead());
        }
    }

    static volatile int passed = 0, failed = 0;

    static void pass() {
        passed++;
    }

    static void pass(String msg) {
        System.out.println(msg);
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

    static void unexpected(Throwable t, String msg) {
        System.out.println(msg);
        failed++;
        t.printStackTrace();
    }

    static boolean check(boolean cond) {
        if (cond)
            pass();
        else
            fail();
        return cond;
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
