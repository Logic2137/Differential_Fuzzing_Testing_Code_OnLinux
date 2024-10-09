



import java.lang.annotation.Target;
import java.lang.annotation.*;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;

public class TestReceiverTypeParameterizedMethod<T> {

    public static void main(String[] args) throws NoSuchMethodException {
        doAssert(TestReceiverTypeParameterizedMethod.class);
        doAssert(TestReceiverTypeParameterizedMethod.Inner.class);
    }

    private static void doAssert(Class<?> c) throws NoSuchMethodException {
        Method method = c.getDeclaredMethod("m");
        AnnotatedType receiverType = method.getAnnotatedReceiverType();
        AnnotatedParameterizedType parameterizedType = (AnnotatedParameterizedType) receiverType;
        int count = 0;
        do {
            AnnotatedType[] arguments = parameterizedType.getAnnotatedActualTypeArguments();
            Annotation[] annotations = arguments[0].getAnnotations();
            if (annotations.length != 1
                    || !(annotations[0] instanceof TypeAnnotation)
                    || ((TypeAnnotation) annotations[0]).value() != count++) {
                throw new AssertionError();
            }
            parameterizedType = (AnnotatedParameterizedType) parameterizedType.getAnnotatedOwnerType();
        } while (parameterizedType != null);
    }

    void m(TestReceiverTypeParameterizedMethod<@TypeAnnotation(0) T> this) { }

    class Inner<S> {
        void m(TestReceiverTypeParameterizedMethod<@TypeAnnotation(1) T>.Inner<@TypeAnnotation(0) S> this) { }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE_USE)
    @interface TypeAnnotation {
        int value();
    }
}
