import java.security.Provider;
import java.security.Security;

public class ProvidersSnapshot {

    private Provider[] oldProviders;

    private ProvidersSnapshot() {
        oldProviders = Security.getProviders();
    }

    public static ProvidersSnapshot create() {
        return new ProvidersSnapshot();
    }

    public void restore() {
        Provider[] newProviders = Security.getProviders();
        for (Provider p : newProviders) {
            Security.removeProvider(p.getName());
        }
        for (Provider p : oldProviders) {
            Security.addProvider(p);
        }
    }
}
