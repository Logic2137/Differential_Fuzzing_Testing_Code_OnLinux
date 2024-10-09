import java.util.SimpleTimeZone;
import java.util.TimeZone;

public class SetDefaultSecurityTest {

    static final TimeZone NOWHERE = new SimpleTimeZone(Integer.MAX_VALUE, "Nowhere");

    public static void main(String[] args) {
        TimeZone defaultZone = TimeZone.getDefault();
        TimeZone.setDefault(NOWHERE);
        if (!NOWHERE.equals(TimeZone.getDefault())) {
            new RuntimeException("TimeZone.setDefault doesn't work for trusted code.");
        }
        TimeZone.setDefault(defaultZone);
        if (!defaultZone.equals(TimeZone.getDefault())) {
            new RuntimeException("TimeZone.setDefault doesn't restore defaultZone.");
        }
        System.setSecurityManager(new SecurityManager());
        try {
            TimeZone.setDefault(NOWHERE);
            throw new RuntimeException("TimeZone.setDefault doesn't throw a SecurityException.");
        } catch (SecurityException se) {
        }
        TimeZone tz = TimeZone.getDefault();
        if (!defaultZone.equals(tz)) {
            throw new RuntimeException("Default TimeZone changed: " + tz);
        }
    }
}
