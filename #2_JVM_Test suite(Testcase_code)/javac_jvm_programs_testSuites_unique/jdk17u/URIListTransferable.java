import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.net.URI;
import java.util.List;

class URIListTransferable implements Transferable {

    private final DataFlavor supportedFlavor;

    private List<URI> list;

    public URIListTransferable(List<URI> list) {
        try {
            this.supportedFlavor = new DataFlavor("text/uri-list;class=java.lang.String");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("FAILED: could not create a DataFlavor");
        }
        this.list = list;
    }

    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { supportedFlavor };
    }

    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return supportedFlavor.equals(flavor);
    }

    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (supportedFlavor.equals(flavor)) {
            return list.stream().map(URI::toASCIIString).collect(StringBuilder::new, (builder, uri) -> {
                builder.append(uri).append("\r\n");
            }, StringBuilder::append).toString();
        }
        throw new UnsupportedFlavorException(flavor);
    }
}
