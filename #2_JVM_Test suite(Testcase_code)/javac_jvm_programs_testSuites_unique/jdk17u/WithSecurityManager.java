import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;

public class WithSecurityManager {

    public static void main(String[] args) throws Exception {
        Path modulesPath = Paths.get(System.getProperty("java.home"), "lib", "modules");
        if (Files.notExists(modulesPath)) {
            System.out.printf("%s doesn't exist.", modulesPath.toString());
            System.out.println();
            System.out.println("It is most probably an exploded build." + " Skip the test.");
            return;
        }
        boolean allow = args[0].equals("allow");
        if (allow) {
            String testSrc = System.getProperty("test.src");
            if (testSrc == null)
                testSrc = ".";
            Path policyFile = Paths.get(testSrc, "java.policy");
            System.setProperty("java.security.policy", policyFile.toString());
        }
        FileSystems.getFileSystem(URI.create("jrt:/"));
        System.setSecurityManager(new SecurityManager());
        try {
            FileSystems.getFileSystem(URI.create("jrt:/"));
            if (!allow)
                throw new RuntimeException("access not expected");
        } catch (SecurityException se) {
            if (allow)
                throw se;
        }
        try {
            FileSystems.newFileSystem(URI.create("jrt:/"), Collections.emptyMap());
            if (!allow)
                throw new RuntimeException("access not expected");
        } catch (SecurityException se) {
            if (allow)
                throw se;
        }
        try {
            Paths.get(URI.create("jrt:/java.base/java/lang/Object.class"));
            if (!allow)
                throw new RuntimeException("access not expected");
        } catch (SecurityException se) {
            if (allow)
                throw se;
        }
    }
}
