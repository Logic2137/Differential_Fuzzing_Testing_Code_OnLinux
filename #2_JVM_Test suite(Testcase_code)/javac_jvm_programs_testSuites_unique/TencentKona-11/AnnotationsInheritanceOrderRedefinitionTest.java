import sun.reflect.annotation.AnnotationParser;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public class AnnotationsInheritanceOrderRedefinitionTest {

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface Ann1 {

        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface Ann2 {

        String value();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @interface Ann3 {

        String value();
    }

    @Ann1("A")
    @Ann2("A")
    static class A {
    }

    @Ann3("B")
    static class B extends A {
    }

    @Ann1("C")
    @Ann3("C")
    static class C extends B {
    }

    public static void main(String[] args) {
        StringBuilder msgs = new StringBuilder();
        boolean ok = true;
        ok &= annotationsEqual(msgs, A.class, true, ann(Ann1.class, "A"), ann(Ann2.class, "A"));
        ok &= annotationsEqual(msgs, A.class, false, ann(Ann1.class, "A"), ann(Ann2.class, "A"));
        ok &= annotationsEqual(msgs, B.class, true, ann(Ann3.class, "B"));
        ok &= annotationsEqual(msgs, B.class, false, ann(Ann1.class, "A"), ann(Ann2.class, "A"), ann(Ann3.class, "B"));
        ok &= annotationsEqual(msgs, C.class, true, ann(Ann1.class, "C"), ann(Ann3.class, "C"));
        ok &= annotationsEqual(msgs, C.class, false, ann(Ann1.class, "C"), ann(Ann2.class, "A"), ann(Ann3.class, "C"));
        Annotation[] declaredAnnotatiosA = A.class.getDeclaredAnnotations();
        Annotation[] annotationsA = A.class.getAnnotations();
        Annotation[] declaredAnnotatiosB = B.class.getDeclaredAnnotations();
        Annotation[] annotationsB = B.class.getAnnotations();
        Annotation[] declaredAnnotatiosC = C.class.getDeclaredAnnotations();
        Annotation[] annotationsC = C.class.getAnnotations();
        incrementClassRedefinedCount(A.class);
        incrementClassRedefinedCount(B.class);
        incrementClassRedefinedCount(C.class);
        ok &= annotationsEqualButNotSame(msgs, A.class, true, declaredAnnotatiosA);
        ok &= annotationsEqualButNotSame(msgs, A.class, false, annotationsA);
        ok &= annotationsEqualButNotSame(msgs, B.class, true, declaredAnnotatiosB);
        ok &= annotationsEqualButNotSame(msgs, B.class, false, annotationsB);
        ok &= annotationsEqualButNotSame(msgs, C.class, true, declaredAnnotatiosC);
        ok &= annotationsEqualButNotSame(msgs, C.class, false, annotationsC);
        if (!ok) {
            throw new RuntimeException("test failure\n" + msgs);
        }
    }

    private static boolean annotationsEqualButNotSame(StringBuilder msgs, Class<?> declaringClass, boolean declaredOnly, Annotation[] oldAnns) {
        if (!annotationsEqual(msgs, declaringClass, declaredOnly, oldAnns)) {
            return false;
        }
        Annotation[] anns = declaredOnly ? declaringClass.getDeclaredAnnotations() : declaringClass.getAnnotations();
        List<Annotation> sameAnns = new ArrayList<>();
        for (int i = 0; i < anns.length; i++) {
            if (anns[i] == oldAnns[i]) {
                sameAnns.add(anns[i]);
            }
        }
        if (!sameAnns.isEmpty()) {
            msgs.append(declaredOnly ? "declared " : "").append("annotations for ").append(declaringClass.getSimpleName()).append(" not re-parsed after class redefinition: ").append(toSimpleString(sameAnns)).append("\n");
            return false;
        } else {
            return true;
        }
    }

    private static boolean annotationsEqual(StringBuilder msgs, Class<?> declaringClass, boolean declaredOnly, Annotation... expectedAnns) {
        Annotation[] anns = declaredOnly ? declaringClass.getDeclaredAnnotations() : declaringClass.getAnnotations();
        if (!Arrays.equals(anns, expectedAnns)) {
            msgs.append(declaredOnly ? "declared " : "").append("annotations for ").append(declaringClass.getSimpleName()).append(" are: ").append(toSimpleString(anns)).append(", expected: ").append(toSimpleString(expectedAnns)).append("\n");
            return false;
        } else {
            return true;
        }
    }

    private static Annotation ann(Class<? extends Annotation> annotationType, Object value) {
        return AnnotationParser.annotationForMap(annotationType, Collections.singletonMap("value", value));
    }

    private static String toSimpleString(List<Annotation> anns) {
        return toSimpleString(anns.toArray(new Annotation[anns.size()]));
    }

    private static String toSimpleString(Annotation[] anns) {
        StringJoiner joiner = new StringJoiner(", ");
        for (Annotation ann : anns) {
            joiner.add(toSimpleString(ann));
        }
        return joiner.toString();
    }

    private static String toSimpleString(Annotation ann) {
        Class<? extends Annotation> annotationType = ann.annotationType();
        Object value;
        try {
            value = annotationType.getDeclaredMethod("value").invoke(ann);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        return "@" + annotationType.getSimpleName() + "(" + value + ")";
    }

    private static final Field classRedefinedCountField;

    static {
        try {
            classRedefinedCountField = Class.class.getDeclaredField("classRedefinedCount");
            classRedefinedCountField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new Error(e);
        }
    }

    private static void incrementClassRedefinedCount(Class<?> clazz) {
        try {
            classRedefinedCountField.set(clazz, ((Integer) classRedefinedCountField.get(clazz)) + 1);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
