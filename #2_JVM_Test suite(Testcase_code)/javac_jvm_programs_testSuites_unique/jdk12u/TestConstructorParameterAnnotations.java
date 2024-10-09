



import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;


public class TestConstructorParameterAnnotations {
    public static void main(String... args) {
        int errors = 0;
        Class<?>[] classes = {NestedClass0.class,
                              NestedClass1.class,
                              NestedClass2.class,
                              NestedClass3.class,
                              NestedClass4.class,
                              StaticNestedClass0.class,
                              StaticNestedClass1.class,
                              StaticNestedClass2.class,
                              StaticNestedClass3.class,
                              StaticNestedClass4.class};

        for (Class<?> clazz : classes) {
            for (Constructor<?> ctor : clazz.getConstructors()) {
                System.out.println(ctor);
                errors += checkGetParameterAnnotations(clazz, ctor);
                errors += checkGetParametersGetAnnotation(clazz, ctor);
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

    private static int checkGetParametersGetAnnotation(Class<?> clazz,
                                                       Constructor<?> ctor) {
        int errors = 0;
        int i = 0;
        ExpectedParameterAnnotations epa =
            clazz.getAnnotation(ExpectedParameterAnnotations.class);

        for (Parameter param : ctor.getParameters() ) {
            String annotationString =
                Objects.toString(param.getAnnotation(MarkerAnnotation.class));
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
    @ExpectedParameterAnnotations({"null"})
    public class NestedClass0 {
        public NestedClass0() {}
    }

    @ExpectedGetParameterAnnotations(
        "[[], " +
        "[@TestConstructorParameterAnnotations$MarkerAnnotation(value=1)]]")
    @ExpectedParameterAnnotations({
        "null",
        "@TestConstructorParameterAnnotations$MarkerAnnotation(value=1)"})
    public class NestedClass1 {
        public NestedClass1(@MarkerAnnotation(1) int parameter) {}
    }

    @ExpectedGetParameterAnnotations(
        "[[], " +
        "[@TestConstructorParameterAnnotations$MarkerAnnotation(value=2)], " +
        "[]]")
    @ExpectedParameterAnnotations({
        "null",
        "@TestConstructorParameterAnnotations$MarkerAnnotation(value=2)",
        "null"})
    public class NestedClass2 {
        public NestedClass2(@MarkerAnnotation(2) int parameter1,
                            int parameter2) {}
    }

    @ExpectedGetParameterAnnotations(
        "[[], " +
        "[@TestConstructorParameterAnnotations$MarkerAnnotation(value=3)], " +
        "[]]")
    @ExpectedParameterAnnotations({
        "null",
        "@TestConstructorParameterAnnotations$MarkerAnnotation(value=3)",
            "null"})
    public class NestedClass3 {
        public <P> NestedClass3(@MarkerAnnotation(3) P parameter1,
                                int parameter2) {}
    }

    @ExpectedGetParameterAnnotations(
        "[[], " +
        "[@TestConstructorParameterAnnotations$MarkerAnnotation(value=4)], " +
        "[]]")
    @ExpectedParameterAnnotations({
        "null",
        "@TestConstructorParameterAnnotations$MarkerAnnotation(value=4)",
        "null"})
    public class NestedClass4 {
        public <P, Q> NestedClass4(@MarkerAnnotation(4) P parameter1,
                                   Q parameter2) {}
    }

    @ExpectedGetParameterAnnotations("[]")
    @ExpectedParameterAnnotations({"null"})
    public static class StaticNestedClass0 {
        public StaticNestedClass0() {}
    }

    @ExpectedGetParameterAnnotations(
        "[[@TestConstructorParameterAnnotations$MarkerAnnotation(value=1)]]")
    @ExpectedParameterAnnotations({
        "@TestConstructorParameterAnnotations$MarkerAnnotation(value=1)"})
    public static class StaticNestedClass1 {
        public StaticNestedClass1(@MarkerAnnotation(1) int parameter) {}
    }

    @ExpectedGetParameterAnnotations(
        "[[@TestConstructorParameterAnnotations$MarkerAnnotation(value=2)], " +
        "[]]")
    @ExpectedParameterAnnotations({
        "@TestConstructorParameterAnnotations$MarkerAnnotation(value=2)",
        "null"})
    public static class StaticNestedClass2 {
        public StaticNestedClass2(@MarkerAnnotation(2) int parameter1,
                            int parameter2) {}
    }

    @ExpectedGetParameterAnnotations(
        "[[@TestConstructorParameterAnnotations$MarkerAnnotation(value=3)], " +
        "[]]")
    @ExpectedParameterAnnotations({
        "@TestConstructorParameterAnnotations$MarkerAnnotation(value=3)",
        "null"})
    public static class StaticNestedClass3 {
        public <P> StaticNestedClass3(@MarkerAnnotation(3) P parameter1,
                                      int parameter2) {}
    }

    @ExpectedGetParameterAnnotations(
        "[[@TestConstructorParameterAnnotations$MarkerAnnotation(value=4)], " +
        "[]]")
    @ExpectedParameterAnnotations({
        "@TestConstructorParameterAnnotations$MarkerAnnotation(value=4)",
        "null"})
    public static class StaticNestedClass4 {
        public <P, Q> StaticNestedClass4(@MarkerAnnotation(4) P parameter1,
                                         Q parameter2) {}
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @interface MarkerAnnotation {
        int value();
    }

    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ExpectedGetParameterAnnotations {
        String value();
    }

    
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ExpectedParameterAnnotations {
        String[] value();
    }
}
