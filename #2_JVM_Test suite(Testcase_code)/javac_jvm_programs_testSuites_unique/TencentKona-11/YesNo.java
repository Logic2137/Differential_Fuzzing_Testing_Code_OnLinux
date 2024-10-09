


import sun.security.krb5.Config;
import sun.security.krb5.internal.crypto.EType;

import java.util.Arrays;

public class YesNo {
    static Config config = null;
    public static void main(String[] args) throws Exception {
        System.setProperty("java.security.krb5.conf",
                System.getProperty("test.src", ".") +"/yesno.conf");
        config = Config.getInstance();
        check("a", Boolean.TRUE);
        check("b", Boolean.FALSE);
        check("c", Boolean.TRUE);
        check("d", Boolean.FALSE);
        check("e", null);
        check("f", null);

        if (!Arrays.stream(EType.getBuiltInDefaults())
                .anyMatch(n -> n < 4)) {
            throw new Exception();
        }
    }

    static void check(String k, Boolean expected) throws Exception {
        Boolean result = config.getBooleanObject("libdefaults", k);
        if (expected != result) {
            throw new Exception("value for " + k + " is " + result);
        }
    }
}
