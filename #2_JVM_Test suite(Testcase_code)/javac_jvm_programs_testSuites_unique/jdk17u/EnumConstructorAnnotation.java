import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Constructor;
import java.util.Arrays;

public class EnumConstructorAnnotation {

    public static void main(String[] args) {
        Constructor<?> c = SampleEnum.class.getDeclaredConstructors()[0];
        Annotation[] a1 = c.getParameters()[2].getAnnotations(), a2 = c.getParameterAnnotations()[2];
        for (Annotation[] a : Arrays.asList(a1, a2)) {
            if (a.length != 1) {
                throw new RuntimeException("Unexpected length " + a.length);
            } else if (a[0].annotationType() != SampleAnnotation.class) {
                throw new RuntimeException("Unexpected type " + a[0]);
            }
        }
    }

    enum SampleEnum {

        INSTANCE("foo");

        SampleEnum(@SampleAnnotation String value) {
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @interface SampleAnnotation {
    }
}
