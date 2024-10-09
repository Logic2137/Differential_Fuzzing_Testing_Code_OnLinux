



import java.lang.reflect.*;

public class OldenCompilingWithDefaults {
    public OldenCompilingWithDefaults(){}
    static Object f;

    public static void main(String... args) throws Exception {
        Class<OldenCompilingWithDefaults> clazz = OldenCompilingWithDefaults.class;
        Package pkg = clazz.getPackage();
        Constructor<OldenCompilingWithDefaults> ctor = clazz.getConstructor();
        Method m = clazz.getMethod("main", String[].class);
        Field f = clazz.getField("f");

        if(clazz.isAnnotationPresent(SuppressWarnings.class) ||
           pkg.isAnnotationPresent(SuppressWarnings.class) ||
           ctor.isAnnotationPresent(SuppressWarnings.class) ||
           m.isAnnotationPresent(SuppressWarnings.class) ||
           f.isAnnotationPresent(SuppressWarnings.class))
            System.out.println("An annotation is present.");
    }
}
