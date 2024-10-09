import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class TestLoggingWithMainAppContext {

    public static void main(String[] args) throws IOException {
        System.out.println("Creating loggers.");
        final Logger foo1 = Logger.getLogger("foo");
        final Logger bar1 = Logger.getLogger("foo.bar");
        if (bar1.getParent() != foo1) {
            throw new RuntimeException("Parent logger of bar1 " + bar1 + " is not " + foo1);
        }
        System.out.println("bar1.getParent() is the same as foo1");
        System.setSecurityManager(new SecurityManager());
        System.out.println("Now running with security manager");
        ByteArrayInputStream is = new ByteArrayInputStream(new byte[] { 0, 1 });
        ImageIO.read(is);
        final Logger bar2 = Logger.getLogger("foo.bar");
        if (bar1 != bar2) {
            throw new RuntimeException("bar2 " + bar2 + " is not the same as bar1 " + bar1);
        }
        System.out.println("bar2 is the same as bar1");
        if (bar2.getParent() != foo1) {
            throw new RuntimeException("Parent logger of bar2 " + bar2 + " is not foo1 " + foo1);
        }
        System.out.println("bar2.getParent() is the same as foo1");
        final Logger foo2 = Logger.getLogger("foo");
        if (foo1 != foo2) {
            throw new RuntimeException("foo2 " + foo2 + " is not the same as foo1 " + foo1);
        }
        System.out.println("foo2 is the same as foo1");
        System.out.println("Test passed.");
    }
}
