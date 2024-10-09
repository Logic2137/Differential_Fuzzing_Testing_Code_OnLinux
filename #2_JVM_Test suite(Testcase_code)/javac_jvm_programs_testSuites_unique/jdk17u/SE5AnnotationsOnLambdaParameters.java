import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

public class SE5AnnotationsOnLambdaParameters {

    @Retention(RetentionPolicy.RUNTIME)
    @interface Annot {
    }

    interface Runnable {

        void run(int x);
    }

    public void run(Runnable r) {
    }

    public static void main(@Annot String[] args) throws ClassNotFoundException {
        new SE5AnnotationsOnLambdaParameters().run((@Annot int x) -> {
            System.out.println(x + args.length);
        });
        Class<?> clazz = Class.forName("SE5AnnotationsOnLambdaParameters");
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().startsWith("lambda$")) {
                for (Annotation[] annots : m.getParameterAnnotations()) {
                    if (annots.length > 0) {
                        throw new AssertionError("Unexpected annotations on lambda parameters");
                    }
                }
            }
        }
    }
}
