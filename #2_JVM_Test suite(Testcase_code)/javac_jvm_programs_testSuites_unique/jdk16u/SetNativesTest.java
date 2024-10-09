

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;



public class SetNativesTest {

    SystemFlavorMap flavorMap;
    Hashtable hashVerify;

    Map mapFlavors;
    Map mapNatives;

    Hashtable hashFlavors;
    Hashtable hashNatives;

    public static void main (String[] args){
        new SetNativesTest().doTest();
    }

    public void doTest() {
        flavorMap = (SystemFlavorMap)SystemFlavorMap.getDefaultFlavorMap();

        
        mapFlavors = flavorMap.getNativesForFlavors(null);
        mapNatives = flavorMap.getFlavorsForNatives(null);

        hashFlavors = new Hashtable(mapFlavors);
        hashNatives = new Hashtable(mapNatives);


        
        
        
        
        
        
        
        String key;
        hashVerify = new Hashtable();

        for (Enumeration e = hashNatives.keys() ; e.hasMoreElements() ;) {
            key = (String)e.nextElement();

            java.util.List listFlavors = flavorMap.getFlavorsForNative(key);
            Vector vectorFlavors = new Vector(listFlavors);
            DataFlavor[] arrayFlavors = (DataFlavor[])vectorFlavors.toArray(new DataFlavor[0]);

            key = key.concat("TEST");   
                                        
            flavorMap.setFlavorsForNative(key, arrayFlavors);
                                        
            hashVerify.put(key, vectorFlavors);
        }

        
        
        verifyNewMappings();
    }

    
    
    public void verifyNewMappings() {
        
        for (Enumeration e = hashVerify.keys() ; e.hasMoreElements() ;) {
            String key = (String)e.nextElement();

            java.util.List listFlavors = flavorMap.getFlavorsForNative(key);
            Vector vectorFlavors = new Vector(listFlavors);

            
            if ( !vectorFlavors.equals((Vector)hashVerify.get(key))) {
                throw new RuntimeException("\n*** Error in verifyNewMappings()" +
                    "\nmethod1: setFlavorsForNative(String nat, DataFlavors[] flavors)" +
                    "\nmethod2: List getFlavorsForNative(String nat)" +
                    "\nString native: " + key +
                    "\nThe Returned List did not match the original set of DataFlavors.");
            }
        }
        System.out.println("*** native size = " + hashVerify.size());
    }
}

