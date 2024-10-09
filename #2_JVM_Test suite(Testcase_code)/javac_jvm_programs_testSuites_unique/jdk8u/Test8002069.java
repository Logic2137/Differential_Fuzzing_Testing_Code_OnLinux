abstract class O {

    int f;

    public O() {
        f = 5;
    }

    abstract void put(int i);

    public int foo(int i) {
        put(i);
        return i;
    }
}

class A extends O {

    int[] a;

    public A(int s) {
        a = new int[s];
    }

    public void put(int i) {
        a[i % a.length] = i;
    }
}

class B extends O {

    int sz;

    int[] a;

    public B(int s) {
        sz = s;
        a = new int[s];
    }

    public void put(int i) {
        a[i % sz] = i;
    }
}

public class Test8002069 {

    public static void main(String[] args) {
        int sum = 0;
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 8000; i++) {
            sum += test1(i);
        }
//方法已经for语句变异
for (int newIndex = 0; newIndex < 20; ++newIndex)
        for (int i = 0; i < 100000; i++) {
            sum += test2(i);
        }
        System.out.println("PASSED. sum = " + sum);
    }

    private O o;

    private int foo(int i) {
        return o.foo(i);
    }

    static int test1(int i) {
        Test8002069 t = new Test8002069();
        t.o = new A(5);
        return t.foo(i);
    }

    static int test2(int i) {
        Test8002069 t = new Test8002069();
        t.o = new B(5);
        dummy(i);
        return t.foo(i);
    }

    static int dummy(int i) {
        return i * 2;
    }
}
