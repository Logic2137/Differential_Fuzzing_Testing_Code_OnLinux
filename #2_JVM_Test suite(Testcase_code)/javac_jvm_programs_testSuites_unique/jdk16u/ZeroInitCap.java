

import java.util.Map;
import java.util.WeakHashMap;



public class ZeroInitCap {
    public static void main(String[] args) {
        Map map = new WeakHashMap(0);
        map.put("a","b");
    }
}
