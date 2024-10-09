

public class RedefineMethodDelInvokeTarget {
    public void test() {
        myMethod0();
    }

    public static void myMethod0() {
        System.out.println("Target 0: myMethod0: Calling myMethod1()");
        myMethod1();
    }

    private static void myMethod1() {
        System.out.println("Target 0: myMethod1: Calling myMethod2()");
        myMethod2();
    }

    private static void myMethod2() {
        System.out.println("Target 0: myMethod2");
    }
}
