


import sun.security.krb5.Config;

public class Duplicates {
    public static void main(String[] args) throws Exception {
        System.setProperty("java.security.krb5.conf",
                System.getProperty("test.src", ".") +"/k1.conf");
        Config config = Config.getInstance();
        config.listTable();
        String s;

        
        s = config.get("libdefaults", "default_realm");
        if (!s.equals("R1")) {
            throw new Exception();
        }
        
        s = config.get("libdefaults", "default_tkt_enctypes");
        if (!s.equals("aes128-cts")) {
            throw new Exception();
        }
        s = config.get("realms", "R1", "kdc");
        if (!s.equals("k1")) {
            throw new Exception(s);
        }
        
        
        s = config.getAll("realms", "R2", "kdc");
        if (!s.equals("k1 k2 k3 k4")) {
            throw new Exception(s);
        }
        
        s = config.getAll("capaths", "R1", "R2");
        if (!s.equals("R3 R4 R5 R6")) {
            throw new Exception(s);
        }
        
        s = config.get("new", "x", "y", "z", "a", "b", "c");
        if (!s.equals("d")) {
            throw new Exception(s);
        }
    }
}
