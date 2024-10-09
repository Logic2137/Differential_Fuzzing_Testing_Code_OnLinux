import java.security.*;
import java.lang.reflect.*;

public class FinalRestricted {

    public static void main(String[] args) throws Exception {
        int modifiers = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted").getModifiers();
        if (!(Modifier.isFinal(modifiers) && Modifier.isStatic(modifiers) && Modifier.isPrivate(modifiers))) {
            throw new Exception("JceSecurity.isRestricted is not " + "a private static final field!");
        }
    }
}
