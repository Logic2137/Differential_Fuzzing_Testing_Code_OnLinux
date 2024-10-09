



import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.security.auth.kerberos.KeyTab;
import javax.xml.crypto.KeySelectorException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import com.sun.security.auth.callback.TextCallbackHandler;
import com.sun.security.jgss.AuthorizationDataEntry;

public class DeprivilegedModuleLoaderTest {

    public static void main(String[] args) {

        boolean pass = true;
        List<Class<?>> classes = getDeprivilegedClasses();
        for (Class<?> cls : classes) {
            try {
                pass &= testPlatformClassLoader(cls);
            } catch (Exception exc) {
                exc.printStackTrace(System.out);
                pass = false;
            }
        }

        if (!pass) {
            throw new RuntimeException("Atleast one test failed.");
        }
    }

    private static List<Class<?>> getDeprivilegedClasses() {

        List<Class<?>> classes = new ArrayList<Class<?>>();
        
        classes.add(XMLSignatureFactory.class);
        
        classes.add(KeySelectorException.class);
        
        classes.add(KeyTab.class);
        
        classes.add(AuthorizationDataEntry.class);
        
        classes.add(TextCallbackHandler.class);
        return classes;
    }

    private static boolean testPlatformClassLoader(Class<?> cls) {

        ClassLoader loader = cls.getClassLoader();
        if (loader == null) {
            throw new RuntimeException(String.format(
                    "Loaded through Bootstrap Classloader: '%s'", cls));
        } else if (!loader.toString().contains("PlatformClassLoader")) {
            throw new RuntimeException(String.format(
                    "Not loaded through Platform ClassLoader: '%s'", cls));
        }
        System.out.println(String.format(
                "Pass: '%s' get loaded through PlatformClassLoader", cls));
        return true;
    }
}
