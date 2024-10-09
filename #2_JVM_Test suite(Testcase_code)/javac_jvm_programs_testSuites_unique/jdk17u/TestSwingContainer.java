import java.beans.BeanDescriptor;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.util.Objects;
import javax.swing.SwingContainer;

public class TestSwingContainer {

    public static void main(String[] args) throws Exception {
        test(X.class, null, null);
        test(H.class, true, "");
        test(G.class, true, "");
        test(F.class, true, "method");
        test(E.class, false, "");
        test(D.class, false, "");
        test(C.class, true, "");
        test(B.class, false, "method");
        test(A.class, true, "method");
    }

    private static void test(Class<?> type, Object iC, Object cD) throws Exception {
        System.out.println(type);
        BeanInfo info = Introspector.getBeanInfo(type);
        BeanDescriptor bd = info.getBeanDescriptor();
        test(bd, "isContainer", iC);
        test(bd, "containerDelegate", cD);
    }

    private static void test(BeanDescriptor bd, String name, Object expected) {
        Object value = bd.getValue(name);
        System.out.println("\t" + name + " = " + value);
        if (!Objects.equals(value, expected)) {
            throw new Error(name + ": expected = " + expected + "; actual = " + value);
        }
    }

    public static class X {
    }

    @SwingContainer()
    public static class H {
    }

    @SwingContainer(delegate = "")
    public static class G {
    }

    @SwingContainer(delegate = "method")
    public static class F {
    }

    @SwingContainer(false)
    public static class E {
    }

    @SwingContainer(value = false, delegate = "")
    public static class D {
    }

    @SwingContainer(value = true, delegate = "")
    public static class C {
    }

    @SwingContainer(value = false, delegate = "method")
    public static class B {
    }

    @SwingContainer(value = true, delegate = "method")
    public static class A {
    }
}
