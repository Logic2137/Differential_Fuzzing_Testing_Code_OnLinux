import java.lang.reflect.*;

public class CheckAccess {

    public static final void main(String[] args) throws Exception {
        Class clazz = Class.forName("com.sun.security.sasl.util.AbstractSaslImpl");
        Field field = clazz.getDeclaredField("logger");
        if (!Modifier.isFinal(field.getModifiers())) {
            throw new Exception("class member 'logger' must be immutable");
        }
    }
}
