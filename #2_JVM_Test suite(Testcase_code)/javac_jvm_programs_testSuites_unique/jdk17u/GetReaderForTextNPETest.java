import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Reader;

public class GetReaderForTextNPETest {

    public static void main(String[] args) {
        DataFlavor df = new DataFlavor();
        FakeTransferable t = new FakeTransferable();
        Reader reader;
        try {
            reader = df.getReaderForText(null);
        } catch (Exception e) {
            if (!(e instanceof NullPointerException)) {
                throw new RuntimeException("TEST FAILED: not a NPE thrown on a null argument.");
            }
        }
        try {
            reader = df.getReaderForText(t);
        } catch (Exception e) {
            if (!(e instanceof IllegalArgumentException)) {
                throw new RuntimeException("FAILED: not an IllegalArgumentException thrown on a transferable with null transfer data .");
            }
        }
    }
}

class FakeTransferable implements Transferable {

    public DataFlavor[] getTransferDataFlavors() {
        return null;
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return false;
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return null;
    }
}
