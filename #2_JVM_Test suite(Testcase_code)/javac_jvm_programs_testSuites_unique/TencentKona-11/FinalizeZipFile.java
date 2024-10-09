import java.io.*;
import java.util.Random;
import java.util.zip.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class FinalizeZipFile {

    private static final CountDownLatch finalizersDone = new CountDownLatch(3);

    private static class InstrumentedZipFile extends ZipFile {

        public InstrumentedZipFile(File f) throws Exception {
            super(f);
            System.out.printf("Using %s%n", f.getPath());
        }

        @Override
        protected void finalize() throws IOException {
            System.out.printf("Killing %s%n", getName());
            super.finalize();
            finalizersDone.countDown();
        }
    }

    private static void makeGarbage() throws Throwable {
        final Random rnd = new Random();
        final File testdir = new File(System.getProperty("test.src", "."));
        check(testdir.isDirectory());
        final File[] jars = testdir.listFiles(new FilenameFilter() {

            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });
        check(jars.length > 1);
        new InstrumentedZipFile(jars[rnd.nextInt(jars.length)]).close();
        new InstrumentedZipFile(jars[rnd.nextInt(jars.length)]).close();
        for (int i = 0; i < jars.length + 10; i++) {
            ZipFile zf = new InstrumentedZipFile(jars[rnd.nextInt(jars.length)]);
            ZipEntry ze = zf.getEntry("META-INF/MANIFEST.MF");
            if (ze != null) {
                InputStream is = zf.getInputStream(ze);
                break;
            }
        }
    }

    public static void realMain(String[] args) throws Throwable {
        makeGarbage();
        while (!finalizersDone.await(10, TimeUnit.MILLISECONDS)) {
            System.gc();
        }
        equal(finalizersDone.getCount(), 0L);
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
        System.out.printf("%nPassed = %d, failed = %d%n%n", passed, failed);
        if (failed > 0)
            throw new AssertionError("Some tests failed");
    }
}
