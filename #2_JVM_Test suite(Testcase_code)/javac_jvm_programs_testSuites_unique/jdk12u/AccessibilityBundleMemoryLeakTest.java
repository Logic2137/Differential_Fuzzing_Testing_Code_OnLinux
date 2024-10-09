

import java.lang.reflect.Field;
import java.util.Hashtable;
import java.util.Locale;

import javax.accessibility.AccessibleRole;
import javax.accessibility.AccessibleBundle;

public class AccessibilityBundleMemoryLeakTest extends AccessibleRole {
    public AccessibilityBundleMemoryLeakTest() {
        super("");
    }

    public static void main(String... args) throws Exception {
        AccessibilityBundleMemoryLeakTest role = new AccessibilityBundleMemoryLeakTest();

        Field field = AccessibleBundle.class.getDeclaredField("table");
        field.setAccessible(true);

        final Hashtable table = (Hashtable)field.get(role);
        Locale locale = Locale.getDefault();

        role.toDisplayString();
        Object obj = table.get(locale);

        role.toDisplayString();
        Object obj1 = table.get(locale);

        if (obj != obj1) {
            throw new RuntimeException("Test case failed: AccessibleBundle allocates new value for existing key!");
        }

        System.out.println("Test passed.");

    }
}
