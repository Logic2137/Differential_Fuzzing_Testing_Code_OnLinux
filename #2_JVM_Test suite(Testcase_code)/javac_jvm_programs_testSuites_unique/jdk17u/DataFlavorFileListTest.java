import java.awt.datatransfer.DataFlavor;

public class DataFlavorFileListTest {

    public static boolean finished = false;

    static DataFlavor df = null;

    public static void main(String[] args) throws Exception {
        df = new DataFlavor("application/x-java-file-list;class=java.util.ArrayList");
        boolean fl = df.isFlavorJavaFileListType();
        finished = true;
        if (!fl)
            throw new RuntimeException("Test FAILED");
    }
}
