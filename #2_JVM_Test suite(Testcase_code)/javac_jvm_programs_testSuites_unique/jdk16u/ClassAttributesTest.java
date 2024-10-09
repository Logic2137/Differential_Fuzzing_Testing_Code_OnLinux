



public class ClassAttributesTest {

    class NestedClass {}

    static int test(Class<?> clazz, boolean anonymous, boolean local, boolean member) {
        if (clazz.isAnonymousClass() != anonymous) {
            System.err.println("Unexpected isAnonymousClass value for " +
                               clazz.getName() + " expected: " + anonymous +
                               " got: " + (!anonymous));
            return 1;
        }
        if (clazz.isLocalClass() != local) {
            System.err.println("Unexpected isLocalClass value for " +
                               clazz.getName() + " expected: " + local +
                               " got: " + (!local));
            return 1;
        }
        if (clazz.isMemberClass() != member) {
            System.err.println("Unexpected isMemberClass status for " +
                               clazz.getName() + " expected: " + member +
                               " got: " + (!member));
            return 1;
        }
        return 0;
    }

    public static void main(String argv[]) {
        int failures = 0;

        class LocalClass {}
        Cloneable clone = new Cloneable() {};
        Runnable lambda = () -> System.out.println("run");

        failures += test(ClassAttributesTest.class,       false, false, false);
        failures += test(NestedClass.class,               false, false, true);
        failures += test(LocalClass.class,                false, true,  false);
        failures += test(clone.getClass(),                true,  false, false);

        
        
        failures += test(lambda.getClass(),               false, false, false);

        if (failures != 0)
            throw new RuntimeException("Test failed with " + failures  + " failures.");
    }
}
