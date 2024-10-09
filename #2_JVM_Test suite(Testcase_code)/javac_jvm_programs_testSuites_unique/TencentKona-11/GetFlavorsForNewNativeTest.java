

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;



public class GetFlavorsForNewNativeTest {

    SystemFlavorMap flavorMap;
    Vector comp1, comp2, comp3, comp4;
    Hashtable hash;
    int hashSize;

    String test_native1, test_encoded;
    DataFlavor test_flavor1, test_flavor2;
    String[] test_natives_set;
    DataFlavor[] test_flavors_set;

    public static void main (String[] args) throws Exception {
        new GetFlavorsForNewNativeTest().doTest();
    }

    public void doTest() throws Exception {
        
        initMappings();

        flavorMap = (SystemFlavorMap) SystemFlavorMap.getDefaultFlavorMap();

        
        hash = new Hashtable(flavorMap.getFlavorsForNatives(null));
        hashSize = hash.size();

        
        
        
        
        System.out.println("GetFlavorsForNative using Unencoded Native Test");

        comp1 = new Vector(flavorMap.getFlavorsForNative(test_native1));

        if (comp1.size() != 0) {
            throw new RuntimeException("\n*** After passing a new Unencoded native" +
                    "\nwith getFlavorsForNative(String nat)" +
                    "\nthe String native should be ignored and no mapping established.");
        } else
            System.out.println("GetFlavorsForNative using Unencoded Native Test: Test Passes");


        
        
        
        
        
        System.out.println("GetFlavorsForNative using Encoded Native Test");

        comp1 = new Vector(Arrays.asList(test_flavors_set));
        comp2 = new Vector(flavorMap.getFlavorsForNative(test_encoded));

        comp3 = new Vector(Arrays.asList(test_natives_set));
        comp4 = new Vector(flavorMap.getNativesForFlavor(test_flavor2));

        if (!comp1.equals(comp2) || !comp3.equals(comp4)) {
            throw new RuntimeException("\n*** After passing a new Encoded native" +
                    "\nwith getFlavorsForNative(String nat)" +
                    "\nthe mapping in both directions was not established.");
        } else
            System.out.println("GetFlavorsForNative using Encoded Native: Test Passes");
    }

    public void initMappings() throws Exception {
       
      
      test_native1 = "TEST1";

      
      test_flavor1 = new DataFlavor(Class.forName("java.awt.Button"), "Button");

      
      String buttonMIME = test_flavor1.getMimeType();
      test_encoded = SystemFlavorMap.encodeJavaMIMEType(buttonMIME);

      
      test_flavor2 = SystemFlavorMap.decodeDataFlavor(test_encoded);

      
      test_flavors_set = new DataFlavor[] {test_flavor2};

      
      test_natives_set = new String[] {test_encoded};
    }

}

