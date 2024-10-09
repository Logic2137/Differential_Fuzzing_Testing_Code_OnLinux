

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.SystemFlavorMap;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;



public class SetDataFlavorsTest {

    SystemFlavorMap flavorMap;
    Hashtable hashVerify;

    Map mapFlavors;
    Map mapNatives;

    Hashtable hashFlavors;
    Hashtable hashNatives;

    public static void main (String[] args) {
        new SetDataFlavorsTest().doTest();
    }

    public void doTest() {
        flavorMap = (SystemFlavorMap)SystemFlavorMap.getDefaultFlavorMap();

        
        mapFlavors = flavorMap.getNativesForFlavors(null);
        mapNatives = flavorMap.getFlavorsForNatives(null);

        hashFlavors = new Hashtable(mapFlavors);
        hashNatives = new Hashtable(mapNatives);


        
        
        
        
        
        
        
        DataFlavor key;
        hashVerify = new Hashtable();

        for (Enumeration e = hashFlavors.keys() ; e.hasMoreElements() ;) {
            key = (DataFlavor)e.nextElement();

            java.util.List listNatives = flavorMap.getNativesForFlavor(key);
            Vector vectorNatives = new Vector(listNatives);
            String[] arrayNatives = (String[])vectorNatives.toArray(new String[0]);

            
            
            
            

            StringBuffer mimeType = new StringBuffer(key.getMimeType());
            mimeType.insert(mimeType.indexOf(";"),"-TEST");

            DataFlavor testFlav = new DataFlavor(mimeType.toString(), "Test DataFlavor");

                                        
            flavorMap.setNativesForFlavor(testFlav, arrayNatives);
                                        
            hashVerify.put(testFlav, vectorNatives);
        }

        
        
        verifyNewMappings();
    }

    
    
    public void verifyNewMappings() {
        
        for (Enumeration e = hashVerify.keys() ; e.hasMoreElements() ;) {
            DataFlavor key = (DataFlavor)e.nextElement();

            java.util.List listNatives = flavorMap.getNativesForFlavor(key);
            Vector vectorFlavors = new Vector(listNatives);

            
            if ( !vectorFlavors.equals(hashVerify.get(key))) {
                throw new RuntimeException("\n*** Error in verifyNewMappings()" +
                    "\nmethod1: setNativesForFlavor(DataFlavor flav, String[] natives)" +
                    "\nmethod2: List getNativesForFlavor(DataFlavor flav)" +
                    "\nDataFlavor: " + key.getMimeType() +
                    "\nThe Returned List did not match the original set of Natives.");
            }
        }
        System.out.println("*** DataFlavor size = " + hashVerify.size());
    }
}

