

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.FlavorTable;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.Arrays;

public class DataFlavorSearcher {
    static public String[] HTML_NAMES = new String[]{"HTML", "HTML Format"};
    static public String[] RICH_TEXT_NAMES = new String[]{"RICH_TEXT", "Rich Text Format"};

    static public DataFlavor getByteDataFlavorForNative(String[] nats) {
        FlavorTable flavorTable = (FlavorTable) SystemFlavorMap.getDefaultFlavorMap();

        for (String nat : nats) {
            java.util.List<DataFlavor> flavors = flavorTable.getFlavorsForNative(nat);
            for (DataFlavor flavor : flavors) {
                if (flavor != null
                        && flavor.getRepresentationClass().equals(byte[].class)) {
                    return flavor;
                }
            }
        }
        throw new RuntimeException("No data flavor was found for natives: " + Arrays.toString(nats));
    }
}
