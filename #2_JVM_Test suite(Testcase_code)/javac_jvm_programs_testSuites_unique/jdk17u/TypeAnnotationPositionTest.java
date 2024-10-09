import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public class TypeAnnotationPositionTest {

    TypeAnnotationPositionTest(char @MyTest [] bar) {
    }

    @Target({ ElementType.TYPE_USE })
    @interface MyTest {
    }

    TypeAnnotationPositionTest test() {
        char @MyTest [] val = new char[] { '1' };
        return new TypeAnnotationPositionTest(val);
    }
}
