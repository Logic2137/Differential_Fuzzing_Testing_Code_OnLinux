



import java.security.PrivilegedAction;
import java.util.Collections;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;

public class TrustedCert {

    public static void main(String[] args) {
        System.out.println(
            Subject.doAsPrivileged(
                new Subject(true,
                            Collections.singleton(new X500Principal("CN=Tim")),
                            Collections.EMPTY_SET,
                            Collections.EMPTY_SET),
                new PrivilegedAction() {
                    public Object run() {
                        return System.getProperty("foo");
                    }
                },
                null));
    }
}
