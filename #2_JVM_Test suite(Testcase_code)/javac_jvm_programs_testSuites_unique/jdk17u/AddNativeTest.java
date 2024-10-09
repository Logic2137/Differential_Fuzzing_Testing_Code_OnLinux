import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.*;

public class AddNativeTest {

    SystemFlavorMap flavorMap;

    Hashtable hashVerify;

    Map mapFlavors;

    Map mapNatives;

    Hashtable hashFlavors;

    Hashtable hashNatives;

    public static void main(String[] args) {
        new AddNativeTest().doTest();
    }

    public void doTest() {
        flavorMap = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();
        mapFlavors = flavorMap.getNativesForFlavors(null);
        mapNatives = flavorMap.getFlavorsForNatives(null);
        hashFlavors = new Hashtable(mapFlavors);
        hashNatives = new Hashtable(mapNatives);
        DataFlavor key;
        hashVerify = new Hashtable();
        for (Enumeration e = hashFlavors.keys(); e.hasMoreElements(); ) {
            key = (DataFlavor) e.nextElement();
            java.util.List listNatives = flavorMap.getNativesForFlavor(key);
            Vector vectorNatives = new Vector(listNatives);
            StringBuffer mimeType = new StringBuffer(key.getMimeType());
            mimeType.insert(mimeType.indexOf(";"), "-TEST");
            DataFlavor testFlavor = new DataFlavor(mimeType.toString(), "Test DataFlavor");
            for (ListIterator i = vectorNatives.listIterator(); i.hasNext(); ) {
                String element = (String) i.next();
                flavorMap.addUnencodedNativeForFlavor(testFlavor, element);
            }
            Vector existingNatives = new Vector(flavorMap.getNativesForFlavor(testFlavor));
            existingNatives.addAll(vectorNatives);
            vectorNatives = existingNatives;
            hashVerify.put(testFlavor, vectorNatives);
        }
        verifyNewMappings();
    }

    public boolean verifyNewMappings() {
        boolean result = true;
        for (Enumeration e = hashVerify.keys(); e.hasMoreElements(); ) {
            DataFlavor key = (DataFlavor) e.nextElement();
            java.util.List listNatives = flavorMap.getNativesForFlavor(key);
            Vector vectorNatives = new Vector(listNatives);
            if (!(vectorNatives.containsAll((Vector) hashVerify.get(key)) && ((Vector) hashVerify.get(key)).containsAll(vectorNatives))) {
                throw new RuntimeException("\n*** Error in verifyNewMappings()" + "\nmethod1: addUnencodedNativeForFlavor(DataFlavor flav, String nat)" + "\nmethod2: List getNativesForFlavor(DataFlavor flav)" + "\nDataFlavor: " + key.getMimeType() + "\nAfter adding several mappings with addUnencodedNativeForFlavor," + "\nthe returned list did not match the mappings that were added." + "\nThe mapping was not included in the list.");
            }
        }
        System.out.println("*** DataFlavor size = " + hashVerify.size());
        System.out.println("*** verifyNewMappings result: " + result + "\n");
        return result;
    }
}
