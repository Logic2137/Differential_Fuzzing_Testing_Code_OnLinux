import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.ArrayList;

interface A1 extends B1, C1 {
}

interface B1 extends D1 {
}

interface C1 extends D1 {
}

interface D1 {

    void m();
}

interface A2 extends B2, C2 {

    void m();
}

interface B2 extends D2 {
}

interface C2 extends D2 {
}

interface D2 {

    void m();
}

interface A3 extends B3, C3 {
}

interface B3 extends D3 {

    void m();
}

interface C3 extends D3 {

    void m();
}

interface D3 {

    void m();
}

interface A4 extends B4, C4 {
}

interface B4 extends D4 {

    void m();
}

interface C4 extends D4 {
}

interface D4 {

    void m();
}

public class StarInheritance {

    private static int n = 1;

    private static void test(Method[] ma, ArrayList expect) {
        System.out.println("Test " + n++);
        if (expect.size() != ma.length) {
            System.err.println("  found methods: " + Arrays.asList(ma));
            System.err.println("  expected locations: " + expect);
            throw new RuntimeException("found = " + ma.length + "; expected = " + expect.size());
        }
        for (int i = 0; i < ma.length; i++) {
            Method m = ma[i];
            System.out.println("  " + m.toString());
            int n;
            if (m.getName().equals("m") && (n = expect.indexOf(m.getDeclaringClass())) != -1) {
                expect.remove(n);
            } else {
                throw new RuntimeException("unable to locate method in class: " + m.getDeclaringClass());
            }
        }
    }

    public static void main(String[] args) {
        Class[] l1 = { D1.class };
        test(A1.class.getMethods(), new ArrayList(Arrays.asList(l1)));
        Class[] l2 = { A2.class };
        test(A2.class.getMethods(), new ArrayList(Arrays.asList(l2)));
        Class[] l3 = { B3.class, C3.class };
        test(A3.class.getMethods(), new ArrayList(Arrays.asList(l3)));
        Class[] l4 = { B4.class };
        test(A4.class.getMethods(), new ArrayList(Arrays.asList(l4)));
    }
}
