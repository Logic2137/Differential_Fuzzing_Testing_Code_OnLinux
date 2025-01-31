

 

import java.lang.reflect.*;
import java.util.*;

import sun.misc.*;

public class TestStringDedup {
    private static Field valueField;
    private static Unsafe unsafe;

    private static final int UniqueStrings = 20;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);

            valueField = String.class.getDeclaredField("value");
            valueField.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object getValue(String string) {
        try {
            return valueField.get(string);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static class StringAndId {
        private String str;
        private int id;

        public StringAndId(String str, int id) {
            this.str = str;
            this.id = id;
        }

        public String str() {
            return str;
        }

        public int id() {
            return id;
        }
    }

    private static void generateStrings(ArrayList<StringAndId> strs, int unique_strs) {
        Random rn = new Random();
        for (int u = 0; u < unique_strs; u++) {
            int n = rn.nextInt() % 10;
            n = Math.max(n, 2);
            for (int index = 0; index < n; index++) {
                strs.add(new StringAndId("Unique String " + u, u));
            }
        }
    }

    private static int verifyDedepString(ArrayList<StringAndId> strs) {
        HashMap<Object, StringAndId> seen = new HashMap<>();
        int total = 0;
        int dedup = 0;

        for (StringAndId item : strs) {
            total++;
            StringAndId existing_item = seen.get(getValue(item.str()));
            if (existing_item == null) {
                seen.put(getValue(item.str()), item);
            } else {
                if (item.id() != existing_item.id() ||
                        !item.str().equals(existing_item.str())) {
                    System.out.println("StringDedup error:");
                    System.out.println("String: " + item.str() + " != " + existing_item.str());
                    throw new RuntimeException("StringDedup Test failed");
                } else {
                    dedup++;
                }
            }
        }
        System.out.println("Dedup: " + dedup + "/" + total + " unique: " + (total - dedup));
        return (total - dedup);
    }

    public static void main(String[] args) {
        ArrayList<StringAndId> astrs = new ArrayList<>();
        generateStrings(astrs, UniqueStrings);
        System.gc();
        System.gc();
        System.gc();
        System.gc();
        System.gc();

        if (verifyDedepString(astrs) != UniqueStrings) {
            
            
            System.out.println("Not all strings are deduplicated");
        }
    }
}
