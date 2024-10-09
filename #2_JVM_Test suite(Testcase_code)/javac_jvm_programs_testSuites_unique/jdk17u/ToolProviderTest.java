import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.spi.ToolProvider;

public class ToolProviderTest {

    public static void main(String... args) throws Exception {
        ToolProviderTest t = new ToolProviderTest();
        t.run();
    }

    void run() throws Exception {
        initServices();
        System.out.println("Validate an NPE is thrown with null arguments");
        testNullArgs();
        System.out.println("test without security manager present:");
        test();
        System.setSecurityManager(new SecurityManager());
        System.out.println("test with security manager present:");
        test();
    }

    private void test() throws Exception {
        ToolProvider testProvider = ToolProvider.findFirst("test").get();
        int rc = testProvider.run(System.out, System.err, "hello test");
        if (rc != 0) {
            throw new Exception("unexpected exit code: " + rc);
        }
    }

    private void testNullArgs() {
        ToolProvider testProvider = ToolProvider.findFirst("test").get();
        expectNullPointerException(() -> testProvider.run(null, System.err));
        expectNullPointerException(() -> testProvider.run(System.out, null));
        expectNullPointerException(() -> testProvider.run(System.out, System.err, (String[]) null));
        expectNullPointerException(() -> testProvider.run(System.out, System.err, (String) null));
    }

    private static void expectNullPointerException(Runnable test) {
        try {
            test.run();
            throw new Error("NullPointerException not thrown");
        } catch (NullPointerException e) {
        }
    }

    private void initServices() throws IOException {
        Path testClasses = Paths.get(System.getProperty("test.classes"));
        Path services = testClasses.resolve(Paths.get("META-INF", "services"));
        Files.createDirectories(services);
        Files.write(services.resolve(ToolProvider.class.getName()), Arrays.asList(TestProvider.class.getName()));
    }

    public static class TestProvider implements ToolProvider {

        public TestProvider() {
            checkPrivileges();
        }

        public String name() {
            return "test";
        }

        public int run(PrintWriter out, PrintWriter err, String... args) {
            out.println("Test: " + Arrays.toString(args));
            return 0;
        }

        private void checkPrivileges() {
            boolean haveSecurityManager = (System.getSecurityManager() != null);
            try {
                System.getProperty("java.home");
                if (haveSecurityManager) {
                    throw new Error("exception exception not thrown");
                }
            } catch (SecurityException e) {
                if (!haveSecurityManager) {
                    throw new Error("unexpected exception: " + e);
                }
            }
        }
    }
}
