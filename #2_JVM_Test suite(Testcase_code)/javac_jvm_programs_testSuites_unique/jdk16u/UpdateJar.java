



import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.Set;
import java.util.spi.ToolProvider;

public class UpdateJar {
    private static final ToolProvider JAR_TOOL = ToolProvider.findFirst("jar")
        .orElseThrow(() ->
            new RuntimeException("jar tool not found")
        );

    private static void cleanup(String... fnames) throws Throwable {
        for (String fname : fnames) {
            Files.deleteIfExists(Paths.get(fname));
        }
    }

    public static void realMain(String[] args) throws Throwable {
        if (!System.getProperty("os.name").startsWith("Windows")) {
            String jar = "testUpdateJar.jar";
            String e0  = "testUpdateJar_entry0.txt";
            String e1  = "testUpdateJar_entry1.txt";
            cleanup(jar, e0, e1);
            try {
                try (FileOutputStream fos0 = new FileOutputStream(e0);
                     FileOutputStream fos1 = new FileOutputStream(e1)) {
                    fos0.write(0);
                    fos1.write(0);
                }
                String[] jarArgs = new String[] {"cfM0", jar, e0};
                if (JAR_TOOL.run(System.out, System.err, jarArgs) != 0) {
                    fail("Could not create jar file.");
                }
                Set<PosixFilePermission> pm = Files.getPosixFilePermissions(Paths.get(jar));
                jarArgs = new String[] {"uf", jar, e1};
                if (JAR_TOOL.run(System.out, System.err, jarArgs) != 0) {
                    fail("Could not create jar file.");
                }
                equal(pm, Files.getPosixFilePermissions(Paths.get(jar)));
            } finally {
                cleanup(jar, e0, e1);
            }
        }
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
