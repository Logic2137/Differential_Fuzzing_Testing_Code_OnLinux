import java.lang.reflect.Constructor;
import java.util.prefs.Preferences;
import java.util.prefs.PreferencesFactory;

public class CodePointZeroPrefsTest {

    public static void main(String[] args) throws Exception {
        int failures = 0;
        Preferences node = Preferences.userRoot().node("com/acme/testing");
        try {
            node.put("a", "1");
        } catch (IllegalArgumentException iae) {
            System.err.println("Unexpected IllegalArgumentException for legal put() key");
            failures++;
        }
        try {
            node.put("a\u0000b", "1");
            System.err.println("IllegalArgumentException not thrown for illegal put() key");
            failures++;
        } catch (IllegalArgumentException iae) {
        }
        try {
            node.put("ab", "2\u00003");
            System.err.println("IllegalArgumentException not thrown for illegal put() value");
            failures++;
        } catch (IllegalArgumentException iae) {
        }
        try {
            node.put("a\u0000b", "2\u00003");
            System.err.println("IllegalArgumentException not thrown for illegal put() entry");
            failures++;
        } catch (IllegalArgumentException iae) {
        }
        try {
            String theDefault = "default";
            String value = node.get("a\u0000b", theDefault);
            System.err.println("IllegalArgumentException not thrown for illegal get() key");
            failures++;
        } catch (IllegalArgumentException iae) {
        }
        try {
            node.remove("a\u0000b");
            System.err.println("IllegalArgumentException not thrown for illegal remove() key");
            failures++;
        } catch (IllegalArgumentException iae) {
        }
        node.removeNode();
        if (failures != 0) {
            throw new RuntimeException("CodePointZeroPrefsTest failed with " + failures + " errors!");
        }
    }
}
