import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.*;

public class TestAnnotatedElementDefaults {

    public static void main(String... args) throws SecurityException {
        int failures = 0;
        for (AnnotatedElement annotElement : elementsToTest()) {
            System.out.println(annotElement);
            AnnotatedElementDelegate delegate = new AnnotatedElementDelegate(annotElement);
            failures += testNullHandling(delegate);
            for (Class<? extends Annotation> annotType : annotationsToTest()) {
                failures += AnnotatedElementDelegate.testDelegate(delegate, annotType);
            }
        }
        if (failures > 0) {
            System.err.printf("%d failures%n", failures);
            throw new RuntimeException();
        }
    }

    private static List<AnnotatedElement> elementsToTest() {
        List<AnnotatedElement> annotatedElements = new ArrayList<>();
        annotatedElements.add(TestClass1Super.class);
        annotatedElements.add(TestClass1.class);
        for (Method method : TestClass1.class.getDeclaredMethods()) {
            annotatedElements.add(method);
        }
        return annotatedElements;
    }

    private static List<Class<? extends Annotation>> annotationsToTest() {
        List<Class<? extends Annotation>> annotations = new ArrayList<>();
        annotations.add(Missing.class);
        annotations.add(MissingRepeatable.class);
        annotations.add(DirectlyPresent.class);
        annotations.add(IndirectlyPresent.class);
        annotations.add(IndirectlyPresentContainer.class);
        annotations.add(DirectlyAndIndirectlyPresent.class);
        annotations.add(DirectlyAndIndirectlyPresentContainer.class);
        annotations.add(AssociatedDirectOnSuperClass.class);
        annotations.add(AssociatedDirectOnSuperClassContainer.class);
        annotations.add(AssociatedDirectOnSuperClassIndirectOnSubclass.class);
        annotations.add(AssociatedDirectOnSuperClassIndirectOnSubclassContainer.class);
        annotations.add(AssociatedIndirectOnSuperClassDirectOnSubclass.class);
        annotations.add(AssociatedIndirectOnSuperClassDirectOnSubclassContainer.class);
        return annotations;
    }

    private static int testNullHandling(AnnotatedElementDelegate delegate) {
        int failures = 0;
        try {
            Object result = delegate.getDeclaredAnnotationsByType(null);
            failures++;
        } catch (NullPointerException npe) {
            ;
        }
        try {
            Object result = delegate.getAnnotationsByType(null);
            failures++;
        } catch (NullPointerException npe) {
            ;
        }
        try {
            Object result = delegate.getDeclaredAnnotation(null);
            failures++;
        } catch (NullPointerException npe) {
            ;
        }
        return failures;
    }
}

@AssociatedDirectOnSuperClass(123)
@AssociatedIndirectOnSuperClass(234)
@AssociatedIndirectOnSuperClass(345)
@AssociatedDirectOnSuperClassIndirectOnSubclass(987)
@AssociatedIndirectOnSuperClassDirectOnSubclass(1111)
@AssociatedIndirectOnSuperClassDirectOnSubclass(2222)
class TestClass1Super {
}

@DirectlyPresent(1)
@IndirectlyPresent(10)
@IndirectlyPresent(11)
@AssociatedDirectOnSuperClassIndirectOnSubclass(876)
@AssociatedDirectOnSuperClassIndirectOnSubclass(765)
@AssociatedIndirectOnSuperClassDirectOnSubclass(3333)
class TestClass1 extends TestClass1Super {

    @DirectlyPresent(2)
    @IndirectlyPresentContainer({ @IndirectlyPresent(12) })
    @DirectlyAndIndirectlyPresentContainer({ @DirectlyAndIndirectlyPresent(84), @DirectlyAndIndirectlyPresent(96) })
    public void foo() {
        return;
    }

    @IndirectlyPresentContainer({})
    @DirectlyAndIndirectlyPresentContainer({ @DirectlyAndIndirectlyPresent(11), @DirectlyAndIndirectlyPresent(22) })
    @DirectlyAndIndirectlyPresent(33)
    public void bar() {
        return;
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface Missing {

    int value();
}

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(MissingRepeatableContainer.class)
@interface MissingRepeatable {

    int value();
}

@Retention(RetentionPolicy.RUNTIME)
@interface MissingRepeatableContainer {

    MissingRepeatable[] value();
}

@Retention(RetentionPolicy.RUNTIME)
@interface DirectlyPresent {

    int value();
}

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(IndirectlyPresentContainer.class)
@interface IndirectlyPresent {

    int value();
}

@Retention(RetentionPolicy.RUNTIME)
@interface IndirectlyPresentContainer {

    IndirectlyPresent[] value();
}

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(DirectlyAndIndirectlyPresentContainer.class)
@interface DirectlyAndIndirectlyPresent {

    int value();
}

@Retention(RetentionPolicy.RUNTIME)
@interface DirectlyAndIndirectlyPresentContainer {

    DirectlyAndIndirectlyPresent[] value();
}

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AssociatedDirectOnSuperClassContainer.class)
@interface AssociatedDirectOnSuperClass {

    int value();
}

@Retention(RetentionPolicy.RUNTIME)
@interface AssociatedDirectOnSuperClassContainer {

    AssociatedDirectOnSuperClass[] value();
}

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AssociatedIndirectOnSuperClassContainer.class)
@interface AssociatedIndirectOnSuperClass {

    int value();
}

@Retention(RetentionPolicy.RUNTIME)
@interface AssociatedIndirectOnSuperClassContainer {

    AssociatedIndirectOnSuperClass[] value();
}

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AssociatedDirectOnSuperClassIndirectOnSubclassContainer.class)
@interface AssociatedDirectOnSuperClassIndirectOnSubclass {

    int value();
}

@Retention(RetentionPolicy.RUNTIME)
@interface AssociatedDirectOnSuperClassIndirectOnSubclassContainer {

    AssociatedDirectOnSuperClassIndirectOnSubclass[] value();
}

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(AssociatedIndirectOnSuperClassDirectOnSubclassContainer.class)
@interface AssociatedIndirectOnSuperClassDirectOnSubclass {

    int value();
}

@Retention(RetentionPolicy.RUNTIME)
@interface AssociatedIndirectOnSuperClassDirectOnSubclassContainer {

    AssociatedIndirectOnSuperClassDirectOnSubclass[] value();
}

class AnnotatedElementDelegate implements AnnotatedElement {

    private AnnotatedElement base;

    public AnnotatedElementDelegate(AnnotatedElement base) {
        Objects.requireNonNull(base);
        this.base = base;
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> annotationClass) {
        return base.getAnnotation(annotationClass);
    }

    @Override
    public Annotation[] getAnnotations() {
        return base.getAnnotations();
    }

    @Override
    public Annotation[] getDeclaredAnnotations() {
        return base.getDeclaredAnnotations();
    }

    public AnnotatedElement getBase() {
        return base;
    }

    static int testDelegate(AnnotatedElementDelegate delegate, Class<? extends Annotation> annotationClass) {
        int failures = 0;
        AnnotatedElement base = delegate.getBase();
        failures += annotationArrayCheck(delegate.getDeclaredAnnotationsByType(annotationClass), base.getDeclaredAnnotationsByType(annotationClass), annotationClass, "Equality failure on getDeclaredAnnotationsByType(%s) on %s)%n");
        failures += annotationArrayCheck(delegate.getAnnotationsByType(annotationClass), base.getAnnotationsByType(annotationClass), annotationClass, "Equality failure on getAnnotationsByType(%s) on %s)%n");
        if (!Objects.equals(delegate.getDeclaredAnnotation(annotationClass), base.getDeclaredAnnotation(annotationClass))) {
            failures++;
            System.err.printf("Equality failure on getDeclaredAnnotation(%s) on %s)%n", annotationClass, delegate);
        }
        return failures;
    }

    private static <T extends Annotation> int annotationArrayCheck(T[] delegate, T[] base, Class<? extends Annotation> annotationClass, String message) {
        int failures = 0;
        if (!Objects.deepEquals(delegate, base)) {
            failures = 1;
            System.err.printf(message, annotationClass, delegate);
            System.err.println("Base result:\t" + Arrays.toString(base));
            System.err.println("Delegate result:\t " + Arrays.toString(delegate));
            System.err.println();
        }
        return failures;
    }
}
