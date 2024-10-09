



import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Set;


public class InnerClassToString {
    private static final Class<?>[] genericParamClasses = new Class<?>[] {
        InnerClassToString.class, Set.class
    };

    private static final Class<?>[] nongenericParamClasses = new Class<?>[] {
        InnerClassToString.class, String.class
    };

    private int errors = 0;

    private void test(Constructor<MyEntity> constructor,
                     Class<?>[] paramClasses) {
        final Parameter[] params = constructor.getParameters();

        for (int i = 0; i < params.length; i++) {
            final Parameter parameter = params[i];
            System.out.println(parameter.toString());

            if (!parameter.getType().equals(paramClasses[i])) {
                errors++;
                System.err.println("Expected type " + paramClasses[i] +
                                   " but got " + parameter.getType());
            }

            System.out.println(parameter.getParameterizedType());
            System.out.println(parameter.getAnnotatedType());
        }

    }

    private void run() throws Exception {
        final Constructor<MyEntity> genericConstructor =
            MyEntity.class.getConstructor(InnerClassToString.class, Set.class);

        test(genericConstructor, genericParamClasses);

        final Constructor<MyEntity> nongenericConstructor =
            MyEntity.class.getConstructor(InnerClassToString.class, String.class);

        test(nongenericConstructor, nongenericParamClasses);

        if (errors != 0)
            throw new RuntimeException(errors + " errors in test");
    }

    public static void main(String[] args) throws Exception {
        new InnerClassToString().run();
    }

    public class MyEntity {
        public MyEntity(Set<?> names) {}
        public MyEntity(String names) {}
    }
}
