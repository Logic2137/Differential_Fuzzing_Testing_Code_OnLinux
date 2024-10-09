



import java.util.function.BinaryOperator;

class MethodReferenceStaticNotAccessibleTest_Foo {
    MethodReferenceStaticNotAccessibleTest_Foo m(MethodReferenceStaticNotAccessibleTest_Foo foo) { return null; }
    private static void m(MethodReferenceStaticNotAccessibleTest_Foo foo1, MethodReferenceStaticNotAccessibleTest_Foo foo2) {}
}

public class MethodReferenceStaticNotAccessibleTest {
    <T> void m(T t, BinaryOperator<T> binop) {}

    void test(MethodReferenceStaticNotAccessibleTest_Foo foo) {
        m(foo, MethodReferenceStaticNotAccessibleTest_Foo::m);
    }
}
