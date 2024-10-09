import java.awt.datatransfer.DataFlavor;

public class DataFlavorCloneTest {

    public static void main(String[] args) throws Exception {
        DataFlavor df1 = null;
        Object df2 = null;
        try {
            df1 = new DataFlavor();
            df2 = df1.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("FAILED: Unexpected exception: " + e);
        } catch (NullPointerException e) {
            throw new RuntimeException("FAILED: Got Null pointer exception");
        }
    }
}
