



import java.lang.reflect.*;
import java.lang.annotation.*;
import java.util.*;

public class GenericStringTest {
    public static void main(String argv[]) throws Exception{
        int failures = 0;

        for(Class<?> clazz: List.of(TestClass1.class, TestClass2.class))
            for(Constructor<?> ctor: clazz.getDeclaredConstructors()) {
                ExpectedGenericString egs = ctor.getAnnotation(ExpectedGenericString.class);
                String actual = ctor.toGenericString();
                System.out.println(actual);
                failures += checkForFailure(egs.value(), actual);

                if (ctor.isAnnotationPresent(ExpectedString.class)) {
                    failures += checkForFailure(ctor.getAnnotation(ExpectedString.class).value(),
                                                ctor.toString());
                }
            }

        if (failures > 0) {
            System.err.println("Test failed.");
            throw new RuntimeException();
        }
    }

    private static int checkForFailure(String expected, String actual) {
        if (!expected.equals(actual)) {
            System.err.printf("ERROR: Expected ''%s'';%ngot             ''%s''.\n",
                              expected, actual);
            return 1;
        } else
            return 0;
    }
}

class TestClass1 {
    @ExpectedGenericString(
   "TestClass1(int,double)")
    TestClass1(int x, double y) {}

    @ExpectedGenericString(
   "private TestClass1(int,int)")
    private TestClass1(int x, int param2) {}

    @ExpectedGenericString(
   "private TestClass1(java.lang.Object) throws java.lang.RuntimeException")
    @ExpectedString(
   "private TestClass1(java.lang.Object) throws java.lang.RuntimeException")
    private TestClass1(Object o) throws RuntimeException {}

    @ExpectedGenericString(
   "protected <S,T> TestClass1(S,T) throws java.lang.Exception")
    @ExpectedString(
   "protected TestClass1(java.lang.Object,java.lang.Object) throws java.lang.Exception")
    protected <S, T> TestClass1(S s, T t) throws Exception{}

    @ExpectedGenericString(
   "protected <V extends java.lang.Number & java.lang.Runnable> TestClass1(V)")
    @ExpectedString(
   "protected TestClass1(java.lang.Number)")
    protected <V extends Number & Runnable> TestClass1(V v){}

    @ExpectedGenericString(
   "<E extends java.lang.Exception> TestClass1() throws E")
    @ExpectedString(
   "TestClass1() throws java.lang.Exception")
    <E extends Exception> TestClass1() throws E {}

    @ExpectedGenericString(
   "TestClass1(java.lang.Object...)")
    @ExpectedString(
   "TestClass1(java.lang.Object[])")
    TestClass1(Object... o){}
}

class TestClass2<E> {
    @ExpectedGenericString(
   "public <T> TestClass2(E,T)")
    public <T> TestClass2(E e, T t) {}
}

@Retention(RetentionPolicy.RUNTIME)
@interface ExpectedGenericString {
    String value();
}

@Retention(RetentionPolicy.RUNTIME)
@interface ExpectedString {
    String value();
}
