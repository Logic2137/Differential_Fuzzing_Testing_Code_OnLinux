



import java.util.HashMap;
import java.lang.ref.WeakReference;

public class HashMapCloneLeak {

    static WeakReference<Object> wr = null;

    
    private static HashMap<Integer, Object> makeMap() {
        HashMap<Integer, Object> map = new HashMap<Integer, Object>();
        Object testObject = new Object();
        wr = new WeakReference<Object>(testObject);
        map.put(42, testObject);
        return map;
    }

    public static void main(String[] args) throws Exception {
        HashMap<Integer, Object> hm = makeMap();
        hm = (HashMap<Integer, Object>)hm.clone();
        hm.clear();
        
        
        
        Object[] chain = null;
        while (wr.get() != null) {
            try {
                Object[] allocate = new Object[1000000];
                allocate[0] = chain;
                chain = allocate;
            } catch (OutOfMemoryError oome) {
                chain = null;
            }
            System.gc();
            Thread.sleep(100);
        }
    }

}
