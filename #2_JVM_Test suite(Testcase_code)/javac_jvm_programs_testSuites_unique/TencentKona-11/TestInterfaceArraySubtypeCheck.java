



package compiler.types;

public class TestInterfaceArraySubtypeCheck {

    static interface MyInterface { }

    static class MyClassA { }

    static class MyClassB extends MyClassA implements MyInterface { }

    static MyInterface[] getMyInterfaceArray() {
        return new MyClassB[0];
    }

    static MyInterface getMyInterface() {
        return new MyClassB();
    }

    static MyClassA[] test1() {
        return (MyClassA[])getMyInterfaceArray();
    }

    static void test2() {
        if (!(getMyInterfaceArray() instanceof MyClassA[])) {
            throw new RuntimeException("test2 failed");
        }
    }

    static MyClassA test3() {
        return (MyClassA)getMyInterface();
    }

    static void test4() {
        if (!(getMyInterface() instanceof MyClassA)) {
            throw new RuntimeException("test4 failed");
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 50_000; ++i) {
            test1();
            test2();
            test3();
            test4();
        }
    }
}
