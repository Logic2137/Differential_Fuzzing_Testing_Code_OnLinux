import java.io.*;
import java.util.zip.*;

public class ConstructInflaterOutput {

    static class MyInflater extends Inflater {

        volatile boolean ended = false;

        public void end() {
            ended = true;
            super.end();
        }
    }

    public static void realMain(String[] args) throws Throwable {
        final MyInflater inf = new MyInflater();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        InflaterOutputStream ios = null;
        byte[] b = new byte[512];
        try {
            ios = new InflaterOutputStream(null);
            fail();
        } catch (NullPointerException ex) {
            pass();
        }
        try {
            ios = new InflaterOutputStream(baos, null);
            fail();
        } catch (NullPointerException ex) {
            pass();
        }
        try {
            ios = new InflaterOutputStream(baos, inf, 0);
            fail();
        } catch (IllegalArgumentException ex) {
            pass();
        }
        ios = new InflaterOutputStream(baos, inf);
        try {
            ios.write(null, 5, 2);
            fail();
        } catch (NullPointerException ex) {
            pass();
        }
        try {
            ios.write(b, -1, 0);
            fail();
        } catch (IndexOutOfBoundsException ex) {
            pass();
        }
        try {
            ios.write(b, 0, -1);
            fail();
        } catch (IndexOutOfBoundsException ex) {
            pass();
        }
        try {
            ios.write(b, 0, 600);
            fail();
        } catch (IndexOutOfBoundsException ex) {
            pass();
        }
        ios.flush();
        check(!inf.ended);
        ios.flush();
        check(!inf.ended);
        ios.finish();
        check(!inf.ended);
        ios.close();
        check(!inf.ended);
        try {
            ios.finish();
            fail();
        } catch (IOException ex) {
            pass();
        }
        try {
            ios.write(13);
            fail();
        } catch (IOException ex) {
            pass();
        }
        ios = new InflaterOutputStream(baos);
        ios.flush();
        ios.finish();
        ios.close();
        try {
            ios.flush();
        } catch (IOException ex) {
            pass();
        }
        java.lang.ref.Reference.reachabilityFence(inf);
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
