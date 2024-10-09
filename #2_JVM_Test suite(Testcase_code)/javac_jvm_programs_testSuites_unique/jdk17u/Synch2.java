import java.util.Iterator;
import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;

public class Synch2 {

    static volatile boolean finished = false;

    public static void main(String[] args) {
        System.setSecurityManager(new SecurityManager());
        Subject subject = new Subject();
        final Set principals = subject.getPrincipals();
        principals.add(new X500Principal("CN=Alice"));
        final Set credentials = subject.getPrivateCredentials();
        credentials.add("Dummy credential");
        new Thread() {

            {
                start();
            }

            public void run() {
                X500Principal p = new X500Principal("CN=Bob");
                while (!finished) {
                    principals.add(p);
                    principals.remove(p);
                }
            }
        };
        for (int i = 0; i < 1000; i++) {
            synchronized (credentials) {
                for (Iterator it = credentials.iterator(); it.hasNext(); ) {
                    it.next();
                }
            }
        }
        finished = true;
    }
}
