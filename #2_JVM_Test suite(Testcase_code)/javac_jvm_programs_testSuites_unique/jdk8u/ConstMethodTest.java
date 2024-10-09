import java.util.*;
import java.lang.annotation.*;
import java.lang.reflect.*;
import java.io.Serializable;

@Retention(RetentionPolicy.RUNTIME)
@interface MyAnnotation {

    public String name();

    public String value();

    public String date() default "today";
}

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface TypeAnno {

    String value();
}

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@interface TypeAnno2 {

    String value();
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@interface Named {

    String value();
}

@Retention(RetentionPolicy.RUNTIME)
@interface ScalarTypesWithDefault {

    byte b() default 11;

    short s() default 12;

    int i() default 13;

    long l() default 14;

    char c() default 'V';
}

class OkException extends RuntimeException {
}

@MyAnnotation(name = "someName", value = "Hello World")
public class ConstMethodTest {

    @TypeAnno("constructor")
    public ConstMethodTest() {
    }

    public ConstMethodTest(int i) {
    }

    private static void check(boolean b) {
        if (!b)
            throw new RuntimeException();
    }

    private static void fail(String msg) {
        System.err.println(msg);
        throw new RuntimeException();
    }

    private static void equal(Object x, Object y) {
        if (x == null ? y == null : x.equals(y)) {
        } else {
            fail(x + " not equal to " + y);
        }
    }

    private static final String[] parameter_names = { "parameter", "parameter2", "x" };

    @MyAnnotation(name = "someName", value = "Hello World")
    static <T> void kitchenSinkFunc(@Named(value = "aName") String parameter, @Named("bName") String parameter2, @ScalarTypesWithDefault T x) throws @TypeAnno("RE") @TypeAnno2("RE2") RuntimeException, NullPointerException, @TypeAnno("AIOOBE") ArrayIndexOutOfBoundsException {
        int i, j, k;
        try {
            System.out.println("calling kitchenSinkFunc " + parameter);
            throw new OkException();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void test1() throws Throwable {
        for (Method m : ConstMethodTest.class.getDeclaredMethods()) {
            if (m.getName().equals("kitchenSinkFunc")) {
                Annotation[][] ann = m.getParameterAnnotations();
                equal(ann.length, 3);
                Annotation foo = ann[0][0];
                Annotation bar = ann[1][0];
                equal(foo.toString(), "@Named(value=aName)");
                equal(bar.toString(), "@Named(value=bName)");
                check(foo.equals(foo));
                check(bar.equals(bar));
                check(!foo.equals(bar));
                Annotation[] ann2 = m.getAnnotations();
                equal(ann2.length, 1);
                Annotation mann = ann2[0];
                equal(mann.toString(), "@MyAnnotation(date=today, name=someName, value=Hello World)");
                Parameter[] parameters = m.getParameters();
                if (parameters == null)
                    throw new Exception("getParameters should never be null");
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
                for (int i = 0; i < parameters.length; i++) {
                    Parameter p = parameters[i];
                    equal(parameters[i].getName(), parameter_names[i]);
                }
            }
        }
    }

    private static void testConstructor() throws Exception {
        for (Constructor c : ConstMethodTest.class.getDeclaredConstructors()) {
            Annotation[] aa = c.getAnnotatedReturnType().getAnnotations();
            if (c.getParameterTypes().length == 1) {
                check(aa.length == 0);
            } else if (c.getParameterTypes().length == 0) {
                check(aa.length == 1);
                check(((TypeAnno) aa[0]).value().equals("constructor"));
            } else {
                check(false);
            }
        }
    }

    public static void main(java.lang.String[] unused) throws Throwable {
        kitchenSinkFunc("parameter", "param2", 5);
        test1();
        testConstructor();
    }
}
