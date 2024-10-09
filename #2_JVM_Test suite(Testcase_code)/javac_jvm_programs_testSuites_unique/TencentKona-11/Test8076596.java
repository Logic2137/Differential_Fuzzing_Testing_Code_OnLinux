



import java.security.AccessController;
import java.security.PrivilegedAction;

public class Test8076596 extends SecurityManager {
    public Test8076596() {
        
        AccessController.doPrivileged((PrivilegedAction<Void>) () -> null);
        
        AccessController.doPrivileged(new PrivilegedAction<Void>() {
            @Override
            public Void run() {
                return null;
            }
        });
    }

    public static void main(String[] args) {
        
    }
}
