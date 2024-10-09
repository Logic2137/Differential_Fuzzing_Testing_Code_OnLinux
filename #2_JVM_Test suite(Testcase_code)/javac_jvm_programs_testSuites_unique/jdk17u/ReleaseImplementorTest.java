import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ReleaseImplementorTest {

    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        Path release = Paths.get(System.getProperty("test.jdk"), "release");
        try (InputStream in = Files.newInputStream(release)) {
            props.load(in);
        }
        if (!props.containsKey("IMPLEMENTOR")) {
            throw new RuntimeException("IMPLEMENTOR key is missing");
        }
        String implementor = props.getProperty("IMPLEMENTOR");
        if (implementor.length() < 3) {
            throw new RuntimeException("IMPLEMENTOR value is not expected length");
        }
        if (implementor.charAt(0) != '"' || implementor.charAt(implementor.length() - 1) != '"') {
            throw new RuntimeException("IMPLEMENTOR value not quoted property");
        }
        System.out.println("IMPLEMENTOR is " + implementor);
    }
}
