import java.util.Set;
import javax.security.auth.Subject;
import javax.security.auth.x500.X500Principal;

public class Synch3 {

    static volatile boolean finished = false;

    public static void main(String[] args) {
        Subject subject = new Subject();
        final Set principals = subject.getPrincipals();
        principals.add(new X500Principal("CN=Alice"));
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
            subject.getPrincipals(X500Principal.class);
        }
        finished = true;
    }
}
