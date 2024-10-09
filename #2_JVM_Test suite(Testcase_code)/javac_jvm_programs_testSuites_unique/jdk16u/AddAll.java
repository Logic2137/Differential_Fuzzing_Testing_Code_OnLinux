



import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.WeakHashMap;

public class AddAll {
    public static void main(String[] args) {
        for (int j = 0; j < 1; j++) {
            Map m = new WeakHashMap(100000);
            for (int i = 0; i < 100000; i++)
                m.put(new Object(), Boolean.TRUE);
            new ArrayList().addAll(m.keySet());
        }

        for (int j = 0; j < 1; j++) {
            Map m = new WeakHashMap(100000);
            for (int i = 0; i < 100000; i++)
                m.put(new Object(), Boolean.TRUE);
            new LinkedList().addAll(m.keySet());
        }

        for (int j = 0; j < 1; j++) {
            Map m = new WeakHashMap(100000);
            for (int i = 0; i < 100000; i++)
                m.put(new Object(), Boolean.TRUE);
            new Vector().addAll(m.keySet());
        }

        for (int j = 0; j < 1; j++) {
            Map m = new WeakHashMap(100000);
            for (int i = 0; i < 100000; i++)
                m.put(new Object(), Boolean.TRUE);
            List list = new ArrayList();
            list.add("inka"); list.add("dinka"); list.add("doo");
            list.addAll(1, m.keySet());
        }

        for (int j = 0; j < 1; j++) {
            Map m = new WeakHashMap(100000);
            for (int i = 0; i < 100000; i++)
                m.put(new Object(), Boolean.TRUE);
            List list = new LinkedList();
            list.add("inka"); list.add("dinka"); list.add("doo");
            list.addAll(1, m.keySet());
        }

        for (int j = 0; j < 1; j++) {
            Map m = new WeakHashMap(100000);
            for (int i = 0; i < 100000; i++)
                m.put(new Object(), Boolean.TRUE);
            List list = new ArrayList();
            list.add("inka"); list.add("dinka"); list.add("doo");
            list.addAll(1, m.keySet());
        }
    }
}
