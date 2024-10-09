



import java.lang.reflect.*;
import java.lang.annotation.*;

public class TestAnnotatedElement<A> {
    
    private static <B> B m(B b) {return null;}

    
    private <C> TestAnnotatedElement(){super();}

    public static void main(String... argv) throws ReflectiveOperationException {
        int errors = 0;

        Class<?> clazz = TestAnnotatedElement.class;
        errors += testTypeVariable(clazz.getTypeParameters());
        errors += testTypeVariable(clazz.getDeclaredConstructor().getTypeParameters());
        errors += testTypeVariable(clazz.getDeclaredMethod("m", Object.class).getTypeParameters());

        if (errors > 0)
            throw new RuntimeException(errors + " failures");
    }


    private static int testTypeVariable(TypeVariable<?>[] typeVars) {
        int errors = 0;
        if (typeVars.length == 0)
            return ++errors;

        for(TypeVariable<?> typeVar : typeVars) {
            try {
                typeVar.getAnnotation(null);
                errors++;
            } catch(NullPointerException npe) {
                ; 
            }

            if (typeVar.getAnnotation(SuppressWarnings.class) != null)
                errors++;

            try {
                typeVar.isAnnotationPresent(null);
                errors++;
            } catch(NullPointerException npe) {
                ; 
            }

            if (typeVar.isAnnotationPresent(SuppressWarnings.class))
                errors++;

            if(typeVar.getAnnotations().length != 0)
                errors++;

            if(typeVar.getDeclaredAnnotations().length != 0)
                errors++;
        }
        return errors;
    }
}
