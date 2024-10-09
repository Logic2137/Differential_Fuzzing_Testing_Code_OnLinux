import java.awt.*;
import java.awt.datatransfer.DataFlavor;

public class DataFlavorSerializedTest {

    public static boolean finished = false;

    static DataFlavor df = null;

    public static void main(String[] args) throws Exception {
        df = new DataFlavor("application/x-java-serialized-object;class=java.io.Serializable");
        boolean fl = df.isMimeTypeSerializedObject();
        finished = true;
        if (!fl)
            throw new RuntimeException("Test FAILED");
    }
}
