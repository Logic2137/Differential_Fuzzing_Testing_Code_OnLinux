



import java.util.*;
import java.io.*;

public class ReverseOrder {
    static byte[] serialBytes(Object o) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(o);
            oos.flush();
            oos.close();
            return bos.toByteArray();
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    @SuppressWarnings("unchecked")
    static <T> T serialClone(T o) {
        try {
            ObjectInputStream ois = new ObjectInputStream
                (new ByteArrayInputStream(serialBytes(o)));
            T clone = (T) ois.readObject();
            return clone;
        } catch (Throwable t) {
            throw new Error(t);
        }
    }

    public static void main(String[] args) throws Exception {
        Foo[] a = { new Foo(2), new Foo(3), new Foo(1) };
        List list = Arrays.asList(a);
        Comparator cmp = Collections.reverseOrder();
        Collections.sort(list, cmp);

        Foo[] golden = { new Foo(3), new Foo(2), new Foo(1) };
        List goldenList = Arrays.asList(golden);
        if (!list.equals(goldenList))
            throw new Exception(list.toString());

        Comparator clone = serialClone(cmp);
        List list2 = Arrays.asList(a);
        Collections.sort(list2, clone);
        if (!list2.equals(goldenList))
            throw new Exception(list.toString());
    }
}

class Foo implements Comparable {
    int val;
    Foo(int i) { val = i; }

    public int compareTo(Object o) {
        Foo f = (Foo)o;
        return (val < f.val ? Integer.MIN_VALUE : (val == f.val ? 0 : 1));
    }

    public boolean equals(Object o) {
        return o instanceof Foo && ((Foo)o).val == val;
    }

    public int hashCode()    { return val; }

    public String toString() { return Integer.toString(val); }
}
