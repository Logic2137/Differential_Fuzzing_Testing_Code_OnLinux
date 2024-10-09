import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Properties;

public class UnicodeEscape {

    public static void main(String[] argv) throws Exception {
        save();
        load();
    }

    private static void save() throws Exception {
        FileWriter out = new FileWriter("a.properties");
        out.write("a=b\nb=\\u0\n");
        out.close();
    }

    private static void load() throws Exception {
        Properties properties = new Properties();
        InputStream in = new FileInputStream("a.properties");
        try {
            properties.load(in);
        } catch (IllegalArgumentException iae) {
        } finally {
            in.close();
        }
    }
}
