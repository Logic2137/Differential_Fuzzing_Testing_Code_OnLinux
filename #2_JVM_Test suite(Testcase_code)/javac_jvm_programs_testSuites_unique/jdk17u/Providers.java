import java.security.Provider;
import java.security.Security;

public class Providers {

    public static void setAt(Provider p, int pos) throws Exception {
        if (Security.getProvider(p.getName()) != null) {
            Security.removeProvider(p.getName());
        }
        if (Security.insertProviderAt(p, pos) == -1) {
            throw new Exception("cannot setAt");
        }
    }
}
