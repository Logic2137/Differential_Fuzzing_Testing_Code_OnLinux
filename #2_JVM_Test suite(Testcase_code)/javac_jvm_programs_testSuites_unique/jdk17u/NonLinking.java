import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;

public class NonLinking {

    public static void main(String[] args) throws Throwable {
        Path jarPath = Paths.get("classes.jar");
        URL url = jarPath.toUri().toURL();
        URLClassLoader ucl1 = new URLClassLoader("UCL1", new URL[] { url }, null);
        switch(args[0]) {
            case "init":
                try {
                    Class.forName("Container", true, ucl1);
                    throw new RuntimeException("Missed expected NoClassDefFoundError");
                } catch (NoClassDefFoundError expected) {
                    final String CLASSNAME = "MissingClass";
                    Throwable cause = expected.getCause();
                    if (!cause.getMessage().contains(CLASSNAME)) {
                        throw new RuntimeException("Cause of NoClassDefFoundError does not contain \"" + CLASSNAME + "\"", cause);
                    }
                }
                break;
            case "load":
                Class.forName("Container", false, ucl1);
                break;
            default:
                throw new RuntimeException("Unknown command: " + args[0]);
        }
    }
}
