import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;

public class EraseClassInfoAnnotationValueTest {

    @Retention(RetentionPolicy.RUNTIME)
    public @interface A {

        Class<?> value();
    }

    static class ParametricType<T> {

        @A(Inner.class)
        public static class Nested {
        }

        public class Inner {
        }
    }

    public static void main(String[] args) {
        Class<?> clazz = ParametricType.Nested.class.getAnnotation(A.class).value();
        if (!clazz.equals(ParametricType.Inner.class)) {
            throw new AssertionError(clazz);
        }
    }
}
