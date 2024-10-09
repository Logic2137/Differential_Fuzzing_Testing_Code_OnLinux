


import java.util.function.Supplier;

public class T8134329 {

    static void assertEquals(Object o1, Object o2) {
        if (!o1.equals(o2)) {
            throw new AssertionError(String.format("Expected %s - found %s", o2, o1));
        }
    }

    public static void main(String[] args) {
        Supplier<String> s1 = new T8134329() { }::m;
        assertEquals(s1.get(), "m");
        Supplier<String> s2 = new T8134329() { }::g;
        assertEquals(s2.get(), "g");
        Supplier<String> s3 = new T8134329() { }::m;
        assertEquals(s3.get(), "m");
    }

    String m() { return "m"; }
    String g() { return "g"; }
}
