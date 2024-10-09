import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import javax.security.auth.*;
import javax.security.auth.login.*;
import javax.security.auth.callback.*;
import com.sun.security.auth.module.KeyStoreLoginModule;

public class ReadOnly {

    private static final String TEST = "OptionTest";

    private static int testnum = 1;

    private static final String O_URL = "keyStoreURL";

    private static final String O_ALIAS = "keyStoreAlias";

    private static final String O_SPASS_URL = "keyStorePasswordURL";

    private static final String O_KPASS_URL = "privateKeyPasswordURL";

    private static String URL;

    private static String SPASS_URL;

    private static String KPASS_URL;

    private static final String ALIAS = "alias";

    private static final char[] STORE_PASS = new char[] { 's', 't', 'o', 'r', 'e', 'P', 'a', 's', 's' };

    private static final char[] KEY_PASS = { 'k', 'e', 'y', 'P', 'a', 's', 's' };

    public static void main(String[] args) throws Exception {
        init();
        testReadOnly();
    }

    private static void init() throws Exception {
        File f = new File(System.getProperty("test.src", ".") + File.separatorChar + TEST + ".keystore");
        URL = f.toURI().toURL().toString();
        f = new File(System.getProperty("test.src", ".") + File.separatorChar + TEST + ".storePass");
        SPASS_URL = f.toURI().toURL().toString();
        f = new File(System.getProperty("test.src", ".") + File.separatorChar + TEST + ".keyPass");
        KPASS_URL = f.toURI().toURL().toString();
    }

    private static void testReadOnly() throws Exception {
        KeyStoreLoginModule m = new KeyStoreLoginModule();
        Subject s = new Subject();
        Map options = new HashMap();
        options.put(O_URL, URL);
        options.put(O_ALIAS, ALIAS);
        options.put(O_SPASS_URL, SPASS_URL);
        options.put(O_KPASS_URL, KPASS_URL);
        m.initialize(s, null, null, options);
        m.login();
        m.commit();
        System.out.println("test " + testnum++ + " passed");
        m.logout();
        if (s.getPrincipals().size() != 0) {
            throw new SecurityException("expected no principals");
        }
        if (s.getPublicCredentials().size() != 0) {
            throw new SecurityException("expected no public creds");
        }
        if (s.getPrivateCredentials().size() != 0) {
            throw new SecurityException("expected no private creds");
        }
        System.out.println("test " + testnum++ + " passed");
        m.login();
        m.commit();
        System.out.println("test " + testnum++ + " passed");
        s.setReadOnly();
        try {
            m.logout();
            throw new SecurityException("expected login exception");
        } catch (LoginException le) {
            System.out.println("test " + testnum++ + " passed");
        }
    }
}
