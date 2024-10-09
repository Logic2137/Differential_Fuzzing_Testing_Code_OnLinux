



import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;
import java.util.List;

public class TestGenericReturnTypeToString {

    public static void main(String[] args) {
        boolean hasFailures = false;
        for (Method method : TestGenericReturnTypeToString.class.getMethods()) {
            if (method.isAnnotationPresent(ExpectedGenericString.class)) {
                ExpectedGenericString es = method.getAnnotation
                        (ExpectedGenericString.class);
                String result = method.getGenericReturnType().toString();
                if (!es.value().equals(result)) {
                    hasFailures = true;
                    System.err.println("Unexpected result of " +
                            "getGenericReturnType().toString() " +
                            " for " + method.getName()
                            + " expected: " + es.value() + " actual: " + result);
                }
            }
            if (hasFailures) {
                throw new RuntimeException("Test failed");
            }
        }
    }

    @ExpectedGenericString("TestGenericReturnTypeToString$" +
          "FirstInnerClassGeneric<Dummy>$SecondInnerClassGeneric<Dummy>")
    public FirstInnerClassGeneric<Dummy>.SecondInnerClassGeneric<Dummy> foo1() {
        return null;
    }

    @ExpectedGenericString("TestGenericReturnTypeToString$" +
          "FirstInnerClassGeneric<Dummy>$SecondInnerClass")
    public FirstInnerClassGeneric<Dummy>.SecondInnerClass foo2() {
        return null;
    }

    @ExpectedGenericString("TestGenericReturnTypeToString$" +
          "FirstInnerClass$SecondInnerClassGeneric<Dummy>")
    public FirstInnerClass.SecondInnerClassGeneric<Dummy> foo3() {
        return null;
    }

    @ExpectedGenericString("class TestGenericReturnTypeToString$" +
          "FirstInnerClass$SecondInnerClass")
    public FirstInnerClass.SecondInnerClass foo4() {
        return null;
    }

    @ExpectedGenericString(
          "java.util.List<java.lang.String>")
    public java.util.List<java.lang.String> foo5() {
        return null;
    }

    @ExpectedGenericString("interface TestGenericReturnTypeToString$" +
          "FirstInnerClass$Interface")
    public FirstInnerClass.Interface foo6() {
        return null;
    }

    @ExpectedGenericString("TestGenericReturnTypeToString$" +
          "FirstInnerClass$InterfaceGeneric<Dummy>")
    public FirstInnerClass.InterfaceGeneric<Dummy> foo7() {
        return null;
    }

    public static class FirstInnerClass {

        public class SecondInnerClassGeneric<T> {
        }

        public class SecondInnerClass {
        }

        interface Interface {
        }

        interface InterfaceGeneric<T> {
        }
    }

    public class FirstInnerClassGeneric<T> {

        public class SecondInnerClassGeneric<T> {
        }

        public class SecondInnerClass {
        }
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface ExpectedGenericString {
    String value();
}

class Dummy {
}
