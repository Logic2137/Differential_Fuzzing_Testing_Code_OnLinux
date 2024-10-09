



import java.util.*;
import java.lang.annotation.*;
import java.lang.reflect.*;

public class NonInheritableContainee {

    @Retention(RetentionPolicy.RUNTIME)
    @Repeatable(InheritedAnnotationContainer.class)
    @interface NonInheritedAnnotationRepeated {
        String name();
    }

    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @interface InheritedAnnotationContainer {
        NonInheritedAnnotationRepeated[] value();
    }

    @NonInheritedAnnotationRepeated(name="A")
    @NonInheritedAnnotationRepeated(name="B")
    class Parent {}
    class Sample extends Parent {}


    public static void main(String[] args) {

        Annotation[] anns = Sample.class.getAnnotationsByType(
                NonInheritedAnnotationRepeated.class);

        if (anns.length != 0)
            throw new RuntimeException("Non-@Inherited containees should not " +
                    "be inherited even though its container is @Inherited.");
    }
}
