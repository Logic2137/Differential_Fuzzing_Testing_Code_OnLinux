



import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings({ "javadoc", "serial" })
public class AnonymousExtendsTest {

    @Target(ElementType.TYPE_USE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface TA {
        int value();
    }

    public class TestClass extends @TA(1) ArrayList<@TA(2) List<Number>> {
    }

    public void testIt() {
        checkAnnotations(TestClass.class.getAnnotatedSuperclass(),
              "[@AnonymousExtendsTest$TA(1)],[@AnonymousExtendsTest$TA(2)]");
        checkAnnotations(new @TA(3) ArrayList<@TA(4) List<Number>>() {
                         }.getClass().getAnnotatedSuperclass(),
              "[@AnonymousExtendsTest$TA(3)],[@AnonymousExtendsTest$TA(4)]");
    }

    public void checkAnnotations(AnnotatedType type, String expected) {
        String actual = Arrays.asList(((AnnotatedParameterizedType) type)
                                      .getAnnotations())
                                      .toString()
                                       + "," +
                        Arrays.asList(((AnnotatedParameterizedType) type)
                                       .getAnnotatedActualTypeArguments()[0].getAnnotations())
                                       .toString();

        if (!actual.equals(expected))
            throw new AssertionError("Unexpected annotations" + actual);
    }

    public static void main(String[] args) {
        new AnonymousExtendsTest().testIt();
    }
}
