



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Map;

public class SimpleSerialization {
    public static void main(final String[] args) throws Exception {
        Hashtable<String, String> h1 = new Hashtable<>();

        System.err.println("*** BEGIN TEST ***");

        h1.put("key", "value");

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
            oos.writeObject(h1);
        }

        final byte[] data = baos.toByteArray();
        final ByteArrayInputStream bais = new ByteArrayInputStream(data);
        final Object deserializedObject;
        try (ObjectInputStream ois = new ObjectInputStream(bais)) {
            deserializedObject = ois.readObject();
        }

        if(!h1.getClass().isInstance(deserializedObject)) {
             throw new RuntimeException("Result not assignable to type of h1");
        }

        if (false == h1.equals(deserializedObject)) {
             Hashtable<String, String> d1 = (Hashtable<String, String>) h1;
            for(Map.Entry entry: h1.entrySet()) {
                System.err.println("h1.key::" + entry.getKey() + " d1.containsKey()::" + d1.containsKey((String) entry.getKey()));
                System.err.println("h1.value::" + entry.getValue() + " d1.contains()::" + d1.contains(entry.getValue()));
                System.err.println("h1.value == d1.value " + entry.getValue().equals(d1.get((String) entry.getKey())));
            }

            throw new RuntimeException(getFailureText(h1, deserializedObject));
        }
    }

    private static String getFailureText(final Object orig, final Object copy) {
        final StringWriter sw = new StringWriter();
        try (PrintWriter pw = new PrintWriter(sw)) {
            pw.println("Test FAILED: Deserialized object is not equal to the original object");
            pw.print("\tOriginal: ");
            printObject(pw, orig).println();
            pw.print("\tCopy:     ");
            printObject(pw, copy).println();
        }
        return sw.toString();
    }

    private static PrintWriter printObject(final PrintWriter pw, final Object o) {
        pw.printf("%s@%08x", o.getClass().getName(), System.identityHashCode(o));
        return pw;
    }
}
