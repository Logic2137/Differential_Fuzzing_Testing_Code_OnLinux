


import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;
import sun.security.krb5.Config;
import sun.security.krb5.SCDynamicStoreConfig;

public class SCDynamicConfigTest {

    static Vector<Hashtable<String,String>>hosts() {
        Vector <Hashtable<String,String>> result = new Vector<>();
        Hashtable<String,String> pair = new Hashtable<>();
        pair.put("host", "127.0.0.1");
        result.add(pair);
        pair = new Hashtable<>();
        pair.put("host", "127.0.0.2");
        result.add(pair);
        return result;
    }

    public static void main(String[] args) throws Exception {
        
        Hashtable<String, Object> conf = new Hashtable<>();

        Hashtable<String, Object> libdefaults = new Hashtable<>();
        libdefaults.put("default_realm", "REALM.COM");
        conf.put("libdefaults", libdefaults);

        Hashtable<String, Object> realms = new Hashtable<>();
        Hashtable<String, Object> thisRealm = new Hashtable<>();
        realms.put("REALM.COM", thisRealm);
        thisRealm.put("kpasswd", hosts());
        thisRealm.put("kadmin", hosts());
        thisRealm.put("kdc", hosts());
        conf.put("realms", realms);

        Hashtable<String, Object> domain_realm = new Hashtable<>();
        domain_realm.put(".realm.com", "REALM.COM");
        domain_realm.put("realm.com", "REALM.COM");
        conf.put("domain_realm", domain_realm);

        System.out.println("SCDynamicConfig:\n");
        System.out.println(conf);

        
        Method m = SCDynamicStoreConfig.class.getDeclaredMethod(
                "convertNativeConfig", Hashtable.class);
        m.setAccessible(true);
        conf = (Hashtable)m.invoke(null, conf);

        System.out.println("\nkrb5.conf:\n");
        System.out.println(conf);

        
        System.setProperty("java.security.krb5.conf", "not-a-file");
        Config cf = Config.getInstance();
        Field f = Config.class.getDeclaredField("stanzaTable");
        f.setAccessible(true);
        f.set(cf, conf);

        System.out.println("\nConfig:\n");
        System.out.println(cf);

        if (!cf.getDefaultRealm().equals("REALM.COM")) {
            throw new Exception();
        }
        if (!cf.getKDCList("REALM.COM").equals("127.0.0.1 127.0.0.2")) {
            throw new Exception();
        }
        if (!cf.get("domain_realm", ".realm.com").equals("REALM.COM")) {
            throw new Exception();
        }
    }
}
