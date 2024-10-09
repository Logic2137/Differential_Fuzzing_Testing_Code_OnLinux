



import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Iterator;

public class EmptyMapIterator {
    public static void main(String[] args) throws Exception {
        HashMap map = new HashMap();
        Iterator iter = map.entrySet().iterator();
        map.put("key", "value");

        try {
            iter.next();
            throw new Exception("No exception thrown");
        } catch (ConcurrentModificationException e) {
        }
    }
}
