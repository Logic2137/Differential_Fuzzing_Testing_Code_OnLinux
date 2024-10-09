import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;

public class InvalidMapArgumentsTest {

    SystemFlavorMap flavorMap;

    String test_nat;

    String[] test_natives;

    DataFlavor test_flav;

    DataFlavor[] test_flavors;

    public static void main(String[] args) {
        new InvalidMapArgumentsTest().doTest();
    }

    public void doTest() {
        initMappings();
        addFlavorForUnencodedNativeTest(null, null, "both String nat and DataFlavor flav set to null. ");
        addFlavorForUnencodedNativeTest(null, test_flav, "String nat set to null. ");
        addFlavorForUnencodedNativeTest(test_nat, null, "DataFlavor flav set to null. ");
        addUnencodedNativeForFlavorTest(null, null, "both DataFlavor flav and String nat set to null. ");
        addUnencodedNativeForFlavorTest(null, test_nat, "DataFlavor flav set to null. ");
        addUnencodedNativeForFlavorTest(test_flav, null, "String nat set to null. ");
        setNativesForFlavorTest(null, null, "both DataFlavor flav and String[] natives set to null. ");
        setNativesForFlavorTest(null, test_natives, "DataFlavor flav set to null. ");
        setNativesForFlavorTest(test_flav, null, "String[] natives set to null. ");
        setFlavorsForNativeTest(null, null, "both String nat and DataFlavor[] flavors set to null. ");
        setFlavorsForNativeTest(null, test_flavors, "String nat set to null. ");
        setFlavorsForNativeTest(test_nat, null, "DataFlavor[] flavors set to null. ");
    }

    public void initMappings() {
        flavorMap = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();
        test_flav = new DataFlavor("text/plain; charset=ascii", "ASCII Flavor");
        test_nat = "TEXT_TEST";
        test_flavors = new DataFlavor[] { test_flav };
        test_natives = new String[] { test_nat };
    }

    public void setNativesForFlavorTest(DataFlavor flav, String[] natives, String errmsg) {
        try {
            flavorMap.setNativesForFlavor(flav, natives);
            throw new RuntimeException("NullPointerException is not thrown for method " + "setNativesForFlavor with " + errmsg);
        } catch (NullPointerException e) {
        }
    }

    public void setFlavorsForNativeTest(String nat, DataFlavor[] flavors, String errmsg) {
        try {
            flavorMap.setFlavorsForNative(nat, flavors);
            throw new RuntimeException("NullPointerException is not thrown for method " + "setFlavorsForNative with " + errmsg);
        } catch (NullPointerException e) {
        }
    }

    public void addFlavorForUnencodedNativeTest(String nat, DataFlavor flav, String errmsg) {
        try {
            flavorMap.addFlavorForUnencodedNative(nat, flav);
            throw new RuntimeException("NullPointerException is not thrown for method " + "addFlavorForUnencodedNative with " + errmsg);
        } catch (NullPointerException e) {
        }
    }

    public void addUnencodedNativeForFlavorTest(DataFlavor flav, String nat, String errmsg) {
        try {
            flavorMap.addUnencodedNativeForFlavor(flav, nat);
            throw new RuntimeException("NullPointerException is not thrown for method " + "addUnencodedNativeForFlavor with " + errmsg);
        } catch (NullPointerException e) {
        }
    }
}
