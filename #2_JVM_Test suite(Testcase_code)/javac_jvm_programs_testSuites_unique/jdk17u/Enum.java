import java.util.Collections;
import java.util.List;
import java.util.Vector;

public class Enum {

    public static void main(String[] args) throws Exception {
        int[] sizes = { 0, 10, 100 };
        for (int i = 0; i < sizes.length; i++) {
            Vector v = new Vector();
            int size = sizes[i];
            for (int j = 0; j < size; j++) v.add(new Integer(j));
            List l = Collections.list(v.elements());
            if (!l.equals(v))
                throw new Exception("Copy failed: " + size);
        }
    }
}
