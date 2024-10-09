import java.security.Provider;
import java.security.Security;
import java.lang.Exception;

public class ProviderVersionCheck {

    public static void main(String[] arg) throws Exception {
        boolean failure = false;
        for (Provider p : Security.getProviders()) {
            System.out.print(p.getName() + " ");
            String specVersion = System.getProperty("java.specification.version");
            if (p.getVersion() != Double.parseDouble(specVersion)) {
                System.out.println("failed. " + "Version received was " + p.getVersion());
                failure = true;
            } else {
                System.out.println("passed.");
            }
        }
        if (failure) {
            throw new Exception("Provider(s) failed to have the expected " + "version value.");
        }
    }
}
