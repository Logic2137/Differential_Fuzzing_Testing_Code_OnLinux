import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.security.auth.x500.X500Principal;

public class OIDMap {

    public static void main(String[] args) throws Exception {
        X500Principal p = null;
        Map<String, String> m1, m2 = null;
        p = new X500Principal("CN=user");
        try {
            p.getName("RFC2253", null);
            throw new Exception("expected NullPointerException for null oidMap");
        } catch (NullPointerException npe) {
        }
        m1 = Collections.singletonMap("FOO", "1.2.3");
        m2 = Collections.singletonMap("1.2.3", "*&$");
        p = new X500Principal("FOO=user", m1);
        try {
            p.getName("RFC2253", m2);
            throw new Exception("expected IllegalArgumentException for bad keyword");
        } catch (IllegalArgumentException iae) {
        }
        try {
            m2 = Collections.singletonMap("1.2.3", "1abc");
            p.getName("RFC2253", m2);
            throw new Exception("expected IllegalArgumentException for bad keyword");
        } catch (IllegalArgumentException iae) {
        }
        try {
            m2 = Collections.singletonMap("1.2.3", "");
            p.getName("RFC2253", m2);
            throw new Exception("expected IllegalArgumentException for bad keyword");
        } catch (IllegalArgumentException iae) {
        }
        try {
            m2 = Collections.singletonMap("1.2.3", "a1_b)a");
            p.getName("RFC2253", m2);
            throw new Exception("expected IllegalArgumentException for bad keyword");
        } catch (IllegalArgumentException iae) {
        }
        m1 = Collections.singletonMap("*&D", "FOO");
        p = new X500Principal("CN=user");
        p.getName("RFC2253", m1);
        m1 = Collections.singletonMap("2.5.4.3", "FOO");
        p = new X500Principal("CN=user");
        if (!p.getName("RFC2253", m1).startsWith("FOO")) {
            throw new Exception("mapping did not override builtin OID");
        }
        try {
            p.getName("CANONICAL", m1);
            throw new Exception("expected IllegalArgumentException for CANONICAL format");
        } catch (IllegalArgumentException iae) {
        }
        try {
            p.getName("YABBADABBADOO", m1);
            throw new Exception("expected IllegalArgumentException for invalid format");
        } catch (IllegalArgumentException iae) {
        }
        m1 = Collections.singletonMap("1.1", "BAR");
        p = new X500Principal("1.1=sean");
        System.out.println(p.getName("RFC1779", m1));
        System.out.println(p.getName("RFC2253", m1));
        if (!p.getName("RFC1779", m1).startsWith("BAR")) {
            throw new Exception("mapping did not override builtin OID");
        }
    }
}
