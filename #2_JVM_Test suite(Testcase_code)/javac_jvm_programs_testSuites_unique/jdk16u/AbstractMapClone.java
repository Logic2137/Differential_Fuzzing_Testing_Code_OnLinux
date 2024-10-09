



import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AbstractMapClone extends AbstractMap implements Cloneable {

    private Map map = new HashMap();

    public Set entrySet() {
        return map.entrySet();
    }

    public Object put(Object key, Object value) {
        return map.put(key, value);
    }

    public Object clone() {
        final AbstractMapClone clone;
        try {
            clone = (AbstractMapClone)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(e);
        }
        clone.map = (Map)((HashMap)map).clone();
        return clone;
    }

    public static void main(String[] args) {
        AbstractMapClone m1 = new AbstractMapClone();
        m1.put("1", "1");
        Set k1 = m1.keySet();
        AbstractMapClone m2 = (AbstractMapClone)m1.clone();
        Set k2 = m2.keySet();
        m2.put("2","2");
        if (k1.equals(k2)) {
            throw new RuntimeException("AbstractMap.clone() failed.");
        }
    }
}
