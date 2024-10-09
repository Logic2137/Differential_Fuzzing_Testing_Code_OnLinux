


import java.io.*;
import java.lang.reflect.Method;
import sun.security.krb5.Config;

public class DnsFallback {

    static Method useDNS_Realm;
    static Method useDNS_KDC;

    public static void main(String[] args) throws Exception {

        useDNS_Realm = Config.class.getDeclaredMethod("useDNS_Realm");
        useDNS_Realm.setAccessible(true);
        useDNS_KDC = Config.class.getDeclaredMethod("useDNS_KDC");
        useDNS_KDC.setAccessible(true);


        
        check("true", "true", true, true);
        check("false", "true", false, false);
        check("true", "false", true, true);
        check("false", "false", false, false);
        check("true", null, true, true);
        check("false", null, false, false);
        check(null, "true", true, true);
        check(null, "false", false, false);

        
        

        
        check(null, null, false, true);
    }

    
    static void check(String u, String f, boolean r, boolean k)
            throws Exception {

        try (PrintStream ps =
                new PrintStream(new FileOutputStream("dnsfallback.conf"))) {
            ps.println("[libdefaults]\n");
            if (u != null) {
                ps.println("dns_lookup_realm=" + u);
                ps.println("dns_lookup_kdc=" + u);
            }
            if (f != null) {
                ps.println("dns_fallback=" + f);
            }
        }

        System.setProperty("java.security.krb5.conf", "dnsfallback.conf");
        Config.refresh();
        System.out.println("Testing " + u + ", " + f + ", " + r + ", " + k);

        if (!useDNS_Realm.invoke(Config.getInstance()).equals(r)) {
            throw new Exception("useDNS_Realm Fail");
        }

        if (!useDNS_KDC.invoke(Config.getInstance()).equals(k)) {
            throw new Exception("useDNS_KDC Fail");
        }
    }
}

