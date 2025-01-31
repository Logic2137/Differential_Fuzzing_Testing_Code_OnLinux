import java.io.File;
import java.io.FileOutputStream;
import javax.security.auth.login.*;
import java.net.URI;
import java.security.URIParameter;

public class ConfigFileWithBlank {

    public static void main(String[] args) throws Exception {
        File f = new File("a b c");
        FileOutputStream fos = new FileOutputStream(f);
        fos.write("".getBytes());
        fos.close();
        System.err.println(f.toURI());
        try {
            Configuration.getInstance("JavaLoginConfig", new URIParameter(f.toURI()));
        } finally {
            f.delete();
        }
    }
}
