import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

public class AddNativeForFlavorTest {

    SystemFlavorMap flavorMap;

    Vector comp1, comp2, comp3;

    Hashtable hash;

    int hashSize;

    String test_native1, test_native2, test_native3, test_native4;

    String[] test_natives_set1, test_natives_set2;

    DataFlavor test_flav;

    DataFlavor[] test_flavors_set;

    public static void main(String[] args) throws Exception {
        new AddNativeForFlavorTest().doTest();
    }

    public void doTest() throws Exception {
        initMappings();
        flavorMap = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();
        hash = new Hashtable(flavorMap.getFlavorsForNatives(null));
        hashSize = hash.size();
        System.out.println("One-way Mappings Test");
        flavorMap.addUnencodedNativeForFlavor(test_flav, test_native1);
        flavorMap.addUnencodedNativeForFlavor(test_flav, test_native2);
        comp1 = new Vector(Arrays.asList(test_natives_set1));
        comp2 = new Vector(flavorMap.getNativesForFlavor(test_flav));
        if (!comp1.equals(comp2)) {
            throw new RuntimeException("\n*** After setting up one-way mapping" + "\nwith addUnencodedNativeForFlavor(DataFlavor flav, String nat)" + "\nthe mappings returned from getNativesForFlavor() do not match" + "\noriginal mappings.");
        } else
            System.out.println("One-way: Test Passes");
        System.out.println("Two-way Mappings Test");
        flavorMap.addFlavorForUnencodedNative(test_native1, test_flav);
        flavorMap.addFlavorForUnencodedNative(test_native2, test_flav);
        comp1 = new Vector(Arrays.asList(test_flavors_set));
        comp2 = new Vector(flavorMap.getFlavorsForNative(test_native1));
        comp3 = new Vector(flavorMap.getFlavorsForNative(test_native2));
        if (!(comp1.equals(comp2)) || !(comp1.equals(comp3))) {
            throw new RuntimeException("\n*** After setting up two-way mapping" + "\nwith addFlavorForUnencodedNative(String nat, DataFlavor flav)" + "\nthe mappings returned from getFlavorsForNative() do not match" + "\noriginal mappings.");
        } else
            System.out.println("Two-way (DataFlavor): Test Passes");
        comp1 = new Vector(Arrays.asList(test_natives_set1));
        comp2 = new Vector(flavorMap.getNativesForFlavor(test_flav));
        if (!comp1.equals(comp2)) {
            throw new RuntimeException("\n*** After setting up two-way mapping" + "\nwith addUnencodedNativeForFlavor(DataFlavor flav, String nat)" + "\nthe mappings returned from getNativesForFlavor() do not match" + "\noriginal mappings.");
        } else
            System.out.println("Two-way (String native): Test Passes");
        System.out.println("Modify Existing Mappings Test");
        flavorMap.addUnencodedNativeForFlavor(test_flav, test_native3);
        flavorMap.addUnencodedNativeForFlavor(test_flav, test_native4);
        comp1 = new Vector(Arrays.asList(test_natives_set2));
        comp2 = new Vector(flavorMap.getNativesForFlavor(test_flav));
        if (!comp1.equals(comp2)) {
            throw new RuntimeException("\n*** After modifying an existing mapping" + "\nwith addUnencodedNativeForFlavor(DataFlavor flav, String nat)" + "\nthe mappings returned from getNativesForFlavor() do not match" + "\nupdated mappings.");
        } else
            System.out.println("Modify Existing Mappings: Test Passes");
    }

    public void initMappings() throws Exception {
        test_native1 = "TEST1";
        test_native2 = "TEST2";
        test_native3 = "TEST3";
        test_native4 = "TEST4";
        test_flav = new DataFlavor(Class.forName("java.awt.Label"), "test1");
        test_flavors_set = new DataFlavor[] { test_flav };
        test_natives_set1 = new String[] { test_native1, test_native2 };
        test_natives_set2 = new String[] { test_native1, test_native2, test_native3, test_native4 };
    }
}
