import java.lang.annotation.Target;
import java.lang.annotation.*;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Constructor;

public class TestReceiverTypeParameterizedConstructor<T> {

    public static void main(String[] args) throws NoSuchMethodException {
        doAssert(TestReceiverTypeParameterizedConstructor.Inner.class);
        doAssert(TestReceiverTypeParameterizedConstructor.Inner.Inner2.class);
    }

    private static void doAssert(Class<?> c) throws NoSuchMethodException {
        Constructor<?> constructor = c.getDeclaredConstructor(c.getDeclaringClass());
        AnnotatedType receiverType = constructor.getAnnotatedReceiverType();
        AnnotatedParameterizedType parameterizedType = (AnnotatedParameterizedType) receiverType;
        int count = 0;
        do {
            AnnotatedType[] arguments = parameterizedType.getAnnotatedActualTypeArguments();
            Annotation[] annotations = arguments[0].getAnnotations();
            if (annotations.length != 1 || !(annotations[0] instanceof TypeAnnotation) || ((TypeAnnotation) annotations[0]).value() != count++) {
                throw new AssertionError();
            }
            parameterizedType = (AnnotatedParameterizedType) parameterizedType.getAnnotatedOwnerType();
        } while (parameterizedType != null);
    }

    class Inner<S> {

        Inner() {
        }

        class Inner2 {

            Inner2() {
            }
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE_USE)
    @interface TypeAnnotation {

        int value();
    }
}
