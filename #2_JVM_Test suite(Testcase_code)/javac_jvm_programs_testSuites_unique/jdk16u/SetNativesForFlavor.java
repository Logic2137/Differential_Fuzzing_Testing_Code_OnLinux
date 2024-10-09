

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.Vector;



public class SetNativesForFlavor {

    SystemFlavorMap flavorMap;
    Vector comp1, comp2, comp3;
    Hashtable hash;
    int hashSize;

    String test_native1, test_native2, test_native3, test_native4;
    String[] test_natives_set1, test_natives_set2;
    DataFlavor test_flavor1, test_flavor2, test_flavor3, test_flavor4;
    DataFlavor[] test_flavors_set1, test_flavors_set2;

    public static void main (String[] args) throws Exception {
        new SetNativesForFlavor().doTest();
    }

    public void doTest() throws Exception {
        
        initMappings();

        flavorMap = (SystemFlavorMap)SystemFlavorMap.getDefaultFlavorMap();

        
        hash = new Hashtable(flavorMap.getFlavorsForNatives(null));
        hashSize = hash.size();

        
        System.out.println("One-way Mappings Test");
        flavorMap.setNativesForFlavor(test_flavor1, test_natives_set1);
        flavorMap.setNativesForFlavor(test_flavor2, test_natives_set1);

        
        comp1 = new Vector(Arrays.asList(test_natives_set1));
        comp2 = new Vector(flavorMap.getNativesForFlavor(test_flavor1));
        comp3 = new Vector(flavorMap.getNativesForFlavor(test_flavor2));

        if ( !(comp1.equals(comp2)) || !(comp1.equals(comp3))) {
            throw new RuntimeException("\n*** After setting up one-way mapping" +
                "\nwith setNativesForFlavor(DataFlavor flav, String[] natives)" +
                "\nthe mappings returned from getNativesForFlavor() do not match" +
                "\noriginal mappings.");
        }
        else
           System.out.println("One-way: Test Passes");

        
        System.out.println("Two-way Mappings Test");
        flavorMap.setFlavorsForNative(test_native1, test_flavors_set1);
        flavorMap.setFlavorsForNative(test_native2, test_flavors_set1);

        
        comp1 = new Vector(Arrays.asList(test_flavors_set1));
        comp2 = new Vector(flavorMap.getFlavorsForNative(test_native1));
        comp3 = new Vector(flavorMap.getFlavorsForNative(test_native2));

        if ( !(comp1.equals(comp2)) || !(comp1.equals(comp3))) {
            throw new RuntimeException("\n*** After setting up two-way mapping" +
                "\nwith setFlavorsForNative(string nat, DataFlavor[] flavors)" +
                "\nthe mappings returned from getFlavorsForNative() do not match" +
                "\noriginal mappings.");
        }
        else
           System.out.println("Two-way (DataFlavor): Test Passes");

        
        comp1 = new Vector(Arrays.asList(test_natives_set1));
        comp2 = new Vector(flavorMap.getNativesForFlavor(test_flavor1));
        comp3 = new Vector(flavorMap.getNativesForFlavor(test_flavor2));

        if ( !(comp1.equals(comp2)) || !(comp1.equals(comp3))) {
            throw new RuntimeException("\n*** After setting up two-way mapping" +
                "\nwith setNativesForFlavor(DataFlavor flav, String[] natives)" +
                "\nthe mappings returned from getNativesForFlavor() do not match" +
                "\noriginal mappings.");
        }
        else
           System.out.println("Two-way (String native): Test Passes");

        
        System.out.println("Modify Existing Mappings Test");
        flavorMap.setNativesForFlavor(test_flavor1, test_natives_set2);
        flavorMap.setNativesForFlavor(test_flavor2, test_natives_set2);

        
        comp1 = new Vector(Arrays.asList(test_natives_set2));
        comp2 = new Vector(flavorMap.getNativesForFlavor(test_flavor1));
        comp3 = new Vector(flavorMap.getNativesForFlavor(test_flavor2));

        if ( !(comp1.equals(comp2)) || !(comp1.equals(comp3))) {
            throw new RuntimeException("\n*** After modifying an existing mapping" +
                "\nwith setNativesForFlavor(DataFlavor flav, String[] natives)" +
                "\nthe mappings returned from getNativesForFlavor() do not match" +
                "\noriginal mappings.");
        }
        else
           System.out.println("Modify Existing Mappings: Test Passes");

    }

    
    public void initMappings() throws Exception {
        
        test_native1 = "TEST1";
        test_native2 = "TEST2";
        test_native3 = "TEST3";
        test_native4 = "TEST4";

        test_flavor1 = new DataFlavor(Class.forName("java.awt.Label"), "test1");
        test_flavor2 = new DataFlavor(Class.forName("java.awt.Button"), "test2");
        test_flavor3 = new DataFlavor(Class.forName("java.awt.Checkbox"), "test3");
        test_flavor4 = new DataFlavor(Class.forName("java.awt.List"), "test4");

        
        test_flavors_set1 = new DataFlavor[] {test_flavor1, test_flavor2};
        test_flavors_set2 = new DataFlavor[] {test_flavor3, test_flavor4};

        
        test_natives_set1 = new String[] {test_native1, test_native2};
        test_natives_set2 = new String[] {test_native3, test_native4};
    }
}

