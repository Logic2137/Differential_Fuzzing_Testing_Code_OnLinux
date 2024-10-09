public class IntersectionOrderTest2 {

    interface A {

        void a();
    }

    interface B {

        void b();
    }

    interface C {

        void c();
    }

    interface AB extends A, B {

        @Override
        default void a() {
            System.out.println("A");
        }

        @Override
        default void b() {
            System.out.println("B");
        }
    }

    public static void main(String[] args) {
        Runnable a = ((A & B & AB & C) () -> System.out.println("Called"))::b;
        a.run();
    }
}
