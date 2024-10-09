import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.io.Reader;

public class GetReaderForTextIAEForStringSelectionTest {

    public static void main(String[] args) throws Exception {
        DataFlavor pt;
        try {
            pt = DataFlavor.plainTextFlavor;
            StringSelection ss = new StringSelection("ReaderExample");
            Reader re = pt.getReaderForText(ss);
            if (re == null) {
                throw new RuntimeException("Test FAILED! reader==null");
            }
        } catch (Exception e) {
            throw new RuntimeException("Test FAILED because of the exception: " + e);
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
