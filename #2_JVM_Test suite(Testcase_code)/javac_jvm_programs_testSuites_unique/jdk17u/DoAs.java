import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.security.auth.Subject;

public class DoAs {

    public static void main(String[] args) throws Exception {
        final Set<String> outer = new HashSet<>(Arrays.asList("Outer"));
        final Subject subject = new Subject(true, Collections.EMPTY_SET, outer, Collections.EMPTY_SET);
        for (int i = 0; i < 100_000; ++i) {
            final int index = i;
            Subject.doAs(subject, (PrivilegedExceptionAction<Integer>) () -> {
                AccessControlContext c1 = AccessController.getContext();
                Subject s = Subject.getSubject(c1);
                if (s != subject) {
                    throw new AssertionError("outer Oops! " + "iteration " + index + " " + s + " != " + subject);
                }
                return 0;
            });
        }
    }
}
