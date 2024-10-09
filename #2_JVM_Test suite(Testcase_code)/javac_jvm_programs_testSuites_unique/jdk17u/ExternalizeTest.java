import java.awt.datatransfer.DataFlavor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ExternalizeTest {

    public static void main(String[] args) {
        DataFlavor df = new DataFlavor("text/enriched; charset=ascii", "Enrich Flavor");
        storeDataFlavor(df);
        DataFlavor df1 = retrieveDataFlavor();
        if (!df.equals(df1)) {
            throw new RuntimeException("FAILED: restored DataFlavor is not equal to externalized one");
        }
    }

    public static void storeDataFlavor(DataFlavor dfs) {
        try {
            FileOutputStream ostream = new FileOutputStream("t.tmp");
            ObjectOutputStream p = new ObjectOutputStream(ostream);
            dfs.writeExternal(p);
            ostream.close();
        } catch (Exception ex) {
            throw new RuntimeException("FAIL: problem occured while storing DataFlavor");
        }
    }

    public static DataFlavor retrieveDataFlavor() {
        DataFlavor df = DataFlavor.stringFlavor;
        try {
            FileInputStream istream = new FileInputStream("t.tmp");
            ObjectInputStream p = new ObjectInputStream(istream);
            df.readExternal(p);
            istream.close();
        } catch (Exception ex) {
            throw new RuntimeException("FAIL: problem occured while retrieving DataFlavor");
        }
        return df;
    }
}
