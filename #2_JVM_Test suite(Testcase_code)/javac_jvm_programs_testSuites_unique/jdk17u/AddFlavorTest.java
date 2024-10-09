import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.*;
import java.nio.charset.Charset;

public class AddFlavorTest {

    SystemFlavorMap flavorMap;

    Hashtable<String, List<DataFlavor>> hashVerify;

    public static void main(String[] args) throws Exception {
        new AddFlavorTest().doTest();
    }

    void doTest() throws Exception {
        flavorMap = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();
        hashVerify = new Hashtable();
        for (String key : flavorMap.getFlavorsForNatives(null).keySet()) {
            Set<DataFlavor> flavorsSet = new HashSet<>(flavorMap.getFlavorsForNative(key));
            key = key.concat("TEST");
            for (DataFlavor element : flavorsSet) {
                flavorMap.addFlavorForUnencodedNative(key, element);
            }
            hashVerify.put(key, new Vector(flavorsSet));
        }
        verifyNewMappings();
    }

    void verifyNewMappings() {
        System.out.println("*** native size = " + hashVerify.size());
        for (Enumeration e = hashVerify.keys(); e.hasMoreElements(); ) {
            String key = (String) e.nextElement();
            compareFlavors(hashVerify.get(key), flavorMap.getFlavorsForNative(key), key);
            compareFlavors(flavorMap.getFlavorsForNative(key), hashVerify.get(key), key);
        }
    }

    void compareFlavors(List<DataFlavor> flavors1, List<DataFlavor> flavors2, String key) {
        for (DataFlavor flavor1 : flavors1) {
            boolean result = false;
            for (DataFlavor flavor2 : flavors2) {
                if (flavor1.equals(flavor2))
                    result = true;
            }
            if (!result)
                throw new RuntimeException("\n*** Error in verifyNewMappings()" + "\nmethod1: addFlavorForUnencodedNative(String nat, DataFlavor flav)" + "\nmethod2: List getFlavorsForNative(String nat)" + "\nString native: " + key + "\nAfter adding several mappings with addFlavorForUnencodedNative," + "\nthe returned list did not match the mappings that were added." + "\nEither the mapping was not included in the list, or the order was incorect.");
        }
    }

    Set<DataFlavor> convertMimeTypeToDataFlavors(String baseType) throws Exception {
        Set<DataFlavor> result = new LinkedHashSet<>();
        for (String charset : getStandardEncodings()) {
            for (String txtClass : new String[] { "java.io.InputStream", "java.nio.ByteBuffer", "\"[B\"" }) {
                String mimeType = baseType + ";charset=" + charset + ";class=" + txtClass;
                if ("text/html".equals(baseType)) {
                    for (String documentType : new String[] { "all", "selection", "fragment" }) result.add(new DataFlavor(mimeType + ";document=" + documentType));
                } else {
                    DataFlavor df = new DataFlavor(mimeType);
                    if (df.equals(DataFlavor.plainTextFlavor))
                        df = DataFlavor.plainTextFlavor;
                    result.add(df);
                }
            }
        }
        return result;
    }

    Set<String> getStandardEncodings() {
        Set<String> tempSet = new HashSet<>();
        tempSet.add("US-ASCII");
        tempSet.add("ISO-8859-1");
        tempSet.add("UTF-8");
        tempSet.add("UTF-16BE");
        tempSet.add("UTF-16LE");
        tempSet.add("UTF-16");
        tempSet.add(Charset.defaultCharset().name());
        return tempSet;
    }
}
