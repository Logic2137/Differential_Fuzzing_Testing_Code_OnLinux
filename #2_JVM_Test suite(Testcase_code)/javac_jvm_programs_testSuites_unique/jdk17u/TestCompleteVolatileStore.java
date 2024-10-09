
package compiler.macronodes;

public class TestCompleteVolatileStore {

    int i1 = 4;

    public void test() {
        A a = new A();
        B b = new B(a);
        B[] arr = new B[i1];
        arr[i1 - 3] = b;
    }

    public static void main(String[] strArr) {
        TestCompleteVolatileStore _instance = new TestCompleteVolatileStore();
        for (int i = 0; i < 10_000; i++) {
            _instance.test();
        }
    }
}

class A {

    volatile long l1;

    A() {
        this.l1 = 256;
    }
}

class B {

    A a;

    B(A a) {
        this.a = a;
    }
}
