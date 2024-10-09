import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;
import java.util.Properties;

public class PropertiesSerialization {

    private static final Properties TEST_PROPS;

    static {
        TEST_PROPS = new Properties();
        TEST_PROPS.setProperty("one", "two");
        TEST_PROPS.setProperty("buckle", "shoe");
        TEST_PROPS.setProperty("three", "four");
        TEST_PROPS.setProperty("shut", "door");
    }

    private static final String TEST_SER_BASE64 = "rO0ABXNyABRqYXZhLnV0aWwuUHJvcGVydGllczkS0HpwNj6YAgABTAAIZGVmYXVs" + "dHN0ABZMamF2YS91dGlsL1Byb3BlcnRpZXM7eHIAE2phdmEudXRpbC5IYXNodGFi" + "bGUTuw8lIUrkuAMAAkYACmxvYWRGYWN0b3JJAAl0aHJlc2hvbGR4cD9AAAAAAAAI" + "dwgAAAALAAAABHQAA29uZXQAA3R3b3QABHNodXR0AARkb29ydAAGYnVja2xldAAE" + "c2hvZXQABXRocmVldAAEZm91cnhw";

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length == 0) {
            System.err.println("Run with 'read' or 'write'");
            System.err.println("  read mode:  normal test mode.");
            System.err.println("              Confirms that serial stream can");
            System.err.println("              be deserialized as expected.");
            System.err.println("  write mode: meant for updating the test,");
            System.err.println("              should the serial form change.");
            System.err.println("              Test output should be pasted");
            System.err.println("              back into the test source.");
            return;
        }
        Properties deserializedObject;
        if ("read".equals(args[0])) {
            ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(TEST_SER_BASE64));
            try (ObjectInputStream ois = new ObjectInputStream(bais)) {
                deserializedObject = (Properties) ois.readObject();
            }
            if (!TEST_PROPS.equals(deserializedObject)) {
                throw new RuntimeException("deserializedObject not equals()");
            }
            System.out.println("Test passed");
        } else if ("write".equals(args[0])) {
            System.out.println("\nTo update the test, paste the following back " + "into the test code:\n");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(TEST_PROPS);
            oos.flush();
            oos.close();
            byte[] byteArray = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
            ObjectInputStream ois = new ObjectInputStream(bais);
            Properties deser = (Properties) ois.readObject();
            if (!TEST_PROPS.equals(deser)) {
                throw new RuntimeException("write: Deserialized != original");
            }
            final String base64 = Base64.getEncoder().encodeToString(byteArray);
            ByteArrayInputStream bais2 = new ByteArrayInputStream(Base64.getDecoder().decode(base64));
            ObjectInputStream ois2 = new ObjectInputStream(bais2);
            Properties deser2 = (Properties) ois2.readObject();
            if (!TEST_PROPS.equals(deser2)) {
                throw new RuntimeException("write: Deserialized base64 != " + "original");
            }
            System.out.println(dumpBase64SerialStream(base64));
        }
    }

    private static final String INDENT = "   ";

    private static String dumpBase64SerialStream(String base64) {
        final StringBuilder sb = new StringBuilder();
        sb.append(INDENT).append(" ").append('\n');
        sb.append(INDENT).append(" private static final String TEST_SER_BASE64 = ").append("\n").append(INDENT).append("      ");
        final int last = base64.length() - 1;
        for (int i = 0; i < base64.length(); i++) {
            if (i % 64 == 0)
                sb.append("\"");
            sb.append(base64.charAt(i));
            if (i % 64 == 63 || i == last) {
                sb.append("\"");
                if (i == last)
                    sb.append(";\n");
                else
                    sb.append("\n").append(INDENT).append("    + ");
            }
        }
        return sb.toString();
    }
}
