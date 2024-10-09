import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE_USE)
@interface A {
}

class Nesting {

    void top(@A Nesting this) {
    }

    class B {

        void inB(@A B this) {
        }
    }

    void meth(@A Nesting this) {
        class C {

            void inMethod(@A C this) {
            }
        }
    }
}
