



import javax.security.auth.Subject;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public class UnreliableContains {

    public static void main(String[] args) {
        MySet<Principal> set = new MySet<>();
        set.add(null);
        Subject s = null;
        try {
            s = new Subject(false, set, Collections.emptySet(),
                    Collections.emptySet());
        } catch (NullPointerException e) {
            
            return;
        }
        
        for (Principal p : s.getPrincipals()) {
            Objects.requireNonNull(p);
        }
        
        throw new RuntimeException("Fail");
    }

    
    static class MySet<E> extends HashSet<E> {
        @Override
        public boolean contains(Object o) {
            if (o == null) {
                return false;
            } else {
                return super.contains(o);
            }
        }
    }
}
