import java.awt.datatransfer.DataFlavor;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

public class MimeTypeSerializationTest {

    public static void main(String[] args) throws Exception {
        boolean failed = false;
        try {
            int len = 70000;
            char[] longValue = new char[len];
            Arrays.fill(longValue, 'v');
            DataFlavor longdf = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + "; class=java.lang.String; longParameter=" + new String(longValue));
            DataFlavor shortdf = new DataFlavor(DataFlavor.javaJVMLocalObjectMimeType + "; class=java.lang.String");
            ByteArrayOutputStream baos = new ByteArrayOutputStream(100000);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(longdf);
            oos.writeObject(shortdf);
            oos.close();
            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            DataFlavor longdf2 = (DataFlavor) ois.readObject();
            DataFlavor shortdf2 = (DataFlavor) ois.readObject();
            ois.close();
            failed = !(longdf.getMimeType().equals(longdf2.getMimeType()) && shortdf.getMimeType().equals(shortdf2.getMimeType()));
            if (failed) {
                System.err.println("deserialized MIME type does not match original one");
            }
        } catch (IOException e) {
            failed = true;
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (failed) {
            throw new RuntimeException("test failed: serialization attempt failed");
        } else {
            System.err.println("test passed");
        }
    }
}
