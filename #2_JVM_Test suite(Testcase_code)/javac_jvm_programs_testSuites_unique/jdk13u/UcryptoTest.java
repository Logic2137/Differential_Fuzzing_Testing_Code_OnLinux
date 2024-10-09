import java.io.*;
import java.util.*;
import java.lang.reflect.*;
import java.security.*;

public abstract class UcryptoTest {

    protected static final boolean hasUcrypto;

    static {
        hasUcrypto = (Security.getProvider("OracleUcrypto") != null);
    }

    private static Provider getCustomizedUcrypto(String config) throws Exception {
        Class clazz = Class.forName("com.oracle.security.ucrypto.OracleUcrypto");
        Constructor cons = clazz.getConstructor(new Class[] { String.class });
        Object obj = cons.newInstance(new Object[] { config });
        return (Provider) obj;
    }

    public abstract void doTest(Provider p) throws Exception;

    public static void main(UcryptoTest test, String config) throws Exception {
        Provider prov = null;
        if (hasUcrypto) {
            if (config != null) {
                prov = getCustomizedUcrypto(config);
            } else {
                prov = Security.getProvider("OracleUcrypto");
            }
        }
        if (prov == null) {
            System.out.println("No OracleUcrypto provider found, skipping test");
            return;
        }
        test.doTest(prov);
    }
}
