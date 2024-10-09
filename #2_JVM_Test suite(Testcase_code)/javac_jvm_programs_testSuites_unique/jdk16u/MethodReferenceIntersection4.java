



import java.util.stream.*;

public class MethodReferenceIntersection4 {
    interface I {}
    static abstract class C { }
    static class A extends C implements I { }
    static class B extends C implements I { }

    static String f(I i) { return null; }

    public static void main(String[] args) {
        Stream.of(new A(), new B())
                .map(MethodReferenceIntersection4::f)
                .forEach(System.out::println);
    }
}
