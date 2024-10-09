



import java.io.File;
import java.io.PrintWriter;
import java.nio.file.attribute.FileTime;
import java.util.Date;
import java.util.TimeZone;
import java.util.spi.ToolProvider;

public class JarEntryTime {
    static final ToolProvider JAR_TOOL = ToolProvider.findFirst("jar")
        .orElseThrow(() ->
            new RuntimeException("jar tool not found")
        );


    
    
    static final long PRECISION = 10000L;

    static final TimeZone TZ = TimeZone.getDefault();
    static final boolean DST = TZ.inDaylightTime(new Date());

    static boolean cleanup(File dir) throws Throwable {
        boolean rc = true;
        File[] x = dir.listFiles();
        if (x != null) {
            for (int i = 0; i < x.length; i++) {
                rc &= x[i].delete();
            }
        }
        return rc & dir.delete();
    }

    static void extractJar(File jarFile, boolean useExtractionTime) throws Throwable {
        String javahome = System.getProperty("java.home");
        String jarcmd = javahome + File.separator + "bin" + File.separator + "jar";
        String[] args;
        if (useExtractionTime) {
            args = new String[] {
                jarcmd,
                "-J-Dsun.tools.jar.useExtractionTime=true",
                "xf",
                jarFile.getName() };
        } else {
            args = new String[] {
                jarcmd,
                "xf",
                jarFile.getName() };
        }
        Process p = Runtime.getRuntime().exec(args);
        check(p != null && (p.waitFor() == 0));
    }

    public static void realMain(String[] args) throws Throwable {

        File dirOuter = new File("outer");
        File dirInner = new File(dirOuter, "inner");
        File jarFile = new File("JarEntryTime.jar");
        File testFile = new File("JarEntryTimeTest.txt");

        
        cleanup(dirInner);
        cleanup(dirOuter);
        jarFile.delete();
        testFile.delete();

        
        check(dirOuter.mkdir());
        check(dirInner.mkdir());
        File fileInner = new File(dirInner, "foo.txt");
        try (PrintWriter pw = new PrintWriter(fileInner)) {
            pw.println("hello, world");
        }

        
        
        
        final long now = fileInner.lastModified();
        final long earlier = now - (60L * 60L * 6L * 1000L);
        final long yesterday = now - (60L * 60L * 24L * 1000L);

        check(dirOuter.setLastModified(now));
        check(dirInner.setLastModified(yesterday));
        check(fileInner.setLastModified(earlier));

        
        check(JAR_TOOL.run(System.out, System.err,
                           "cf", jarFile.getName(), dirOuter.getName()) == 0);
        check(jarFile.exists());

        check(cleanup(dirInner));
        check(cleanup(dirOuter));

        
        
        extractJar(jarFile, false);
        check(dirOuter.exists());
        check(dirInner.exists());
        check(fileInner.exists());
        checkFileTime(dirOuter.lastModified(), now);
        checkFileTime(dirInner.lastModified(), yesterday);
        checkFileTime(fileInner.lastModified(), earlier);

        check(cleanup(dirInner));
        check(cleanup(dirOuter));

        try (PrintWriter pw = new PrintWriter(testFile)) {
            pw.println("hello, world");
        }
        final long start = testFile.lastModified();

        
        extractJar(jarFile, true);

        try (PrintWriter pw = new PrintWriter(testFile)) {
            pw.println("hello, world");
        }
        final long end = testFile.lastModified();

        check(dirOuter.exists());
        check(dirInner.exists());
        check(fileInner.exists());
        checkFileTime(start, dirOuter.lastModified(), end);
        checkFileTime(start, dirInner.lastModified(), end);
        checkFileTime(start, fileInner.lastModified(), end);

        check(cleanup(dirInner));
        check(cleanup(dirOuter));

        check(jarFile.delete());
        check(testFile.delete());
    }

    static void checkFileTime(long now, long original) {
        if (isTimeSettingChanged()) {
            return;
        }

        if (Math.abs(now - original) > PRECISION) {
            System.out.format("Extracted to %s, expected to be close to %s%n",
                FileTime.fromMillis(now), FileTime.fromMillis(original));
            fail();
        }
    }

    static void checkFileTime(long start, long now, long end) {
        if (isTimeSettingChanged()) {
            return;
        }

        if (now < start || now > end) {
            System.out.format("Extracted to %s, "
                              + "expected to be after %s and before %s%n",
                              FileTime.fromMillis(now),
                              FileTime.fromMillis(start),
                              FileTime.fromMillis(end));
            fail();
        }
    }

    private static boolean isTimeSettingChanged() {
        TimeZone currentTZ = TimeZone.getDefault();
        boolean currentDST = currentTZ.inDaylightTime(new Date());
        return (!currentTZ.equals(TZ) || currentDST != DST);
    }

    
    static volatile int passed = 0, failed = 0;
    static void pass() {passed++;}
    static void fail() {failed++; Thread.dumpStack();}
    static void fail(String msg) {System.out.println(msg); fail();}
    static void unexpected(Throwable t) {failed++; t.printStackTrace();}
    static void check(boolean cond) {if (cond) pass(); else fail();}
    static void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y)) pass();
        else fail(x + " not equal to " + y);}
    public static void main(String[] args) throws Throwable {
        try {realMain(args);} catch (Throwable t) {unexpected(t);}
        System.out.println("\nPassed = " + passed + " failed = " + failed);
        if (failed > 0) throw new AssertionError("Some tests failed");}
}
