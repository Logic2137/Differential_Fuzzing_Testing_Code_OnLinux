import java.awt.datatransfer.DataFlavor;

public class DataFlavorEqualsTest {

    public static boolean finished = false;

    static boolean noexc = true;

    static boolean eq = false;

    static DataFlavor df = null;

    public static void main(String[] args) {
        df = new DataFlavor();
        try {
            eq = df.equals((Object) new DataFlavor());
            if (!eq)
                noexc = false;
            eq = df.equals(new DataFlavor());
            if (!eq)
                noexc = false;
            eq = df.equals("application/postscript;class=java.awt.datatransfer.DataFlavor");
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
