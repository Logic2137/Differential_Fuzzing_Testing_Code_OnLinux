import java.util.List;

public class StrictfpModifierChecksTest {

    public static void main(String... args) throws Throwable {
        for (String version60ClassName : List.of("AbstractStrictfpMethod60", "AbstractStrictfpIntMethod60")) {
            try {
                Class<?> newClass = Class.forName(version60ClassName);
                throw new RuntimeException("Should not reach; expected ClassFormatError not thrown");
            } catch (ClassFormatError cfe) {
                String message = cfe.getMessage();
                if (!message.contains("has illegal modifier")) {
                    throw new RuntimeException("Unexpected exception message: " + message);
                }
            }
        }
        for (String version61ClassName : List.of("AbstractStrictfpMethod61", "AbstractStrictfpIntMethod61")) {
            Class<?> newClass = Class.forName(version61ClassName);
        }
    }
}
