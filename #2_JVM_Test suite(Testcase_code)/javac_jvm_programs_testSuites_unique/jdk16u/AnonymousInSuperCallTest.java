



public class AnonymousInSuperCallTest {

    static class Base {
        Base(Object o) {}
    }

    static class Outer {
        class Inner {}
    }

    public static class JavacBug extends Base {
        JavacBug() { super(new Outer().new Inner() {}); }
    }

    public static void main(String[] args) {
        new JavacBug();
    }
}
