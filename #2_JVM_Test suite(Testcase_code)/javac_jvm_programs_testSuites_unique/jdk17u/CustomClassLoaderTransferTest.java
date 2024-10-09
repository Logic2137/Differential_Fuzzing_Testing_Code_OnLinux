import java.awt.*;
import java.awt.datatransfer.*;
import java.io.*;
import java.net.URL;
import java.net.URLClassLoader;

public class CustomClassLoaderTransferTest {

    public static class DFTransferable implements Transferable {

        private final DataFlavor df;

        private final Object obj;

        public DFTransferable(DataFlavor df, Object obj) {
            this.df = df;
            this.obj = obj;
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (df.equals(flavor)) {
                return obj;
            } else {
                throw new UnsupportedFlavorException(flavor);
            }
        }

        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { df };
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return df.equals(flavor);
        }
    }

    public static void main(String[] args) throws Exception {
        Clipboard c = Toolkit.getDefaultToolkit().getSystemClipboard();
        URL url = new File("./subdir/").toURL();
        ClassLoader classLoader = new URLClassLoader(new URL[] { url }, CustomClassLoaderTransferTest.class.getClassLoader());
        Class clazz = Class.forName("TransferableList", true, classLoader);
        DataFlavor df = new DataFlavor(clazz, "Transferable List");
        Object obj = clazz.newInstance();
        Transferable t = new DFTransferable(df, obj);
        c.setContents(t, null);
        Transferable ct = c.getContents(null);
        ct.getTransferData(df);
    }
}
