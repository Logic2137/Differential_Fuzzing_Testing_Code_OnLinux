import java.util.Properties;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

public class StringPropertyNames {

    private static int NUM_SHARE_PROPS = 2;

    private static int NUM_PROPS1 = 3;

    private static int NUM_PROPS2 = 5;

    private static String KEY = "good.property.";

    private static String VALUE = "good.value.";

    public static void main(String[] argv) throws Exception {
        Properties props1 = new Properties();
        Properties props2 = new Properties(props1);
        for (int i = 0; i < NUM_PROPS1; i++) {
            props1.put(KEY + "1." + i, VALUE + "1." + i);
        }
        for (int i = 0; i < NUM_PROPS2; i++) {
            props2.put(KEY + "2." + i, VALUE + "2." + i);
        }
        for (int i = 0; i < NUM_SHARE_PROPS; i++) {
            props1.put(KEY + i, VALUE + "1." + i);
            props2.put(KEY + i, VALUE + "2." + i);
        }
        checkProperties(props1, NUM_PROPS1 + NUM_SHARE_PROPS, NUM_PROPS1 + NUM_SHARE_PROPS, NUM_PROPS1 + NUM_SHARE_PROPS, false);
        checkProperties(props2, NUM_PROPS2 + NUM_SHARE_PROPS, NUM_PROPS1 + NUM_PROPS2 + NUM_SHARE_PROPS, NUM_PROPS1 + NUM_PROPS2 + NUM_SHARE_PROPS, false);
        props1.put(KEY + "9", new Integer(4));
        checkProperties(props1, NUM_PROPS1 + NUM_SHARE_PROPS + 1, NUM_PROPS1 + NUM_SHARE_PROPS, NUM_PROPS1 + NUM_SHARE_PROPS + 1, false);
        checkProperties(props2, NUM_PROPS2 + NUM_SHARE_PROPS, NUM_PROPS1 + NUM_PROPS2 + NUM_SHARE_PROPS, NUM_PROPS1 + NUM_PROPS2 + NUM_SHARE_PROPS + 1, false);
        Object v = props1.remove(KEY + "9");
        if (v == null) {
            throw new RuntimeException("Test Failed: " + "Key " + KEY + "9" + " not found");
        }
        props1.put(new Integer(5), "good.value.5");
        props2.put(new Object(), new Object());
        checkProperties(props1, NUM_PROPS1 + NUM_SHARE_PROPS + 1, NUM_PROPS1 + NUM_SHARE_PROPS, NUM_PROPS1 + NUM_SHARE_PROPS + 1, true);
        checkProperties(props2, NUM_PROPS2 + NUM_SHARE_PROPS + 1, NUM_PROPS1 + NUM_PROPS2 + NUM_SHARE_PROPS, NUM_PROPS1 + NUM_PROPS2 + NUM_SHARE_PROPS + 2, true);
        System.out.println("Test passed.");
    }

    private static void checkProperties(Properties props, int propSize, int numStringKeys, int enumerateSize, boolean hasNonStringKeys) {
        if (props.size() != propSize) {
            throw new RuntimeException("Test Failed: " + "Expected number of properties = " + propSize + " but found = " + props.size());
        }
        Set<String> keys = props.stringPropertyNames();
        if (keys.size() != numStringKeys) {
            throw new RuntimeException("Test Failed: " + "Expected number of String keys = " + numStringKeys + " but found = " + keys.size());
        }
        boolean cceThrown = false;
        try {
            int count = 0;
            Enumeration<?> e = props.propertyNames();
            for (; e.hasMoreElements(); e.nextElement()) {
                count++;
            }
            if (count != enumerateSize) {
                throw new RuntimeException("Test Failed: " + "Expected number of enumerated keys = " + enumerateSize + " but found = " + count);
            }
        } catch (ClassCastException e) {
            if (!hasNonStringKeys) {
                RuntimeException re = new RuntimeException("Test Failed: " + "ClassCastException is expected not to be thrown");
                re.initCause(e);
                throw re;
            }
            cceThrown = true;
        }
        if ((hasNonStringKeys && !cceThrown)) {
            throw new RuntimeException("Test Failed: " + "ClassCastException is expected to be thrown");
        }
        try {
            keys.add("xyzzy");
            throw new RuntimeException("Test Failed: " + "add() should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException ignore) {
        }
        Iterator<String> it = keys.iterator();
        if (it.hasNext()) {
            try {
                keys.remove(it.next());
                throw new RuntimeException("Test Failed: " + "remove() should have thrown UnsupportedOperationException");
            } catch (UnsupportedOperationException ignore) {
            }
        }
    }
}
