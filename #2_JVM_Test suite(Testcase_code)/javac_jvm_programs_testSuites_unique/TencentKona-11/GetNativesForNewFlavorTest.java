

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;



public class GetNativesForNewFlavorTest {

    SystemFlavorMap flavorMap;
    Vector comp1, comp2, comp3, comp4;
    Hashtable hash;
    int hashSize;

    String test_encoded;
    DataFlavor test_flavor1, test_flavor2;
    String[] test_natives_set;
    DataFlavor[] test_flavors_set;

    public static void main (String[] args) throws Exception {
        new GetNativesForNewFlavorTest().doTest();
    }

    public void doTest() throws Exception {
        
        initMappings();

        boolean passed = true;
        flavorMap = (SystemFlavorMap)SystemFlavorMap.getDefaultFlavorMap();

        
        hash = new Hashtable(flavorMap.getFlavorsForNatives(null));
        hashSize = hash.size();

        
        
        
        
        
        System.out.println("GetNativesForFlavor using new DataFlavor");

        comp1 = new Vector(Arrays.asList(test_natives_set));
        comp2 = new Vector(flavorMap.getNativesForFlavor(test_flavor1));

        comp3 = new Vector(Arrays.asList(test_flavors_set));
        comp4 = new Vector(flavorMap.getFlavorsForNative(test_encoded));

        if ( !comp1.equals(comp2) || !comp3.equals(comp4) ) {
            throw new RuntimeException("\n*** After passing a new DataFlavor" +
                "\nwith getNativesForFlavor(DataFlavor flav)" +
                "\nthe mapping in both directions was not established.");
        }
        else
           System.out.println("GetNativesForFlavor using new DataFlavor: Test Passes");
    }

    public void initMappings() throws Exception {
        
        
        test_flavor1 = new DataFlavor(Class.forName("java.awt.Button"), "Button");

        
        String buttonMIME = test_flavor1.getMimeType();
        test_encoded = SystemFlavorMap.encodeJavaMIMEType(buttonMIME);

        
        test_flavor2 = SystemFlavorMap.decodeDataFlavor(test_encoded);

        
        test_flavors_set = new DataFlavor[] {test_flavor1};

        
        test_natives_set = new String[] {test_encoded};
    }
}

