



import java.lang.reflect.AnnotatedType;
import java.util.Arrays;

public class GetAnnotatedSuperclass {
    private static final Class<?>[] nullTestData = {
        Object.class,
        If.class,
        Object[].class,
        void.class,
        int.class,
    };

    private static final Class<?>[] nonNullTestData = {
        Class.class,
        GetAnnotatedSuperclass.class,
        (new If() {}).getClass(),
        (new Clz() {}).getClass(),
        (new Object() {}).getClass(),
    };

    private static int failed = 0;
    private static int tests = 0;

    public static void main(String[] args) throws Exception {
        testReturnsNull();
        testReturnsEmptyAT();

        if (failed != 0)
            throw new RuntimeException("Test failed, check log for details");
        if (tests != 10)
            throw new RuntimeException("Not all cases ran, failing");
    }

    private static void testReturnsNull() {
        for (Class<?> toTest : nullTestData) {
            tests++;

            Object res = toTest.getAnnotatedSuperclass();

            if (res != null) {
                failed++;
                System.out.println(toTest + ".getAnnotatedSuperclass() returns: "
                        + res + ", should be null");
            }
        }
    }

    private static void testReturnsEmptyAT() {
        for (Class<?> toTest : nonNullTestData) {
            tests++;

            AnnotatedType res = toTest.getAnnotatedSuperclass();

            if (res == null) {
                failed++;
                System.out.println(toTest + ".getAnnotatedSuperclass() returns 'null' should  be non-null");
            } else if (res.getAnnotations().length != 0) {
                failed++;
                System.out.println(toTest + ".getAnnotatedSuperclass() returns: "
                        + Arrays.asList(res.getAnnotations()) + ", should be an empty AnnotatedType");
            }
        }
    }

    interface If {}

    abstract static class Clz {}
}
