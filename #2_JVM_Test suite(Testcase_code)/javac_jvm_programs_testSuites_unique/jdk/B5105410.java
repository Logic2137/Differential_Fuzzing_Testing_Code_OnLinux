



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class B5105410 {
    public static void main (String[] args) throws Exception {
        setup();
        URL url = new URL("jar:file:./foo2.jar!/bar.txt");
        URLConnection urlc = url.openConnection();
        urlc.setUseCaches(false);
        InputStream is = urlc.getInputStream();
        is.read();
        is.close();
        File file = new File("foo2.jar");
        if (!file.delete()) {
            throw new RuntimeException("Could not delete foo2.jar");
        }
        if (file.exists()) {
            throw new RuntimeException("foo2.jar still exists");
        }
    }

    static void setup() throws IOException {
        Files.copy(Paths.get(System.getProperty("test.src"), "foo2.jar"),
                   Paths.get(".", "foo2.jar"), REPLACE_EXISTING);
    }
}

