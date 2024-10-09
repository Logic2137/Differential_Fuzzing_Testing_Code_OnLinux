
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.io.IOException;
import java.io.Reader;
import javax.swing.JLabel;
import javax.swing.TransferHandler;

public class ConstructFlavoredObjectTest {

    public static void main(String[] args) throws Exception {

        if (args[0].equals("COPY")) {

            

            final String TEXT_MIME_TYPE = DataFlavor.javaJVMLocalObjectMimeType +
                    ";class=" + String.class.getName();

            final DataFlavor dataFlavor = new DataFlavor(TEXT_MIME_TYPE);
            SystemFlavorMap systemFlavorMap =
                   (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();
            systemFlavorMap.addUnencodedNativeForFlavor(dataFlavor, "TEXT");
            systemFlavorMap.addFlavorForUnencodedNative("TEXT", dataFlavor);

            TransferHandler transferHandler = new TransferHandler("text");

            String text = "This is sample export text";
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            transferHandler.exportToClipboard(new JLabel(text), clipboard,
                    TransferHandler.COPY);
        }
        else if (args[0].equals("PASTE")) {

            

            final String TEST_MIME_TYPE = "text/plain;class=" +
                    MyStringReader.class.getName();

            final DataFlavor dataFlavor = new DataFlavor(TEST_MIME_TYPE);
            SystemFlavorMap systemFlavorMap = (SystemFlavorMap) SystemFlavorMap.
                    getDefaultFlavorMap();
            systemFlavorMap.addUnencodedNativeForFlavor(dataFlavor, "TEXT");
            systemFlavorMap.addFlavorForUnencodedNative("TEXT", dataFlavor);

            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

            Object clipboardData = clipboard.getData(dataFlavor);

            if (!(clipboardData instanceof MyStringReader)) {
                throw new RuntimeException("Wrong clipboard data!");
            }
        }
    }

    public static class MyStringReader extends Reader {

        public MyStringReader(Reader reader) {
        }

        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public void close() throws IOException {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}

