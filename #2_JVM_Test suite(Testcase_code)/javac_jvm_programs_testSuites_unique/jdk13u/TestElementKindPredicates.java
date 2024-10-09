import java.util.Set;
import java.util.function.Predicate;
import javax.lang.model.element.ElementKind;

public class TestElementKindPredicates {

    public static void main(String... args) {
        Set<ElementKind> ALL_KINDS = Set.of(ElementKind.values());
        test(ALL_KINDS, (ElementKind k) -> Set.of(ElementKind.CLASS, ElementKind.ENUM).contains(k), (ElementKind k) -> k.isClass(), "isClass");
        test(ALL_KINDS, (ElementKind k) -> Set.of(ElementKind.FIELD, ElementKind.ENUM_CONSTANT).contains(k), (ElementKind k) -> k.isField(), "isField");
        test(ALL_KINDS, (ElementKind k) -> Set.of(ElementKind.INTERFACE, ElementKind.ANNOTATION_TYPE).contains(k), (ElementKind k) -> k.isInterface(), "isInterface");
    }

    private static void test(Set<ElementKind> kinds, Predicate<ElementKind> expectedPred, Predicate<ElementKind> actualPred, String errorMessage) {
        for (ElementKind kind : kinds) {
            boolean expected = expectedPred.test(kind);
            boolean actual = actualPred.test(kind);
            if (expected != actual) {
                throw new RuntimeException("Error testing ElementKind." + errorMessage + "(" + kind + "):\texpected " + expected + "\tgot " + actual);
            }
        }
    }
}
