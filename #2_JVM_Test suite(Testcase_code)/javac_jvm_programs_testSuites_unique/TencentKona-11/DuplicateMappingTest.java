



import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.Iterator;

public class DuplicateMappingTest {

    public static void main(String[] args) throws Exception {

        final String nativeString = "NATIVE";
        final DataFlavor dataFlavor = new DataFlavor();

        final SystemFlavorMap fm =
                (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();

        fm.addUnencodedNativeForFlavor(dataFlavor, nativeString);
        fm.addUnencodedNativeForFlavor(dataFlavor, nativeString);

        final java.util.List natives =
                fm.getNativesForFlavor(dataFlavor);
        boolean found = false;

        for (final Iterator i = natives.iterator(); i.hasNext(); ) {
            if (nativeString.equals(i.next())) {
                if (found) {
                    throw new RuntimeException("getNativesForFlavor() returns:" +
                            natives);
                } else {
                    found = true;
                }
            }
        }

        if (!found) {
            throw new RuntimeException("getNativesForFlavor() returns:" +
                    natives);
        }

        fm.addFlavorForUnencodedNative(nativeString, dataFlavor);
        fm.addFlavorForUnencodedNative(nativeString, dataFlavor);

        final java.util.List flavors =
                fm.getFlavorsForNative(nativeString);
        found = false;

        for (final Iterator i = flavors.iterator(); i.hasNext(); ) {
            if (dataFlavor.equals(i.next())) {
                if (found) {
                    throw new RuntimeException("getFlavorsForNative() returns:" +
                            flavors);
                } else {
                    found = true;
                }
            }
        }

        if (!found) {
            throw new RuntimeException("getFlavorsForNative() returns:" +
                    natives);
        }
    }
}
