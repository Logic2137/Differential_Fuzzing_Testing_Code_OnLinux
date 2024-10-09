import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;

public class AddFlavorForNativeTest {

    SystemFlavorMap flavorMap;

    Vector comp1, comp2, comp3;

    Hashtable hash;

    int hashSize;

    String test_native;

    String[] test_natives_set;

    DataFlavor test_flavor1, test_flavor2, test_flavor3, test_flavor4;

    DataFlavor[] test_flavors_set1, test_flavors_set2;

    public static void main(String[] args) throws Exception {
        new AddFlavorForNativeTest().doTest();
    }

    public void doTest() throws Exception {
        initMappings();
        flavorMap = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();
        hash = new Hashtable(flavorMap.getFlavorsForNatives(null));
        hashSize = hash.size();
        System.out.println("One-way Mappings Test");
        flavorMap.addFlavorForUnencodedNative(test_native, test_flavor1);
        flavorMap.addFlavorForUnencodedNative(test_native, test_flavor2);
        comp1 = new Vector(Arrays.asList(test_flavors_set1));
        comp2 = new Vector(flavorMap.getFlavorsForNative(test_native));
        if (!comp1.equals(comp2)) {
            throw new RuntimeException("\n*** After setting up one-way mapping" + "\nwith addFlavorForUnencodedNative(String nat, DataFlavor flav)" + "\nthe mappings returned from getFlavorsForNative() do not match" + "\noriginal mappings.");
        } else
            System.out.println("One-way: Test Passes");
        System.out.println("Two-way Mappings Test");
        flavorMap.addUnencodedNativeForFlavor(test_flavor1, test_native);
        flavorMap.addUnencodedNativeForFlavor(test_flavor2, test_native);
        comp1 = new Vector(Arrays.asList(test_natives_set));
        comp2 = new Vector(flavorMap.getNativesForFlavor(test_flavor1));
        comp3 = new Vector(flavorMap.getNativesForFlavor(test_flavor2));
        if (!(comp1.equals(comp2)) || !(comp1.equals(comp3))) {
            throw new RuntimeException("\n*** After setting up two-way mapping" + "\nwith addUnencodedNativeForFlavor(DataFlavor flav, String nat)" + "\nthe mappings returned from getNativesForFlavor() do not match" + "\noriginal mappings.");
        } else
            System.out.println("Two-way (String native): Test Passes");
        comp1 = new Vector(Arrays.asList(test_flavors_set1));
        comp2 = new Vector(flavorMap.getFlavorsForNative(test_native));
        if (!comp1.equals(comp2)) {
            throw new RuntimeException("\n*** After setting up two-way mapping" + "\nwith addFlavorForUnencodedNative(String nat, DataFlavor flav)" + "\nthe mappings returned from getFlavorsForNative() do not match" + "\noriginal mappings.");
        } else
            System.out.println("Two-way (DataFlavor): Test Passes");
        System.out.println("Modify Existing Mappings Test");
        flavorMap.addFlavorForUnencodedNative(test_native, test_flavor3);
        flavorMap.addFlavorForUnencodedNative(test_native, test_flavor4);
        comp1 = new Vector(Arrays.asList(test_flavors_set2));
        comp2 = new Vector(flavorMap.getFlavorsForNative(test_native));
        if (!comp1.equals(comp2)) {
            throw new RuntimeException("\n*** After modifying an existing mapping" + "\nwith addFlavorForUnencodedNative(String nat, DataFlavor flav)" + "\nthe mappings returned from getFlavorsForNative() do not match" + "\nupdated mappings.");
        } else
            System.out.println("Modify Existing Mappings: Test Passes");
    }

    public void initMappings() throws Exception {
        test_native = "TEST1";
        test_flavor1 = new DataFlavor(Class.forName("java.awt.Label"), "test1");
        test_flavor2 = new DataFlavor(Class.forName("java.awt.Button"), "test2");
        test_flavor3 = new DataFlavor(Class.forName("java.awt.Checkbox"), "test3");
        test_flavor4 = new DataFlavor(Class.forName("java.awt.List"), "test4");
        test_flavors_set1 = new DataFlavor[] { test_flavor1, test_flavor2 };
        test_flavors_set2 = new DataFlavor[] { test_flavor1, test_flavor2, test_flavor3, test_flavor4 };
        test_natives_set = new String[] { test_native };
    }
}
