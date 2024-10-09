



import java.lang.annotation.*;
import java.lang.reflect.*;

@TestAnnotation
public class EqualityTest {
    public static void main(String... args) throws Exception {
        TestAnnotation annotation =
            EqualityTest.class.getAnnotation(TestAnnotation.class);
        InvocationHandler handler = Proxy.getInvocationHandler(annotation);

        testEquality(annotation, handler,    false);
        testEquality(annotation, annotation, true);
        testEquality(handler,    handler,    true);
    }

    private static void testEquality(Object a, Object b, boolean expected) {
        boolean result = a.equals(b);
        if (result != b.equals(a) || result != expected)
            throw new RuntimeException("Unexpected result");
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface TestAnnotation {
}

