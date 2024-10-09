class IntersectionFunctionalButComponentsNotTest {

    <T extends Object & A & B> void consume(T arg) {
    }

    void foo() {
        consume(System::gc);
    }

    interface C {

        void c();
    }

    interface A extends C {

        void a();
    }

    interface B extends C {

        default void c() {
        }
    }
}
