



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.util.stream.Stream;

public class BadSerializationTest {

    private static final String[] badSerialized = new String[] {
            "badAction",
            "noEvents",
            "nullComponent",
            "nullDragSource",
            "nullOrigin"
    };

    private static final String goodSerialized = "good";

    public static void main(String[] args) throws Exception {
        String testSrc = System.getProperty("test.src") + File.separator;
        testReadObject(testSrc + goodSerialized, false);
        Stream.of(badSerialized).forEach(file -> testReadObject(testSrc + file, true));
    }

    private static void testReadObject(String filename, boolean expectException) {
        Exception exceptionCaught = null;
        try (FileInputStream fileInputStream = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fileInputStream)) {
            ois.readObject();
        } catch (InvalidObjectException e) {
            exceptionCaught = e;
        } catch (IOException e) {
            throw new RuntimeException("FAILED: IOException", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("FAILED: ClassNotFoundException", e);
        }
        if (exceptionCaught != null && !expectException) {
            throw new RuntimeException("FAILED: UnexpectedException", exceptionCaught);
        }
        if (exceptionCaught == null && expectException) {
            throw new RuntimeException("FAILED: Invalid object was created with no exception");
        }
    }
}
