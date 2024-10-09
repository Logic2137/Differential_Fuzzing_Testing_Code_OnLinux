

import java.io.*;
import java.lang.reflect.Field;
import java.util.Hashtable;


public class DeserializedLength {

    static boolean testDeserializedLength(int elements, float loadFactor) throws Exception {

        
        Hashtable<Integer, Integer> ht1 = new Hashtable<>(1, loadFactor);

        
        for (int i = 0; i < elements; i++) {
            ht1.put(i, i);
        }

        
        Hashtable<Integer, Integer> ht2 = serialClone(ht1);

        
        Object[] table1 = (Object[]) hashtableTableField.get(ht1);
        Object[] table2 = (Object[]) hashtableTableField.get(ht2);
        assert table1 != null;
        assert table2 != null;

        int minLength = (int) (ht1.size() / loadFactor) + 1;
        int maxLength = minLength * 2;

        boolean ok = (table2.length >= minLength && table2.length <= maxLength);

        System.out.printf(
            "%7d %5.2f %7d %7d %7d...%7d %s\n",
            ht1.size(), loadFactor,
            table1.length, table2.length,
            minLength, maxLength,
            (ok ? "OK" : "NOT-OK")
        );

        return ok;
    }

    static <T> T serialClone(T o) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(o);
        }
        @SuppressWarnings("unchecked")
        T clone = (T) new ObjectInputStream(
            new ByteArrayInputStream(bos.toByteArray())).readObject();
        return clone;
    }

    private static final Field hashtableTableField;

    static {
        try {
            hashtableTableField = Hashtable.class.getDeclaredField("table");
            hashtableTableField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new Error(e);
        }
    }

    public static void main(String[] args) throws Exception {
        boolean ok = true;

        System.out.printf("Results:\n" +
                "                 ser.  deser.\n" +
                "   size  load  lentgh  length       valid range ok?\n" +
                "------- ----- ------- ------- ----------------- ------\n"
        );

        for (int elements : new int[]{10, 50, 500, 5000}) {
            for (float loadFactor : new float[]{0.15f, 0.5f, 0.75f, 1.0f, 2.5f}) {
                ok &= testDeserializedLength(elements, loadFactor);
            }
        }
        if (!ok) {
            throw new AssertionError("Test failed.");
        }
    }
}
