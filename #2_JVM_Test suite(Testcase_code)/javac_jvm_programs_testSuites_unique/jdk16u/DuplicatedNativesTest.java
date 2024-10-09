

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;


public class DuplicatedNativesTest {

    public static void main(String[] args) throws Exception {

        
        SystemFlavorMap flavorMap = (SystemFlavorMap)SystemFlavorMap.getDefaultFlavorMap();
        for (Map.Entry<DataFlavor, String> entry : flavorMap.getNativesForFlavors(null).entrySet()) {
            List<String> natives = flavorMap.getNativesForFlavor(entry.getKey());
            if (new HashSet<>(natives).size() != natives.size()) {
                throw new RuntimeException("FAILED: returned natives contain duplicates: " + Arrays.toString(natives.toArray()));
            }
        }

        
        flavorMap.setNativesForFlavor(DataFlavor.stringFlavor, new String[] {"test", "test", "test"});
        List<String> natives = flavorMap.getNativesForFlavor(DataFlavor.stringFlavor);
        if (new HashSet<>(natives).size() != natives.size()) {
            throw new RuntimeException("FAILED: duplicates were not ignored: " + Arrays.toString(natives.toArray()));
        }
    }
}
