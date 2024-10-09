import java.awt.datatransfer.DataFlavor;

public class DefaultMatchTest {

    static DataFlavor df1, df2, df3;

    public static void main(String[] args) throws Exception {
        boolean passed = true;
        try {
            df1 = new DataFlavor("application/postscript");
            df2 = new DataFlavor();
            df3 = new DataFlavor();
        } catch (ClassNotFoundException e1) {
            throw new RuntimeException("Could not create DataFlavors. This should never happen.");
        } catch (IllegalArgumentException e2) {
            passed = false;
        }
        try {
            boolean b;
            b = df1.match(df2);
            b = df2.match(df1);
            b = df2.match(df3);
        } catch (NullPointerException e) {
            throw new RuntimeException("The test FAILED: DataFlavor.match still throws NPE");
        }
        if (!passed) {
            throw new RuntimeException("Test FAILED");
        }
        System.out.println("Test PASSED");
    }
}
