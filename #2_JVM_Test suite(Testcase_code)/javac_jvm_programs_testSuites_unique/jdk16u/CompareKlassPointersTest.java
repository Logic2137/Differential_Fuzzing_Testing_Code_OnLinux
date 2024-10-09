



package compiler.c2;



public class CompareKlassPointersTest {

    static A a;

    public static void main(String[] args) {
        a = new C();
        for (int i = 0; i < 10_000; i++) {
            test();
        }
    }

    public static int test() {
        Object a = getA();

        
        if (a instanceof B) {
            return 1;
        }
        return 0;
    }

    private static Object getA() {
        return a;
    }
}

class A { }

class B { }

class C extends A { }
