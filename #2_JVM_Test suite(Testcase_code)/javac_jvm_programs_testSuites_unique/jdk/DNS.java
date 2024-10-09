


import sun.security.krb5.Config;
import sun.security.krb5.KrbException;

public class DNS {
    public static void main(String[] args) throws Exception {
        System.setProperty("java.security.krb5.conf",
                System.getProperty("test.src", ".") +"/no-such-file.conf");
        Config config = Config.getInstance();
        try {
            String r = config.getDefaultRealm();
            throw new Exception("What? There is a default realm " + r + "?");
        } catch (KrbException ke) {
            ke.printStackTrace();
            if (ke.getCause() != null) {
                throw new Exception("There should be no cause. Won't try DNS");
            }
        }
        String kdcs = config.getKDCList("X");
        if (!kdcs.equals("a.com.:88 b.com.:99") &&
                !kdcs.equals("a.com. b.com.:99")) {
            throw new Exception("Strange KDC: [" + kdcs + "]");
        };
    }
}
