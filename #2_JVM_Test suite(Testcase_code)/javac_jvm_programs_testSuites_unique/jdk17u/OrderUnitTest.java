import java.lang.annotation.*;
import java.lang.reflect.*;

public class OrderUnitTest {

    public static void main(String[] args) {
        testOrder(Case1.class);
        testOrder(Case2.class);
    }

    private static void testOrder(AnnotatedElement e) {
        Annotation[] decl = e.getDeclaredAnnotations();
        Foo[] declByType = e.getDeclaredAnnotationsByType(Foo.class);
        if (decl[0] instanceof Foo != declByType[0].isDirect() || decl[1] instanceof Foo != declByType[1].isDirect()) {
            throw new RuntimeException("Order of directly / indirectly present " + "annotations from getDeclaredAnnotationsByType does not " + "match order from getDeclaredAnnotations.");
        }
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface FooContainer {

    Foo[] value();
}

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(FooContainer.class)
@interface Foo {

    boolean isDirect();
}

@Foo(isDirect = true)
@FooContainer({ @Foo(isDirect = false) })
class Case1 {
}

@FooContainer({ @Foo(isDirect = false) })
@Foo(isDirect = true)
class Case2 {
}
