



import java.security.*;
import java.util.*;

public class NewInstance {

    public static void main(String[] args) throws Exception {
        for (Provider p : Security.getProviders()) {
            System.out.println("---------");
            System.out.println(p.getName() + ":" + p.getInfo());
            if (p.getName().equals("SunPCSC")) {
                System.out.println("A smartcard might not be installed. Skip test.");
                continue;
            }
            Set<Provider.Service> set = p.getServices();
            Iterator<Provider.Service> i = set.iterator();

            while (i.hasNext()) {
                Provider.Service s = i.next();
                System.out.println(s.getType() + "." + s.getAlgorithm());
                try {
                    s.newInstance(null);
                } catch (NoSuchAlgorithmException e) {
                    System.out.println("  check");
                    Throwable t = e.getCause();
                    if (!(t instanceof InvalidAlgorithmParameterException)) {
                        
                        
                        
                        throw e;
                    }
                }
            }
        }
    }
}
