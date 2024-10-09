import java.lang.annotation.Target;
import java.lang.annotation.*;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;

public class TestReceiverTypeOwner<T> {

    public static void main(String[] args) throws NoSuchMethodException {
        Method method = TestReceiverTypeOwner.Inner.class.getDeclaredMethod("m");
        AnnotatedType receiverType = method.getAnnotatedReceiverType();
        AnnotatedParameterizedType parameterizedType = (AnnotatedParameterizedType) receiverType;
        AnnotatedType owner = parameterizedType.getAnnotatedOwnerType();
        Annotation[] annotations = owner.getAnnotations();
        if (annotations.length != 1 || !(annotations[0] instanceof TypeAnnotation)) {
            throw new AssertionError();
        }
    }

    class Inner {

        void m(@TypeAnnotation TestReceiverTypeOwner<T>.Inner this) {
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE_USE)
    @interface TypeAnnotation {
    }
}
