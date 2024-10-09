

import java.security.Provider;
import java.security.Security;

public class SunJSSEFIPSInitClient {
    public static void main(String[] args) throws Exception {
        boolean isSunJSSEFIPS = false;
        Provider[] provs = Security.getProviders();
        for (Provider p : provs) {
            if (p.getName().equals("SunJSSE") &&
                    p instanceof com.sun.net.ssl.internal.ssl.Provider) {
                isSunJSSEFIPS = ((com.sun.net.ssl.internal.ssl.Provider)p).isFIPS();
                break;
            }
        }
        System.out.println("SunJSSE.isFIPS(): " + isSunJSSEFIPS);
    }
}

