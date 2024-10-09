



import java.security.Principal;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;

public class Synch {
    static volatile boolean finished = false;
    public static void main(String[] args) {
        Subject subject = new Subject();
        final Set principals = subject.getPrincipals();
        principals.add(new X500Principal("CN=Alice"));
        new Thread() {
            public void run() {
                Principal last = new X500Principal("CN=Bob");
                for (int i = 0; !finished; i++) {
                    Principal next = new X500Principal("CN=Bob" + i);
                    principals.add(next);
                    principals.remove(last);
                    last = next;
                }
            }
        }.start();
        for (int i = 0; i < 1000; i++) {
            Subject.doAs(
                subject,
                new PrivilegedAction() {
                    public Object run() {
                        return Subject.doAs(
                            new Subject(true,
                                        Collections.singleton(
                                            new X500Principal("CN=Claire")),
                                        Collections.EMPTY_SET,
                                        Collections.EMPTY_SET),
                            new PrivilegedAction() {
                                public Object run() {
                                    return null;
                                }
                            });
                    }
                });
        }
        finished = true;
    }
}
