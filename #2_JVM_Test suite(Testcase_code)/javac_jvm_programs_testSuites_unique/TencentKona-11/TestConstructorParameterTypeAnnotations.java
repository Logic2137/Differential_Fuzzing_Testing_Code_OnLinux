



import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;


public class TestConstructorParameterTypeAnnotations {
    public static void main(String... args) {
        int errors = 0;
        Class<?>[] classes = {NestedClass0.class,
                              NestedClass1.class,
                              NestedClass2.class,
                              NestedClass3.class,
                              NestedClass4.class,
                              StaticNestedClass0.class,
                              StaticNestedClass1.class,
                              StaticNestedClass2.class };

        for (Class<?> clazz : classes) {
            for (Constructor<?> ctor : clazz.getConstructors()) {
                System.out.println(ctor);
                errors += checkGetParameterAnnotations(clazz, ctor);
                errors += checkGetAnnotatedParametersGetAnnotation(clazz, ctor);
            }
        }

        if (errors > 0)
            throw new RuntimeException(errors + " errors.");
        return;
    }

    private static int checkGetParameterAnnotations(Class<?> clazz,
                                                    Constructor<?> ctor) {
        String annotationString =
            Arrays.deepToString(ctor.getParameterAnnotations());
        String expectedString =
            clazz.getAnnotation(ExpectedGetParameterAnnotations.class).value();

        if (!Objects.equals(annotationString, expectedString)) {
            System.err.println("Annotation mismatch on " + ctor +
                               "\n\tExpected:" + expectedString +
                               "\n\tActual:  " + annotationString);
            return 1;
        }
        return 0;
    }

    private static int checkGetAnnotatedParametersGetAnnotation(Class<?> clazz,
                                                       Constructor<?> ctor) {
        int errors = 0;
        int i = 0;
        ExpectedParameterTypeAnnotations epa =
            clazz.getAnnotation(ExpectedParameterTypeAnnotations.class);

        for (AnnotatedType param : ctor.getAnnotatedParameterTypes() ) {
            String annotationString =
                Objects.toString(param.getAnnotation(MarkerTypeAnnotation.class));
            String expectedString = epa.value()[i];

            if (!Objects.equals(annotationString, expectedString)) {
                System.err.println("Annotation mismatch on " + ctor +
                                   " on param " + param +
                                   "\n\tExpected:" + expectedString +
                                   "\n\tActual:  " + annotationString);
                errors++;
            }
            i++;
        }
        return errors;
    }

    @ExpectedGetParameterAnnotations("[[]]")
    @ExpectedParameterTypeAnnotations({"null"})
    public class NestedClass0 {
        public NestedClass0() {}
    }

    @ExpectedGetParameterAnnotations("[[], []]")
    @ExpectedParameterTypeAnnotations({
        "null",
        "@TestConstructorParameterTypeAnnotations$MarkerTypeAnnotation(value=1)"})
    public class NestedClass1 {
        public NestedClass1(@MarkerTypeAnnotation(1) int parameter) {}
    }

    @ExpectedGetParameterAnnotations("[[], [], []]")
    @ExpectedParameterTypeAnnotations({
        "null",
        "@TestConstructorParameterTypeAnnotations$MarkerTypeAnnotation(value=2)",
        "null"})
    public class NestedClass2 {
        public NestedClass2(@MarkerTypeAnnotation(2) int parameter1,
                            int parameter2) {}
    }

    @ExpectedGetParameterAnnotations("[[], [], []]")
    @ExpectedParameterTypeAnnotations({
        "null",
        "@TestConstructorParameterTypeAnnotations$MarkerTypeAnnotation(value=3)",
        "null"})
    public class NestedClass3 {
        public <P> NestedClass3(@MarkerTypeAnnotation(3) P parameter1,
                                int parameter2) {}
    }

    @ExpectedGetParameterAnnotations("[[], [], []]")
    @ExpectedParameterTypeAnnotations({
        "null",
        "@TestConstructorParameterTypeAnnotations$MarkerTypeAnnotation(value=4)",
        "null"})
    public class NestedClass4 {
        public <P, Q> NestedClass4(@MarkerTypeAnnotation(4) P parameter1,
                                   Q parameter2) {}
    }

    @ExpectedGetParameterAnnotations("[]")
    @ExpectedParameterTypeAnnotations({"null"})
    public static class StaticNestedClass0 {
        public StaticNestedClass0() {}
    }

    @ExpectedGetParameterAnnotations("[[]]")
    @ExpectedParameterTypeAnnotations({
        "@TestConstructorParameterTypeAnnotations$MarkerTypeAnnotation(value=1)"})
    public static class StaticNestedClass1 {
        public StaticNestedClass1(@MarkerTypeAnnotation(1) int parameter) {}
    }

    @ExpectedGetParameterAnnotations("[[], []]")
    @ExpectedParameterTypeAnnotations({
        "@TestConstructorParameterTypeAnnotations$MarkerTypeAnnotation(value=2)",
        "null"})
    public static class StaticNestedClass2 {
        public StaticNestedClass2(@MarkerTypeAnnotation(2) int parameter1,
                                  int parameter2) {}
    }

    @ExpectedGetParameterAnnotations("[[], []]")
    @ExpectedParameterTypeAnnotations({
        "@TestConstructorParameterTypeAnnotations$MarkerTypeAnnotation(value=3)",
        "null"})
    public static class StaticNestedClass3 {
         public <P> StaticNestedClass3(@MarkerTypeAnnotation(3) P parameter1,
                                      int parameter2) {}
    }

    @ExpectedGetParameterAnnotations("[[], []]")
    @ExpectedParameterTypeAnnotations({
        "@TestConstructorParameterTypeAnnotations$MarkerTypeAnnotation(value=4)",
        "null"})
    public static class StaticNestedClass4 {
        public <P, Q> StaticNestedClass4(@MarkerTypeAnnotation(4) P parameter1,
                                         Q parameter2) {}
    }

    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MarkerTypeAnnotation {
        int value();
    }

    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ExpectedGetParameterAnnotations {
        String value();
    }

    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ExpectedParameterTypeAnnotations {
        String[] value();
    }
}
