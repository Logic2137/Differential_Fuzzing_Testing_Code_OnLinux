
package compiler.c2;

public class Test8002069 {

    static abstract class O {

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

    static class A extends O {

        int[] a;

        public A(int s) {
            a = new int[s];
        }

        public void put(int i) {
            a[i % a.length] = i;
        }
    }

    static class B extends O {

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

    public static void main(String[] args) {
        int sum = 0;
        for (int i = 0; i < 8000; i++) {
            sum += test1(i);
        }
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
