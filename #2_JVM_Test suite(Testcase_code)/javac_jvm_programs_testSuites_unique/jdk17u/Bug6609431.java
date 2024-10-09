import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Bug6609431 {

    private static final String expected = "backslash";

    public static void main(String[] args) throws IOException {
        try (FileReader fr = new FileReader(new File(System.getProperty("test.src", "."), "Bug6609431.properties"))) {
            Properties p = new Properties();
            p.load(fr);
            p.getProperty("a");
            String val = p.getProperty("b");
            if (!val.equals(expected)) {
                throw new RuntimeException("Value returned from the property" + " list was incorrect. Returned: '" + val + "', expected: '" + expected + "'");
            }
        }
    }
}
