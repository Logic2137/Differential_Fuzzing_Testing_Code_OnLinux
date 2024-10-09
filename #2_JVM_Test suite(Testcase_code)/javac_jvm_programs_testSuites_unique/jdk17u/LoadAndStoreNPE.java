import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Properties;

public class LoadAndStoreNPE {

    public static void main(String[] args) throws Exception {
        int failures = 0;
        Properties props = new Properties();
        try {
            props.store((OutputStream) null, "comments");
            failures++;
        } catch (NullPointerException e) {
        }
        try {
            props.store((Writer) null, "comments");
            failures++;
        } catch (NullPointerException e) {
        }
        try {
            props.load((InputStream) null);
            failures++;
        } catch (NullPointerException e) {
        }
        try {
            props.load((Reader) null);
            failures++;
        } catch (NullPointerException e) {
        }
        if (failures != 0) {
            throw new RuntimeException("LoadAndStoreNPE failed with " + failures + " errors!");
        }
    }
}
