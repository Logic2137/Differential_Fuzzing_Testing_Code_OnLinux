import java.lang.reflect.AnnotatedType;
import java.util.Arrays;

public class GetAnnotatedInterfaces {

    private static final Class<?>[] testData = { GetAnnotatedInterfaces.class, (new Clz() {
    }).getClass(), (new Object() {
    }).getClass(), Object[].class, Object[][].class, Object[][][].class, Object.class, void.class, int.class };

    private static int failed = 0;

    private static int tests = 0;

    public static void main(String[] args) throws Exception {
        testReturnsZeroLengthArray();
        if (failed != 0)
            throw new RuntimeException("Test failed, check log for details");
        if (tests != 9)
            throw new RuntimeException("Not all cases ran, failing");
    }

    private static void testReturnsZeroLengthArray() {
        for (Class<?> toTest : testData) {
            tests++;
            AnnotatedType[] res = toTest.getAnnotatedInterfaces();
            if (res == null) {
                failed++;
                System.out.println(toTest + ".class.getAnnotatedInterface() returns" + "'null' should zero length array");
            } else if (res.length != 0) {
                failed++;
                System.out.println(toTest + ".class.getAnnotatedInterfaces() returns: " + Arrays.asList(res) + ", should be a zero length array of AnnotatedType");
            }
        }
    }

    interface If {
    }

    abstract static class Clz {
    }
}
