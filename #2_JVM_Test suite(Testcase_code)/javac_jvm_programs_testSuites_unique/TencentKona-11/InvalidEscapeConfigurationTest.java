
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.LogManager;



public class InvalidEscapeConfigurationTest {

    public static void main(String[] args)
            throws UnsupportedEncodingException, IOException {
        String[] validEscapes = {
            "com.f\\u006fo.level = INF\\u004f",
            "com.f\\u006fo.level = INFO",
            "com.foo.level = INF\\u004f"
        };
        String[] invalidEscapes = {
            "com.fo\\u0O6f.level = INF\\u0O4f",
            "com.fo\\u0O6f.level = INFO",
            "com.foo.level = INF\\u0O4f"
        };
        for (String line : validEscapes) {
            test(line, true);
        }
        for (String line : invalidEscapes) {
            test(line, false);
        }
        try {
            Properties props = new Properties();
            props.load((InputStream)null);
            throw new RuntimeException("Properties.load(null): "
                    + "NullPointerException exception not raised");
        } catch (NullPointerException x) {
            System.out.println("Properties.load(null): "
                    + "got expected exception: " + x);
        }
        try {
            LogManager.getLogManager().readConfiguration(null);
            throw new RuntimeException("LogManager.readConfiguration(null): "
                    + "NullPointerException exception not raised");
        } catch (NullPointerException x) {
            System.out.println("LogManager.readConfiguration(null): "
                    + "got expected exception: " + x);
        }


    }

    public static void test(String line, boolean valid) throws IOException {
        String test = (valid ? "valid" : "invalid")
                + " line \"" +line + "\"";
        System.out.println("Testing " + test);

        
        try {
            ByteArrayInputStream bais =
                    new ByteArrayInputStream(line.getBytes("UTF-8"));
            Properties props = new Properties();
            props.load(bais);
            if (!valid) {
                throw new RuntimeException(test
                        + "\n\tProperties.load: expected exception not raised");
            } else {
                System.out.println("Properties.load passed for " + test);
            }
        } catch(IllegalArgumentException x) {
            if (!valid) {
                System.out.println(
                        "Properties.load: Got expected exception: "
                        + x + "\n\tfor " + test);
            } else {
                throw x;
            }
        }

        
        
        try {
            String content = defaultConfiguration() + '\n' + line + '\n';
            ByteArrayInputStream bais =
                    new ByteArrayInputStream(content.getBytes("UTF-8"));
            LogManager.getLogManager().readConfiguration(bais);
            if (!valid) {
                throw new RuntimeException(test
                        + "\n\tLogManager.readConfiguration: "
                        + "expected exception not raised");
            } else {
                System.out.println("LogManager.readConfiguration passed for "
                        + test);
            }
        } catch(IOException x) {
            if (!valid) {
                System.out.println(
                        "LogManager.readConfiguration: Got expected exception: "
                        + x + "\n\tfor " + test);
            } else {
                throw x;
            }
        }
    }

    static String getConfigurationFileName() {
        String fname = System.getProperty("java.util.logging.config.file");
        if (fname == null) {
            fname = System.getProperty("java.home");
            if (fname == null) {
                throw new Error("Can't find java.home ??");
            }
            fname = Paths.get(fname, "conf", "logging.properties")
                    .toAbsolutePath().normalize().toString();
        }
        return fname;
    }

    static String defaultConfiguration() throws IOException {
        Properties props = new Properties();
        String fileName = getConfigurationFileName();
        if (Files.exists(Paths.get(fileName))) {
            try (InputStream is = new FileInputStream(fileName);) {
                props.load(is);
            } catch(IOException x) {
                throw new UncheckedIOException(x);
            }
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        props.store(bos, null);
        return bos.toString();
    }

}
