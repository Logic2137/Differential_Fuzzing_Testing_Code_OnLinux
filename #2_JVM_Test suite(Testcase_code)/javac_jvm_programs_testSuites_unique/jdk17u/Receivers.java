
package typeannos;

import java.lang.annotation.*;

class DefaultUnmodified {

    void plain(@RcvrA DefaultUnmodified this) {
    }

    <T> void generic(@RcvrA DefaultUnmodified this) {
    }

    void withException(@RcvrA DefaultUnmodified this) throws Exception {
    }

    String nonVoid(@RcvrA @RcvrB("m") DefaultUnmodified this) {
        return null;
    }

    <T extends Runnable> void accept(@RcvrA DefaultUnmodified this, T r) throws Exception {
    }
}

class PublicModified {

    public final void plain(@RcvrA PublicModified this) {
    }

    public final <T> void generic(@RcvrA PublicModified this) {
    }

    public final void withException(@RcvrA PublicModified this) throws Exception {
    }

    public final String nonVoid(@RcvrA PublicModified this) {
        return null;
    }

    public final <T extends Runnable> void accept(@RcvrA PublicModified this, T r) throws Exception {
    }
}

class WithValue {

    void plain(@RcvrB("m") WithValue this) {
    }

    <T> void generic(@RcvrB("m") WithValue this) {
    }

    void withException(@RcvrB("m") WithValue this) throws Exception {
    }

    String nonVoid(@RcvrB("m") WithValue this) {
        return null;
    }

    <T extends Runnable> void accept(@RcvrB("m") WithValue this, T r) throws Exception {
    }
}

class WithFinal {

    WithFinal afield;

    void plain(@RcvrB("m") final WithFinal afield) {
    }

    <T> void generic(@RcvrB("m") final WithFinal afield) {
    }

    void withException(@RcvrB("m") final WithFinal afield) throws Exception {
    }

    String nonVoid(@RcvrB("m") final WithFinal afield) {
        return null;
    }

    <T extends Runnable> void accept(@RcvrB("m") final WithFinal afield, T r) throws Exception {
    }
}

class WithBody {

    Object f;

    void field(@RcvrA WithBody this) {
        this.f = null;
    }

    void meth(@RcvrA WithBody this) {
        this.toString();
    }
}

class Generic1<X> {

    void test1(Generic1<X> this) {
    }

    void test2(@RcvrA Generic1<X> this) {
    }

    void test3(Generic1<@RcvrA X> this) {
    }

    void test4(@RcvrA Generic1<@RcvrA X> this) {
    }
}

class Generic2<@RcvrA X> {

    void test1(Generic2<X> this) {
    }

    void test2(@RcvrA Generic2<X> this) {
    }

    void test3(Generic2<@RcvrA X> this) {
    }

    void test4(@RcvrA Generic2<@RcvrA X> this) {
    }
}

class Generic3<X extends @RcvrA Object> {

    void test1(Generic3<X> this) {
    }

    void test2(@RcvrA Generic3<X> this) {
    }

    void test3(Generic3<@RcvrA X> this) {
    }

    void test4(@RcvrA Generic3<@RcvrA X> this) {
    }
}

class Generic4<X extends @RcvrA Object> {

    <Y> void test1(Generic4<X> this) {
    }

    <Y> void test2(@RcvrA Generic4<X> this) {
    }

    <Y> void test3(Generic4<@RcvrA X> this) {
    }

    <Y> void test4(@RcvrA Generic4<@RcvrA X> this) {
    }
}

class Outer {

    class Inner {

        void none(Outer.Inner this) {
        }

        void outer(@RcvrA Outer.Inner this) {
        }

        void inner(Outer.@RcvrB("i") Inner this) {
        }

        void both(@RcvrA Outer.@RcvrB("i") Inner this) {
        }

        void innerOnlyNone(Inner this) {
        }

        void innerOnly(@RcvrA Inner this) {
        }
    }
}

class GenericOuter<S, T> {

    class GenericInner<U, V> {

        void none(GenericOuter<S, T>.GenericInner<U, V> this) {
        }

        void outer(@RcvrA GenericOuter<S, T>.GenericInner<U, V> this) {
        }

        void inner(GenericOuter<S, T>.@RcvrB("i") GenericInner<U, V> this) {
        }

        void both(@RcvrA GenericOuter<S, T>.@RcvrB("i") GenericInner<U, V> this) {
        }

        void innerOnlyNone(GenericInner<U, V> this) {
        }

        void innerOnly(@RcvrA GenericInner<U, V> this) {
        }
    }
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Documented
@interface RcvrA {
}

@Target({ ElementType.TYPE_USE, ElementType.TYPE_PARAMETER })
@Documented
@interface RcvrB {

    String value();
}
