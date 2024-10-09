import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

public class ReaderForUnicodeText {

    public static void main(String[] args) throws Exception {
        DataFlavor df = DataFlavor.plainTextFlavor;
        TextTransferable t = new TextTransferable();
        Reader reader;
        try {
            reader = df.getReaderForText(t);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("FAILED: Exception thrown in getReaderForText()");
        }
    }
}

class TextTransferable implements Transferable {

    String text = "Try to test me...";

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = { DataFlavor.plainTextFlavor };
        return flavors;
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        if (flavor.match(DataFlavor.plainTextFlavor)) {
            return true;
        }
        return false;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        byte[] textBytes = null;
        if (!isDataFlavorSupported(flavor)) {
            throw new UnsupportedFlavorException(flavor);
        }
        String encoding = flavor.getParameter("charset");
        if (encoding == null) {
            textBytes = text.getBytes();
        } else {
            textBytes = text.getBytes(encoding);
        }
        return new ByteArrayInputStream(textBytes);
    }
}
