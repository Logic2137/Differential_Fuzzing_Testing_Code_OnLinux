



import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class ReplaceExisting {
    
    private static int ENTRIES = 13;

    public static void main(String[] args) {
        for (int i = 0; i <= ENTRIES; i++) {
            HashMap<Integer,Integer> hm = prepHashMap();
            testItr(hm, i);
        }
    }

    
    private static HashMap<Integer,Integer> prepHashMap() {
        HashMap<Integer,Integer> hm = new HashMap<>(16, 0.75f);
        
        for (int i = 0; i < ENTRIES; i++) {
            hm.put(i*10, i*10);
        }
        return hm;
    }

    
    private static void testItr(HashMap<Integer,Integer> hm, int elemBeforePut) {
        if (elemBeforePut > hm.size()) {
            throw new IllegalArgumentException("Error in test: elemBeforePut must be <= HashMap size");
        }
        
        HashSet<Integer> keys = new HashSet<>(hm.size());
        keys.addAll(hm.keySet());

        HashSet<Integer> collected = new HashSet<>(hm.size());

        
        Iterator<Integer> itr = hm.keySet().iterator();
        for (int i = 0; i < elemBeforePut; i++) {
            Integer retVal = itr.next();
            if (!collected.add(retVal)) {
                throw new RuntimeException("Corrupt iterator: key " + retVal + " already encountered");
            }
        }

        
        if (null == hm.put(0, 100)) {
            throw new RuntimeException("Error in test: expected key 0 to be in the HashMap");
        }

        
        while(itr.hasNext()) {
            Integer retVal = itr.next();
            if (!collected.add(retVal)) {
                throw new RuntimeException("Corrupt iterator: key " + retVal + " already encountered");
            }
        }

        
        if (!keys.equals(collected)) {
            throw new RuntimeException("Collected keys do not match original set of keys");
        }
    }
}
