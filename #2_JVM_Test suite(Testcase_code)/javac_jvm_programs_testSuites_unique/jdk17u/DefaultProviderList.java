import java.security.Provider;
import java.security.Security;
import java.util.Arrays;
import java.util.Iterator;
import java.util.ServiceLoader;

public class DefaultProviderList {

    public static void main(String[] args) throws Exception {
        Provider[] defaultProvs = Security.getProviders();
        System.out.println("Providers: " + Arrays.asList(defaultProvs));
        System.out.println();
        ServiceLoader<Provider> sl = ServiceLoader.load(Provider.class);
        boolean failed = false;
        Module baseMod = Object.class.getModule();
        for (Provider p : defaultProvs) {
            String pName = p.getName();
            Class pClass = p.getClass();
            if (pClass.getModule() != baseMod) {
                String pClassName = pClass.getName();
                Iterator<Provider> provIter = sl.iterator();
                boolean found = false;
                while (provIter.hasNext()) {
                    Provider pFromSL = provIter.next();
                    if (pFromSL.getClass().getName().equals(pClassName)) {
                        found = true;
                        System.out.println("SL found provider " + pName);
                        break;
                    }
                }
                if (!found) {
                    failed = true;
                    System.out.println("Error: SL cannot find provider " + pName);
                }
            }
        }
        Iterator<Provider> provIter = sl.iterator();
        while (provIter.hasNext()) {
            Provider pFromSL = provIter.next();
            if (pFromSL.getClass().getModule() == baseMod) {
                failed = true;
                System.out.println("Error: base provider " + pFromSL.getName() + " loaded by SL");
            }
        }
        if (!failed) {
            System.out.println("Test Passed");
        } else {
            throw new Exception("One or more tests failed");
        }
    }
}
