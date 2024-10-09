


import sun.security.krb5.Config;
import sun.security.krb5.internal.KDCOptions;

public class KdcDefaultOptions {
    public static void main(String[] args) throws Exception {
        System.setProperty("java.security.krb5.conf",
                System.getProperty("test.src", ".") + "/kdc_default_options.conf");
        Config.refresh();
        KDCOptions options = new KDCOptions();
        if (!options.get(KDCOptions.FORWARDABLE) ||
                !options.get(KDCOptions.PROXIABLE) ||
                !options.get(KDCOptions.RENEWABLE_OK)) {
            throw new Exception(options.toString());
        }
    }
}
