import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.Iterator;

public class GetNativesForFlavorTest {

    final static SystemFlavorMap fm = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();

    public static void main(String[] args) throws Exception {
        test1();
        test2();
        test3();
        test4();
    }

    public static void test1() throws ClassNotFoundException {
        final DataFlavor flavor = new DataFlavor("text/plain-TEST; charset=Unicode");
        final java.util.List natives = fm.getNativesForFlavor(flavor);
        if (natives.size() > 1) {
            for (final Iterator i = natives.iterator(); i.hasNext(); ) {
                String element = (String) i.next();
                if (SystemFlavorMap.isJavaMIMEType(element)) {
                    throw new RuntimeException("getFlavorsForNative() returns: " + natives);
                }
            }
        }
    }

    public static void test2() throws ClassNotFoundException {
        final DataFlavor flavor = new DataFlavor("text/plain-TEST; charset=Unicode");
        fm.setNativesForFlavor(flavor, new String[0]);
        final java.util.List natives = fm.getNativesForFlavor(flavor);
        if (!natives.isEmpty()) {
            throw new RuntimeException("getFlavorsForNative() returns:" + natives);
        }
    }

    public static void test3() throws ClassNotFoundException {
        final DataFlavor flavor = new DataFlavor("text/plain-TEST-nocharset; class=java.nio.ByteBuffer");
        final java.util.List natives = fm.getNativesForFlavor(flavor);
        boolean encodedNativeFound = false;
        if (natives.size() == 0) {
            throw new RuntimeException("getFlavorsForNative() returns:" + natives);
        }
        if (natives.size() == 1) {
            String element = (String) natives.get(0);
            if (SystemFlavorMap.isJavaMIMEType(element)) {
                final DataFlavor decodedFlavor = SystemFlavorMap.decodeDataFlavor(element);
                if (!flavor.equals(decodedFlavor)) {
                    System.err.println("DataFlavor is not properly incoded:");
                    System.err.println("    encoded flavor: " + flavor);
                    System.err.println("    decoded flavor: " + decodedFlavor);
                    throw new RuntimeException("getFlavorsForNative() returns:" + natives);
                }
            }
        } else {
            for (final Iterator i = natives.iterator(); i.hasNext(); ) {
                String element = (String) i.next();
                if (SystemFlavorMap.isJavaMIMEType(element)) {
                    throw new RuntimeException("getFlavorsForNative() returns:" + natives);
                }
            }
        }
    }

    public static void test4() throws ClassNotFoundException {
        final DataFlavor flavor = new DataFlavor("unknown/unknown");
        final java.util.List natives = fm.getNativesForFlavor(flavor);
        if (natives.size() == 1) {
            String element = (String) natives.get(0);
            if (SystemFlavorMap.isJavaMIMEType(element)) {
                final DataFlavor decodedFlavor = SystemFlavorMap.decodeDataFlavor(element);
                if (!flavor.equals(decodedFlavor)) {
                    System.err.println("DataFlavor is not properly incoded:");
                    System.err.println("    encoded flavor: " + flavor);
                    System.err.println("    decoded flavor: " + decodedFlavor);
                    throw new RuntimeException("getFlavorsForNative() returns:" + natives);
                }
            }
        } else {
            throw new RuntimeException("getFlavorsForNative() returns:" + natives);
        }
    }
}
