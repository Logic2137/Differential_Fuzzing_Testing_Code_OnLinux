



import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Executable;
import java.util.Arrays;

public class GetAnnotatedReceiverType {
    public void method() {}
    public void method0(GetAnnotatedReceiverType this) {}
    public static void method4() {}

    class Inner0 {
        public Inner0() {}
    }

    class Inner1 {
        public Inner1(GetAnnotatedReceiverType GetAnnotatedReceiverType.this) {}
    }

    public static class Nested {
        public Nested() {}

        public class NestedInner {
            public NestedInner() { }

            public Class<?> getLocalClass () {
                class NestedInnerLocal { public NestedInnerLocal() {} }
                return NestedInnerLocal.class;
            }

            public Class<?> getAnonymousClass() {
                return new Object() {}.getClass();
            }
        }
    }

    public class Inner2 {
        public Inner2() { }

        public class Inner3 {
            public Inner3() { }

            public Class<?> getLocalClass () {
                class InnerLocal { public InnerLocal() {} }
                return InnerLocal.class;
            }

            public Class<?> getAnonymousClass() {
                return new Object() {}.getClass();
            }
        }

        public Class<?> getLocalClass () {
            class InnerLocal { public InnerLocal() {} }
                return InnerLocal.class;
        }

        public Class<?> getAnonymousClass() {
            return new Object() {}.getClass();
        }
    }

    private static int failures = 0;
    private static int tests = 0;

    public static void main(String[] args) throws NoSuchMethodException {
        checkEmptyAT(GetAnnotatedReceiverType.class.getMethod("method"),
                "getAnnotatedReceiverType for \"method\" should return an empty AnnotatedType");
        checkEmptyAT(Inner0.class.getConstructor(GetAnnotatedReceiverType.class),
                "getAnnotatedReceiverType for a ctor without a \"this\" should return an empty AnnotatedType");

        checkEmptyAT(GetAnnotatedReceiverType.class.getMethod("method0"),
                "getAnnotatedReceiverType for \"method0\" should return an empty AnnotatedType");
        checkEmptyAT(Inner1.class.getConstructor(GetAnnotatedReceiverType.class),
                "getAnnotatedReceiverType for a ctor with a \"this\" should return an empty AnnotatedType");

        checkNull(GetAnnotatedReceiverType.class.getMethod("method4"),
                "getAnnotatedReceiverType() on a static method should return null");

        
        Nested nested = new Nested();
        Nested.NestedInner instance = nested.new NestedInner();
        checkNull(nested.getClass().getConstructors()[0],
                "getAnnotatedReceiverType() on a constructor for a static class should return null");
        checkEmptyAT(instance.getClass().getConstructors()[0],
                "getAnnotatedReceiverType for a ctor without a \"this\" should return an empty AnnotatedType");
        checkNull(instance.getLocalClass().getConstructors()[0],
                "getAnnotatedReceiverType() on a constructor for a local class should return null");
        checkNull(instance.getAnonymousClass().getDeclaredConstructors()[0],
                "getAnnotatedReceiverType() on a constructor for an anonymous class should return null");

        GetAnnotatedReceiverType outer = new GetAnnotatedReceiverType();
        Inner2 instance2 = outer.new Inner2();
        checkEmptyAT(instance2.getClass().getConstructors()[0],
                "getAnnotatedReceiverType for a ctor without a \"this\" should return an empty AnnotatedType");
        checkNull(instance2.getLocalClass().getConstructors()[0],
                "getAnnotatedReceiverType() on a constructor for a local class should return null");
        checkNull(instance2.getAnonymousClass().getDeclaredConstructors()[0],
                "getAnnotatedReceiverType() on a constructor for an anonymous class should return null");

        Inner2.Inner3 instance3 = instance2.new Inner3();
        checkEmptyAT(instance3.getClass().getConstructors()[0],
                "getAnnotatedReceiverType for a ctor without a \"this\" should return an empty AnnotatedType");
        checkNull(instance3.getLocalClass().getConstructors()[0],
                "getAnnotatedReceiverType() on a constructor for a local class should return null");
        checkNull(instance3.getAnonymousClass().getDeclaredConstructors()[0],
                "getAnnotatedReceiverType() on a constructor for an anonymous class should return null");

        if (failures != 0)
            throw new RuntimeException("Test failed, see log for details");
        else if (tests != 15)
            throw new RuntimeException("Not all cases ran, failing");
    }

    private static void checkNull(Executable e, String msg) {
        AnnotatedType a = e.getAnnotatedReceiverType();
        if (a != null) {
            failures++;
            System.err.println(msg + ": " + e);
        }
        tests++;
    }

    private static void checkEmptyAT(Executable e, String msg) {
        AnnotatedType a = e.getAnnotatedReceiverType();
        if (a.getAnnotations().length != 0) {
            failures++;
            System.err.print(msg + ": " + e);
        }
        tests++;
    }
}
