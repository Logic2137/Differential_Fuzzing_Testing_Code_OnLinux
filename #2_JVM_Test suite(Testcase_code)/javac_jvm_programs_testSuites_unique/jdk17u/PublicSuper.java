
package a;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class PublicSuper {

    private static int privateStatic;

    private int privateInstance;

    static int packageStatic;

    int packageInstance;

    protected static int protectedStatic;

    protected int protectedInstance;

    public static int publicStatic;

    public int publicInstance;

    private static int privateStatic() {
        return 42;
    }

    private int privateInstance() {
        return 42;
    }

    static int packageStatic() {
        return 42;
    }

    int packageInstance() {
        return 42;
    }

    protected static int protectedStatic() {
        return 42;
    }

    protected int protectedInstance() {
        return 42;
    }

    public static int publicStatic() {
        return 42;
    }

    public int publicInstance() {
        return 42;
    }

    private PublicSuper(Void _1, Void _2, Void _3) {
    }

    PublicSuper(Void _1, Void _2) {
    }

    protected PublicSuper(Void _1) {
    }

    public PublicSuper() {
    }

    public static void checkAccess(AccessibleObject accessibleObject, Object obj) throws IllegalAccessException, InvocationTargetException, InstantiationException {
        if (accessibleObject instanceof Field) {
            Field field = (Field) accessibleObject;
            field.set(obj, 42);
            field.get(obj);
        } else if (accessibleObject instanceof Method) {
            Method method = (Method) accessibleObject;
            method.invoke(obj);
        } else if (accessibleObject instanceof Constructor) {
            Constructor<?> constructor = (Constructor<?>) accessibleObject;
            Object[] params = new Object[constructor.getParameterCount()];
            constructor.newInstance(params);
        }
    }
}
