

import java.security.Provider;
import java.security.Security;

public class ReadConfInUTF16Env {
    public static void main(String argv[]) {
        Provider p = Security.getProvider("SunPKCS11");
        if (p == null) {
            System.out.println("Skipping test - no PKCS11 provider available");
            return;
        }

        System.out.println(p.getName());
    }
}
