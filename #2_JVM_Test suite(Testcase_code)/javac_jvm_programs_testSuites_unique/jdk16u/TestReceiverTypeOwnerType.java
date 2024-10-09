



import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;

public class TestReceiverTypeOwnerType<T> {

    public static void main(String[] args) throws Exception {
        AnnotatedType nested = Class.forName(TestReceiverTypeOwnerType.class.getTypeName() + "$Nested").getMethod("method").getAnnotatedReceiverType();
        if (!(nested instanceof AnnotatedParameterizedType)) {
            throw new AssertionError();
        } else if (!(nested.getAnnotatedOwnerType() instanceof AnnotatedParameterizedType)) {
            throw new AssertionError();
        }
        AnnotatedType inner = Inner.class.getMethod("method").getAnnotatedReceiverType();
        if (inner instanceof AnnotatedParameterizedType) {
            throw new AssertionError();
        } else if (inner.getAnnotatedOwnerType() instanceof AnnotatedParameterizedType) {
            throw new AssertionError();
        }
        AnnotatedType nestedInner = GenericInner.class.getMethod("method").getAnnotatedReceiverType();
        if (!(nestedInner instanceof AnnotatedParameterizedType)) {
            throw new AssertionError();
        } else if (nestedInner.getAnnotatedOwnerType() instanceof AnnotatedParameterizedType) {
            throw new AssertionError();
        }
    }

    public class Nested {
        public void method(TestReceiverTypeOwnerType<T>.Nested this) { }
    }

    public static class Inner {
        public void method(TestReceiverTypeOwnerType.Inner this) { }
    }

    public static class GenericInner<S> {
        public void method(TestReceiverTypeOwnerType.GenericInner<S> this) { }
    }
}
