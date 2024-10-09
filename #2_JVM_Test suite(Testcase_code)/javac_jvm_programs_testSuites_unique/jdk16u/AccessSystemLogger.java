
import java.io.IOException;
import java.lang.System.Logger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ResourceBundle;


public final class AccessSystemLogger {

    public AccessSystemLogger() {
        this(check());
    }

    private AccessSystemLogger(Void unused) {
    }

    private static Void check() {
        if (AccessSystemLogger.class.getClassLoader() != null) {
            throw new RuntimeException("AccessSystemLogger should be loaded by the null classloader");
        }
        return null;
    }

    public Logger getLogger(String name) {
        Logger logger = System.getLogger(name);
        System.out.println("System.getLogger(\"" + name + "\"): " + logger);
        return logger;
    }

    public Logger getLogger(String name, ResourceBundle bundle) {
        Logger logger = System.getLogger(name, bundle);
        System.out.println("System.getLogger(\"" + name + "\", bundle): " + logger);
        return logger;
    }

    public java.util.logging.Logger demandSystemLogger(String name) {
        return java.util.logging.Logger.getLogger(name);
    }

    
    public static void main(String[] args) throws IOException {
        Path testDir = Paths.get(System.getProperty("user.dir", "."));
        Path bootDir = Paths.get(testDir.toString(), "boot");
        Path classes = Paths.get(System.getProperty("test.classes", "build/classes"));
        Path thisClass = Paths.get(classes.toString(),
                AccessSystemLogger.class.getSimpleName()+".class");
        if (Files.notExists(bootDir)) {
            Files.createDirectory(bootDir);
        }
        Path dest = Paths.get(bootDir.toString(),
                AccessSystemLogger.class.getSimpleName()+".class");
        Files.copy(thisClass, dest, StandardCopyOption.REPLACE_EXISTING);
    }

}
