import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.TypeVariable;
import java.util.concurrent.Callable;

public class TypeVariableBoundParameterIndex {

    public static void main(String[] args) throws Exception {
        TypeVariable<?>[] variables = Sample.class.getTypeParameters();
        for (int i = 0; i < 2; i++) {
            TypeVariable<?> variable = variables[i];
            AnnotatedType[] bounds = variable.getAnnotatedBounds();
            AnnotatedType bound = bounds[0];
            AnnotatedParameterizedType parameterizedType = (AnnotatedParameterizedType) bound;
            AnnotatedType[] actualTypeArguments = parameterizedType.getAnnotatedActualTypeArguments();
            Annotation[] annotations = actualTypeArguments[0].getAnnotations();
            if (annotations.length != 1 || annotations[0].annotationType() != TypeAnnotation.class) {
                throw new AssertionError();
            }
        }
        TypeVariable<?> variable = variables[2];
        AnnotatedType[] bounds = variable.getAnnotatedBounds();
        AnnotatedType bound = bounds[0];
        AnnotatedParameterizedType parameterizedType = (AnnotatedParameterizedType) bound;
        AnnotatedType[] actualTypeArguments = parameterizedType.getAnnotatedActualTypeArguments();
        Annotation[] annotations = actualTypeArguments[0].getAnnotations();
        if (annotations.length != 0) {
            throw new AssertionError();
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE_USE)
    @interface TypeAnnotation {
    }

    static class Sample<T extends Callable<@TypeAnnotation ?>, S extends Callable<@TypeAnnotation ?>, U extends Callable<?>> {
    }
}
