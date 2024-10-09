public class IntersectionTypeReceiverTest2 {

    interface I {
    }

    interface J {

        void foo();
    }

    static <T extends I & J> void bar(T t) {
        Runnable r = t::foo;
    }

    public static void main(String[] args) {
        class A implements I, J {

            public void foo() {
            }
        }
        bar(new A());
    }
}
