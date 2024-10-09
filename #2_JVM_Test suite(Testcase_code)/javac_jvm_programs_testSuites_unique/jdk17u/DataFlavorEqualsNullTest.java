import java.awt.datatransfer.DataFlavor;

public class DataFlavorEqualsNullTest {

    public static boolean finished = false;

    static boolean noexc = true;

    static boolean eq = false;

    static DataFlavor df = null;

    public static void main(String[] args) throws Exception {
        try {
            df = new DataFlavor("application/postscript;class=java.awt.datatransfer.DataFlavor");
        } catch (ClassNotFoundException e) {
        }
        try {
            eq = df.equals((Object) null);
            if (eq)
                noexc = false;
            eq = df.equals((DataFlavor) null);
            if (eq)
                noexc = false;
            eq = df.equals((String) null);
            if (eq)
                noexc = false;
        } catch (NullPointerException e1) {
            noexc = false;
        }
        finished = true;
        if (!noexc)
            throw new RuntimeException("Test FAILED");
    }
}
