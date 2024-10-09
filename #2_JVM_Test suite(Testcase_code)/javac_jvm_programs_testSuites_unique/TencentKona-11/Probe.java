



import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.*;
import static java.util.Arrays.*;

@Classes({"java.util.concurrent.FutureTask",
          "java.util.concurrent.ConcurrentHashMap$EntryIterator",
          "java.util.concurrent.ConcurrentHashMap$KeyIterator",
          "java.util.concurrent.ConcurrentHashMap$ValueIterator",
          "java.util.AbstractList$ListItr",
          "java.util.EnumMap$EntryIterator",
          "java.util.EnumMap$KeyIterator",
          "java.util.EnumMap$ValueIterator",
          "java.util.IdentityHashMap$EntryIterator",
          "java.util.IdentityHashMap$KeyIterator",
          "java.util.IdentityHashMap$ValueIterator",
          "java.util.WeakHashMap$EntryIterator",
          "java.util.WeakHashMap$KeyIterator",
          "java.util.WeakHashMap$ValueIterator",
          "java.util.HashMap$EntryIterator",
          "java.util.HashMap$KeyIterator",
          "java.util.HashMap$ValueIterator",
          "java.util.LinkedHashMap$LinkedEntryIterator",
          "java.util.LinkedHashMap$LinkedKeyIterator",
          "java.util.LinkedHashMap$LinkedValueIterator"})
public class Probe {
    public static void main (String... args) throws Throwable {
        Classes classesAnnotation = (Probe.class).getAnnotation(Classes.class);
        List<String> names = new ArrayList<>(asList(classesAnnotation.value()));

        int errs = 0;
        for(String name: names) {
            System.out.println("\nCLASS " + name);
            Class c = Class.forName(name, false, null);
            errs += probe(c);
            System.out.println(errs == 0 ? "  ok" : "  ERRORS:" + errs);
        }

        if (errs > 0 )
            throw new RuntimeException("Errors during probing.");
    }

    static int probe (Class c) {
        int errs = 0;

        try {
            c.getTypeParameters();
            c.getGenericSuperclass();
            c.getGenericInterfaces();
        } catch (Throwable t) {
            errs++;
            System.err.println(t);
        }

        Field[] fields = c.getDeclaredFields();
        if (fields != null)
            for(Field field: fields) {
                try {
                    field.getGenericType();
                } catch (Throwable t) {
                    errs++;
                    System.err.println("FIELD " + field);
                    System.err.println(t);
                }
            }

        Method[] methods = c.getDeclaredMethods();
        if (methods != null)
            for(Method method: methods) {
                try {
                    method.getTypeParameters();
                    method.getGenericReturnType();
                    method.getGenericParameterTypes();
                    method.getGenericExceptionTypes();
                } catch (Throwable t) {
                    errs++;
                    System.err.println("METHOD " + method);
                    System.err.println(t);
                }
            }

        Constructor[] ctors = c.getDeclaredConstructors();
        if (ctors != null)
            for(Constructor ctor: ctors) {
                try {
                    ctor.getTypeParameters();
                    ctor.getGenericParameterTypes();
                    ctor.getGenericExceptionTypes();
                } catch (Throwable t) {
                    errs++;
                    System.err.println("CONSTRUCTOR " + ctor);
                    System.err.println(t);
                }
            }

        return errs;
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface Classes {
    String [] value(); 
}
