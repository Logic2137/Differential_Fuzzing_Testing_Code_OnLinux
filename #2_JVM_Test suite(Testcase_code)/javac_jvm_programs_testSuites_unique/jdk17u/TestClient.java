
package c;

import java.security.Provider;
import java.security.Security;
import java.util.Iterator;
import java.util.ServiceLoader;

public class TestClient {

    public static void main(String[] args) throws Exception {
        Provider p = null;
        if (args != null && args.length > 1) {
            switch(args[0]) {
                case "CL":
                    p = (Provider) Class.forName(args[1]).newInstance();
                    if (Security.addProvider(p) == -1) {
                        throw new RuntimeException("Failed to add provider");
                    }
                    break;
                case "SL":
                    ServiceLoader<Provider> services = ServiceLoader.load(java.security.Provider.class);
                    Iterator<Provider> iterator = services.iterator();
                    while (iterator.hasNext()) {
                        Provider spr = iterator.next();
                        if (spr.getName().equals(args[1])) {
                            p = spr;
                            if (Security.addProvider(p) == -1) {
                                throw new RuntimeException("Failed to add provider");
                            }
                            break;
                        }
                    }
                    break;
                case "SPN":
                case "SPT":
                    p = Security.getProvider(args[1]);
                    break;
                default:
                    throw new RuntimeException("Invalid argument.");
            }
        }
        if (p == null) {
            throw new RuntimeException("Provider TestProvider not found");
        }
        System.out.printf("Client: found provider %s", p.getName());
    }
}
