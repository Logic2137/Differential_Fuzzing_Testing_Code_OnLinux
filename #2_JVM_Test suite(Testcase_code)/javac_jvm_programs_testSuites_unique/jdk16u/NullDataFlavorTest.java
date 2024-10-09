



import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public class NullDataFlavorTest {

    private final static Clipboard clipboard =
        Toolkit.getDefaultToolkit().getSystemClipboard();

    public static void main(String[] args) throws Exception {
        boolean failed = false;

        try {
            clipboard.setContents(new NullSelection("DATA1",
                new DataFlavor[] { null, null, null }), null);
            clipboard.setContents(new NullSelection("DATA2",
                new DataFlavor[] { null, DataFlavor.stringFlavor, null }), null);
            clipboard.setContents(new NullSelection("DATA3", null), null);
        } catch (NullPointerException e) {
            failed = true;
            e.printStackTrace();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        if (failed) {
            throw new RuntimeException("test failed: NullPointerException " +
            "has been thrown");
        } else {
            System.err.println("test passed");
        }
    }
}

class NullSelection implements Transferable {

    private final DataFlavor[] flavors;

    private final String data;

    public NullSelection(String data, DataFlavor[] flavors) {
        this.data = data;
        this.flavors = flavors;
    }

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        for (DataFlavor fl : flavors) {
            if (flavor.equals(fl)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Object getTransferData(DataFlavor flavor)
        throws UnsupportedFlavorException, java.io.IOException
    {
        for (DataFlavor fl : flavors) {
            if (flavor.equals(fl)) {
                return data;
            }
        }
        throw new UnsupportedFlavorException(flavor);
    }

}
